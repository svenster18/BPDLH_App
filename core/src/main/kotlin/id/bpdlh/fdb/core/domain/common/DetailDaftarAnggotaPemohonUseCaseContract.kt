package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.DaftarAnggotaPemohonNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import io.reactivex.Single

/**
 * Created by hahn on 26/09/23.
 * Project: BPDLH App
 */
interface DetailDaftarAnggotaPemohonUseCaseContract {
    fun fetchDetailDaftarAnggota(memberApplicationId: String): Single<ResultState<List<DataDebiturEntity>>>
    fun fetchDetailDaftarAnggotaPemohonNonSosial(): ResultState<DaftarAnggotaPemohonNonSosialEntity>
}