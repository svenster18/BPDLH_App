package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.*
import id.bpdlh.fdb.core.data.post.RequestForgotPasswordPost
import id.bpdlh.fdb.core.data.post.VerifyForgotPasswordPost
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("/user/v1/distribution/fulfillment/profile")
    fun fetchProfile(): Single<BaseDataSourceApi<ProfileSourceApi>>

    @POST("/user/v1/distribution/fulfillment/logout")
    fun fetchLogout(): Single<BaseDataSourceApi<LogoutSourceApi>>

    @POST("/user/v1/distribution/request-forgot-password")
    fun requestForgotPassword(
        @Body body: RequestForgotPasswordPost
    ): Single<BaseDataSourceApi<Any>>

    @PUT("/user/v1/distribution/verify-forgot-password")
    fun verifyForgotPassword(
        @Body body: VerifyForgotPasswordPost
    ): Single<BaseDataSourceApi<VerifyForgotPasswordSourceApi>>

    @GET("v1/group-application/{user_id}")
    fun fetchGroupApplicationByUserid(@Path("user_id") userId: String): Single<BaseDataSourceApi<String>>

    @GET("v1/member-application/{user_id}")
    fun fetchMemberApplicationByUserId(@Path("user_id") userId: String): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    @GET("v1/group-profile/{user_id}")
    fun fetchGroupProfileByUserid(@Path("user_id") userId: String): Single<BaseDataSourceApi<GroupProfileDataSourceApi>>

    @GET("v1/group-profile/activity/{user_id}")
    fun getActivity(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>>
    @GET("v1/group-profile/general-description/{user_id}")
    fun getGeneralDescription(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>>
    @GET("v1/group-profile/partner/{user_id}")
    fun getPartner(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>>


}