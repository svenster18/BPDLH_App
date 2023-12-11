package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.data.common.AlamatRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.DataAlamatSourceApi
import id.bpdlh.fdb.core.data.sources.remote.AlamatApi
import io.reactivex.Single

/**
 * Created by hahn on 17/11/23.
 * Project: BPDLH App
 */
class AlamatRepository(private val api: AlamatApi): AlamatRepositoryContract {

    override fun fetchProvince(): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>> {
        return api.fetchProvince()
    }

    override fun fetchRegency(provinceId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>> {
        return api.fetchRegency(provinceId)
    }

    override fun fetchDistrict(regencyId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>> {
        return api.fetchDistrict(regencyId)
    }

    override fun fetchVillage(districtId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>> {
        return api.fetchVillage(districtId)
    }

    override fun fetchZipCode(villageId: String): Single<BaseDataSourceApi<List<DataAlamatSourceApi>>> {
        return api.fetchZipCode(villageId)
    }
}