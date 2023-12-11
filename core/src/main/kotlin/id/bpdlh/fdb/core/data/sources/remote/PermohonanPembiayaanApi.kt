package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.BusinessPartnerSourceApi
import id.bpdlh.fdb.core.data.entities.DaftarPermohonanSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PermohonanPembiayaanApi {

    @GET("v1/member-application/business-partner/draft/:user_id")
    fun fetchBusinessPartnerDraftByUserId(@Path("user_id") userId: String):
            Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>>

    @GET("v1/member-application/business-partner/:user_id")
    fun fetchBusinessPartnerByUserId(@Path("user_id") userId: String):
            Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>>

    @PUT("v1/member-application/social-forestry/{user_id}")
    fun updateSocialForestry(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/social-forestry/draft/{user_id}")
    fun updateSocialForestryDraft(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/non-social-forestry/{user_id}")
    fun updateNonSocialForestry(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/non-social-forestry/draft/{user_id}")
    fun updateNonSocialForestryDraft(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/delay-cutting/{user_id}")
    fun updateDelayCutting(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/delay-cutting/draft/{user_id}")
    fun updateDelayCuttingDraft(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/non-forestry-comodity/{user_id}")
    fun updateNonForestryComodity(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/non-forestry-comodity/draft/{user_id}")
    fun updateNonForestryComodityDraft(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/non-wood-forest-product/{user_id}")
    fun updateNonWoodForestProduct(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @PUT("v1/member-application/non-wood-forest-product/draft/{user_id}")
    fun updateNonWoodForestProductDraft(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @GET("v1/member-application/other-document/{user_id}")
    fun fetchOtherDocumentByUserId(@Path("user_id") userId: String):
            Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>

    @POST("v1/member-application/other-document")
    fun createOtherDocumentDraft(@Body body: MultipartBody):
            Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>


    @POST("v1/group-application/submit")
    fun submitApplication(@Body body: MultipartBody):
            Single<BaseDataSourceApi<DaftarPermohonanSourceApi>>
}
