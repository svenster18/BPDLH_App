package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.RegistrasiKelompokSourceApi
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by hahn on 08/10/23.
 * Project: BPDLH App
 */
interface RegistrasiApi {

    @POST("v1/group-profile/register")
    fun submitData(@Body body: MultipartBody):
            Single<BaseDataSourceApi<RegistrasiKelompokSourceApi>>

}