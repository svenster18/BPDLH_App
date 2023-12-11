package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.AuthRepositoryContract
import id.bpdlh.fdb.core.data.post.RequestForgotPasswordPost
import id.bpdlh.fdb.core.data.post.VerifyForgotPasswordPost
import id.bpdlh.fdb.core.domain.common.AuthUseCaseContract
import id.bpdlh.fdb.core.domain.entities.LoginEntity
import id.bpdlh.fdb.core.domain.entities.LogoutEntity
import id.bpdlh.fdb.core.domain.entities.ProfileEntity
import id.bpdlh.fdb.core.domain.entities.VerifyForgotPasswordEntity
import id.bpdlh.fdb.core.domain.mapper.map
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single

class AuthUseCase(private val repositoryContract: AuthRepositoryContract) : AuthUseCaseContract {
    override fun login(email: String, password: String): Single<ResultState<LoginEntity>> {
        return repositoryContract.postLogin(email, password).map {
            return@map responseBaseDataSourceApiToResultState(
                it
            ) { response ->
                response.map()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<LoginEntity>(
                it
            )
        }.compose(singleIo())
    }

    override fun fetchProfile(): Single<ResultState<ProfileEntity>> {
        return repositoryContract.fetchProfile().map {
            return@map responseBaseDataSourceApiToResultState(
                it
            ) { response ->
                response.map()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<ProfileEntity>(
                it
            )
        }.compose(singleIo())
    }

    override fun logout(): Single<ResultState<LogoutEntity>> {
        return repositoryContract.postLogout().map {
            return@map responseBaseDataSourceApiToResultState(
                it
            ) { response ->
                response.map()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<LogoutEntity>(
                it
            )
        }.compose(singleIo())
    }

    override fun requestForgotPassword(body: RequestForgotPasswordPost): Single<ResultState<Any>> {
        return repositoryContract.requestForgotPassword(body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun verifyForgotPassword(body: VerifyForgotPasswordPost): Single<ResultState<VerifyForgotPasswordEntity>> {
        return repositoryContract.verifyForgotPassword(body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response.map()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<VerifyForgotPasswordEntity>(it)
        }.compose(singleIo())
    }
}