package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.RequestForgotPasswordPost
import id.bpdlh.fdb.core.data.post.VerifyForgotPasswordPost
import id.bpdlh.fdb.core.domain.entities.LoginEntity
import id.bpdlh.fdb.core.domain.entities.LogoutEntity
import id.bpdlh.fdb.core.domain.entities.ProfileEntity
import id.bpdlh.fdb.core.domain.entities.VerifyForgotPasswordEntity
import io.reactivex.Single

interface AuthUseCaseContract {
    fun login(email: String, password: String): Single<ResultState<LoginEntity>>
    fun fetchProfile(): Single<ResultState<ProfileEntity>>
    fun logout(): Single<ResultState<LogoutEntity>>
    fun requestForgotPassword(body: RequestForgotPasswordPost): Single<ResultState<Any>>
    fun verifyForgotPassword(body: VerifyForgotPasswordPost): Single<ResultState<VerifyForgotPasswordEntity>>
}