package id.bpdlh.fdb.core.domain.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.RetrofitException
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import retrofit2.HttpException


fun <T, R> responseBaseDataSourceApiToResultState(
    baseDataSourceApi: BaseDataSourceApi<T>,
    onSuccess: (T) -> R,
): ResultState<R> {
    when (baseDataSourceApi.code) {
        200 -> {
            val data = baseDataSourceApi.data
            return if (data != null) {
                onSuccess(data)
                ResultState.Success(onSuccess(data))
            } else {
                ResultState.UnknownError(
                    baseDataSourceApi.message ?: "",
                    null, baseDataSourceApi.code
                )
            }
        }

        400 -> {
            return ResultState.BadRequest(
                baseDataSourceApi.message ?: "",
                null, code = baseDataSourceApi.code
            )
        }

        401 -> {
            return ResultState.Unauthorized(
                baseDataSourceApi.message ?: "",
                null, code = baseDataSourceApi.code
            )
        }

        404 -> {
            return ResultState.NotFound(
                baseDataSourceApi.message ?: "",
                null, code = baseDataSourceApi.code
            )
        }

        403 -> {
            return ResultState.Forbidden(
                baseDataSourceApi.message ?: "",
                null, code = baseDataSourceApi.code
            )
        }

        409 -> {
            return ResultState.Conflict(
                baseDataSourceApi.message ?: "",
                null, code = baseDataSourceApi.code
            )
        }

        else -> {
            val data = baseDataSourceApi.data
            return if (data != null) {
                onSuccess(data)
                ResultState.Success(onSuccess(data))
            } else {
                ResultState.UnknownError(
                    baseDataSourceApi.message ?: "",
                    null, baseDataSourceApi.code ?: 0
                )
            }
        }
    }

}

fun <R> responseErrorToResultStateError(
    error: Throwable,
): ResultState<R> {

    when (error) {
        is HttpException -> {
            when (error.code()) {
                400 -> {
                    return ResultState.BadRequest(
                        getMessageFromBody(
                            error
                        ) + " [${error.code()}]", null, code = error.code()
                    )

                }

                401 -> {
                    return ResultState.Unauthorized(
                        getMessageFromBody(
                            error
                        ) + " [${error.code()}]", null, code = error.code()
                    )

                }

                404, 4041 -> {
                    return ResultState.NotFound(
                        getMessageFromBody(
                            error
                        ) + " [${error.code()}]", null, code = error.code()
                    )

                }

                403, 4032 -> {
                    return ResultState.Forbidden(
                        getMessageFromBody(
                            error
                        ), null, code = error.code()
                    )
                }

                409, 406 -> {
                    return ResultState.Conflict(
                        getMessageFromBody(
                            error
                        ), null, code = error.code()
                    )

                }

                422 -> {
                    return ResultState.UnprocessableContent(
                        getMessageFromBody(
                            error
                        ), null, code = error.code()
                    )
                }

                else -> {
                    return if (error.code() >= 500) {
                        ResultState.UnknownError(
                            "Maaf, Terjadi gangguan pada server [${error.code()}]",
                            null, code = error.code()
                        )
                    } else {
                        ResultState.UnknownError(
                            getMessageFromBody(
                                error
                            ) + " [${error.code()}]", null,
                            code = error.code()
                        )
                    }

                }
            }
        }

        is RetrofitException -> {
            return when (error.code) {
                1 -> {
                    ResultState.NoConnection(
                        "Tidak ada koneksi internet", null, error.code
                    )
                }

                2 -> {
                    ResultState.UnknownError(
                        "Terjadi gangguan pada aplikasi [00${error.code}]", null,
                        error.code
                    )
                }

                else -> {
                    ResultState.UnknownError(
                        "Terjadi gangguan pada aplikasi [00${error.code}]", null,
                        error.code ?: 0
                    )
                }
            }

        }

        else -> {
            return ResultState.UnknownError(
                "Maaf, Terjadi gangguan pada server", null, 0
            )
        }
    }

}

private fun getMessageFromBody(throwable: HttpException): String {
    val type = object : TypeToken<BaseDataSourceApi<String>>() {}.type
    var errorResponse: BaseDataSourceApi<String>? = null
    return try {
        throwable.response()?.errorBody()?.let {
            errorResponse = Gson().fromJson(throwable.response()?.errorBody()?.charStream(), type)

        }
        errorResponse?.message ?: "Terjadi gangguan pada server"

    } catch (error: Exception) {
        "Terjadi gangguan pada server"
    }
}

fun <T, R> responseMapsToResultState(
    routeEntity: T,
    onSuccess: (T) -> R,
): ResultState<R> {
    onSuccess(routeEntity)
    return ResultState.Success(onSuccess(routeEntity))
}
