package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import io.reactivex.Single

/**
 * Created by hahn on 20/09/23.
 * Project: BPDLH App
 */
interface DaftarPemohonUseCaseContract {

    fun fetchGroupApplication(userId: String): Single<ResultState<List<DaftarPemohonEntity>>>
}