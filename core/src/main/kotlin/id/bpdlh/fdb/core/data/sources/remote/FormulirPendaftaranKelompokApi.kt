package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.GroupProfileDataSourceApi
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by hahn on 21/10/23.
 * Project: BPDLH App
 */
interface FormulirPendaftaranKelompokApi {

    @PUT("v1/group-profile/social-forestry/draft/{user_id}")
    fun saveDraft(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<GroupProfileDataSourceApi>>

    @PUT("v1/group-profile/social-forestry/{user_id}")
    fun submit(
        @Path("user_id") userId: String,
        @Body body: MultipartBody
    ): Single<BaseDataSourceApi<GroupProfileDataSourceApi>>
}