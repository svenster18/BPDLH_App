package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FormulirKelompokNonSosialApi {

    @PUT("v1/group-profile/non-social-forestry/draft/{user_id}")
    fun postDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<GroupProfileNonSosial>>

    @PUT("v1/group-profile/non-social-forestry/{user_id}")
    fun postUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<GroupProfileNonSosial>>

    @PUT("v1/group-profile/non-social-forestry/{user_id}")
    fun saveMemberRegister(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<Any>>

    @POST("v1/group-profile/non-social-forestry/{user_id}")
    fun submitForm(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<GroupProfileNonSosial>>

    @POST("v1/group-profile/other-document")
    fun postOtherDocument(@Body body: MultipartBody): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>

    @GET("v1/group-profile/other-document/{user_id}")
    fun getOtherDocument(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>

    @GET("v1/group-profile/other-document/draft/{user_id}")
    fun getOtherDocumentDraft(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>

    @DELETE("v1/group-profile/other-document/{document_draft_id}")
    fun deleteOtherDocumentDraft(@Path("document_draft_id") userId: String): Single<BaseDataSourceApi<Any>>

}