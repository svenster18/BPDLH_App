package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.LoginSourceApi
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST

interface IdentityApi {
    @POST("v1/login/email")
    fun fetchLogin(@Body body: MultipartBody): Single<BaseDataSourceApi<LoginSourceApi>>
}