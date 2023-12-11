package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.DataAlamatSourceApi
import io.reactivex.Single

/**
 * Created by hahn on 17/11/23.
 * Project: BPDLH App
 */
interface AlamatRepositoryContract {
    fun fetchProvince(): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    fun fetchRegency(provinceId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    fun fetchDistrict(regencyId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    fun fetchVillage(districtId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>

    fun fetchZipCode(villageId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>>
}