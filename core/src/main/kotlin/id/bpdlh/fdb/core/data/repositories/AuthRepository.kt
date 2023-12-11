package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.data.common.AuthRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.LoginSourceApi
import id.bpdlh.fdb.core.data.post.RequestForgotPasswordPost
import id.bpdlh.fdb.core.data.post.VerifyForgotPasswordPost
import id.bpdlh.fdb.core.data.sources.remote.IdentityApi
import id.bpdlh.fdb.core.data.sources.remote.UserApi
import io.reactivex.Single
import okhttp3.MultipartBody

class AuthRepository(private val identityApi: IdentityApi, private val userApi: UserApi) :
    AuthRepositoryContract {

    override fun postLogin(
        email: String,
        password: String
    ): Single<BaseDataSourceApi<LoginSourceApi>> {
        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
//            .addFormDataPart("password", password)
            .build()
        return identityApi.fetchLogin(multipart)
    }

    override fun fetchProfile() = userApi.fetchProfile()

    override fun postLogout() = userApi.fetchLogout()

    override fun requestForgotPassword(body: RequestForgotPasswordPost) =
        userApi.requestForgotPassword(body)

    override fun verifyForgotPassword(body: VerifyForgotPasswordPost) =
        userApi.verifyForgotPassword(body)
}