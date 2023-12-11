package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.BusinessPartnerSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MemberApplicationApi {
    @GET("v1/member-application/{user_id}")
    fun getMemberApplicationData(@Path("user_id") userId: String): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/delay-cutting/{user_id}")
    fun delayCuttingUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/delay-cutting/draft/{user_id}")
    fun delayCuttingDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/non-forestry-comodity/{user_id}")
    fun nonForestyComodityUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/non-forestry-comodity/draft/{user_id}")
    fun nonForestyComodityDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/non-wood-forest-product/{user_id}")
    fun nonWoodForestProductUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/non-wood-forest-product/draft/{user_id}")
    fun nonWoodForestProductDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @GET("v1/member-application/other-document/{user_id}")
    fun getOtherDocument(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>
    @GET("v1/member-application/other-document/draft/{user_id}")
    fun getOtherDocumentDraft(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>
    @POST("v1/member-application/other-document")
    fun postOtherDocument(@Body body: MultipartBody): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>
    @DELETE("v1/member-application/other-document/{document_id}")
    fun deleteOtherDocument(@Path("document_id") userId: String): Single<BaseDataSourceApi<Any>>
    @GET("v1/member-application/business-partner/draft/{user_id}")
    fun getBusinessPartnerDraft(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>>

   }