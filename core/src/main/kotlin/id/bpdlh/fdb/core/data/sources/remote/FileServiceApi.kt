package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.FileServiceSourceApi
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FileServiceApi {
    @Multipart
    @POST("v1/files")
    fun uploadFile(
        @Part file: MultipartBody.Part,
    ): Single<BaseDataSourceApi<FileServiceSourceApi>>

    @GET("v1/files/{id}")
    fun fetchFile(
        @Path("id") id: String,
    ): Single<BaseDataSourceApi<GetFileSourceApi>>
}