package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.DataAlamatSourceApi
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by hahn on 17/11/23.
 * Project: BPDLH App
 */
interface AlamatApi {

    @GET("v1/region/province")
    fun fetchProvince(): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    @GET("v1/region/regency/{province_id}")
    fun fetchRegency(@Path("province_id") provinceId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    @GET("v1/region/district/{regency_id}")
    fun fetchDistrict(@Path("regency_id") regencyId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    @GET("v1/region/village/{district_id}")
    fun fetchVillage(@Path("district_id") districtId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    @GET("v1/region/zip-code/{village_id}")
    fun fetchZipCode(@Path("village_id") villageId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>



}