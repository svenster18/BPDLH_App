package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonNonSosialEntity

interface DaftarPemohonNonSosialUseCaseContract {

    fun fetchDataPemohon(
        param: HashMap<String, String>,
        type: Int
    ): ResultState<List<DaftarPemohonNonSosialEntity>>
}