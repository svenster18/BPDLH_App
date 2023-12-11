package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDetailEntity
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupApplicationApi {
    @GET("v1/group-application/{user_id}")
    fun getGroupApplication(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<GroupApplicationEntity>>>
    @POST("v1/group-application/create")
    fun create(@Body body: MultipartBody): Single<BaseDataSourceApi<List<MemberApplicationDetailEntity>>>
    @POST("v1/group-application/submit")
    fun submit(@Body body: MultipartBody): Single<BaseDataSourceApi<Any>>
    @GET("v1/group-application/{member_application_id}/member")
    fun getMemberApplication(@Path("member_application_id") userId: String): Single<BaseDataSourceApi<List<MemberApplicationDetailEntity>>>
    @DELETE("v1/group-application/{user_id}")
    fun delete(@Path("user_id") userId: String): Single<BaseDataSourceApi<Any>>
    @GET("v1/member-application/{user_id}")
    fun getMemberApplicationData(@Path("user_id") userId: String): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun delayCuttingUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<Any>>
    @PUT("v1/member-application/delay-cutting/draft/{user_id}")
    fun delayCuttingDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    @PUT("v1/member-application/non-forestry-comodity/{user_id}")
    fun nonForestyComodityUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<Any>>
    @PUT("v1/member-application/non-forestry-comodity/draft/{user_id}")
    fun nonForestyComodityDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<Any>>
    @PUT("v1/member-application/non-wood-forest-product/{user_id}")
    fun nonWoodForestProductUpdate(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<Any>>
    @PUT("v1/member-application/non-wood-forest-product/draft/{user_id}")
    fun nonWoodForestProductDraft(@Path("user_id") userId: String, @Body body: MultipartBody): Single<BaseDataSourceApi<Any>>

   }