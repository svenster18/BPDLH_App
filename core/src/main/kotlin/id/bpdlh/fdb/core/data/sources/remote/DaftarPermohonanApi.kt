package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.DaftarPermohonanSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by hahn on 05/11/23.
 * Project: BPDLH App
 */
interface DaftarPermohonanApi {

    @GET("v1/group-application/{user_id}")
    fun fetchGroupApplication(@Path("user_id") userId: String): Single<BaseDataSourceApi<List<DaftarPermohonanSourceApi>>>

    @GET("v1/group-application/{member_application_id}/member")
    fun fetchGroupMemberApplication(@Path("member_application_id") id: String): Single<BaseDataSourceApi<List<MemberApplicationSourceApi>>>

    @POST("v1/group-application/create")
    fun createDataDebitur(@Body body: MultipartBody): Single<BaseDataSourceApi<List<MemberApplicationSourceApi>>>

    @POST("v1/group-application/submit")
    fun submitDataDebitur(@Body body: MultipartBody): Single<BaseDataSourceApi<DaftarPermohonanSourceApi>>
}