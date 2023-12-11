package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import io.reactivex.Single

interface AlamatUseCaseContract {

    fun fetchProvince(): Single<ResultState<List<DataAlamatEntity>>>

    fun fetchRegency(provinceId: String): Single<ResultState<List<DataAlamatEntity>>>

    fun fetchDistrict(regencyId: String): Single<ResultState<List<DataAlamatEntity>>>

    fun fetchVillage(districtId: String): Single<ResultState<List<DataAlamatEntity>>>

    fun fetchZipCode(villageId: String): Single<ResultState<List<DataAlamatEntity>>>
}