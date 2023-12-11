package id.bpdlh.fdb.core.common.utils


import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import id.bpdlh.fdb.core.common.utils.AppLink.Auth.LOGIN
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type


class RxErrorHandlingCallAdapterFactory : CallAdapter.Factory() {

    private val _original by lazy {
        RxJava2CallAdapterFactory.create()
    }

    companion object {
        lateinit var context: Context

        fun create(context: Context): CallAdapter.Factory {
            Companion.context = context
            return RxErrorHandlingCallAdapterFactory()
        }
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        val wrapped = _original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(wrapped)
    }

    private class RxCallAdapterWrapper<R>(
        private val wrapped: CallAdapter<R, *>
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type = wrapped.responseType()

        override fun adapt(call: Call<R>): Any {
            val adapted = wrapped.adapt(call)
            if (adapted is Observable<*>) {
                return adapted.onErrorResumeNext(Function { Observable.error(asRetrofitException(it)) })
            }
            if (adapted is Maybe<*>) {
                return adapted.onErrorResumeNext(Function { Maybe.error(asRetrofitException(it)) })

            }
            if (adapted is Single<*>) {
                return adapted.onErrorResumeNext {
                    return@onErrorResumeNext Single.error(asRetrofitException(it))
                }
            }
            if (adapted is Completable) {
                return adapted.onErrorResumeNext {
                    return@onErrorResumeNext Completable.error(asRetrofitException(it))
                }
            }
            return adapted
        }


        private fun asRetrofitException(throwable: Throwable): Throwable {
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()

                response?.let {
                    when (throwable.code()) {
                        401 -> {
                            if (LocalPreferences(context).getValueBoolean(
                                    Constants.LOGGED_IN, false
                                )
                            ) {

                                LocalPreferences(context).clearSharedPreference()
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LOGIN))
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                val baseDataSourceApi: BaseDataSourceApi<Any> = BaseDataSourceApi(
                                    code = 401,
                                    data = null,
                                    success = true,
                                    message = "Harap Login Kembali",
                                    meta = null,
                                    statusCode = null,
                                    status = null
                                )
                                val responseBody: Response<Any> = Response.error(
                                    401,
                                    Gson().toJson(baseDataSourceApi).toString().toResponseBody()
                                )
                                return HttpException(responseBody)

                            } else {
                                return throwable

                            }
                        }
                        else -> {
                            return throwable
                        }
                    }
                }

            }

            // A network error happened
            if (throwable is IOException) {
                return RetrofitException.networkError(throwable)
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return throwable
        }


    }
}