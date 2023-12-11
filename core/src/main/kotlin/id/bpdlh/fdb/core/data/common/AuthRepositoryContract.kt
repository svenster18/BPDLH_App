package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.LoginSourceApi
import id.bpdlh.fdb.core.data.entities.LogoutSourceApi
import id.bpdlh.fdb.core.data.entities.ProfileSourceApi
import id.bpdlh.fdb.core.data.entities.VerifyForgotPasswordSourceApi
import id.bpdlh.fdb.core.data.post.RequestForgotPasswordPost
import id.bpdlh.fdb.core.data.post.VerifyForgotPasswordPost
import io.reactivex.Single
import retrofit2.http.Body

interface AuthRepositoryContract {
    fun postLogin(email: String, password: String): Single<BaseDataSourceApi<LoginSourceApi>>
    fun fetchProfile(): Single<BaseDataSourceApi<ProfileSourceApi>>
    fun postLogout(): Single<BaseDataSourceApi<LogoutSourceApi>>
    fun requestForgotPassword(@Body body: RequestForgotPasswordPost): Single<BaseDataSourceApi<Any>>
    fun verifyForgotPassword(@Body body: VerifyForgotPasswordPost): Single<BaseDataSourceApi<VerifyForgotPasswordSourceApi>>
}