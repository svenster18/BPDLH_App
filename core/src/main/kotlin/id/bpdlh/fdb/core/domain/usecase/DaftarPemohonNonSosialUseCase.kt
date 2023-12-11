package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.domain.common.DaftarPemohonNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonNonSosialEntity

class DaftarPemohonNonSosialUseCase: DaftarPemohonNonSosialUseCaseContract {

    override fun fetchDataPemohon(param: HashMap<String, String>, type: Int): ResultState<List<DaftarPemohonNonSosialEntity>> {

        val datas = when(type) {
            FdbBadge.DRAFT -> {
                listOf(
                    DaftarPemohonNonSosialEntity("1", "10 Feb 2023", FdbBadge.DRAFT, 10),
                    DaftarPemohonNonSosialEntity("2","10 Feb 2023", FdbBadge.DRAFT, 10),
                    DaftarPemohonNonSosialEntity("3","10 Feb 2023", FdbBadge.DRAFT, 10)
                )
            }
            FdbBadge.ON_PROGRESS -> {
                listOf(
                    DaftarPemohonNonSosialEntity("4","10 Feb 2023", FdbBadge.ON_PROGRESS, 10),
                )
            }
            FdbBadge.ON_VERIFY -> {
                listOf(
                    DaftarPemohonNonSosialEntity("5","10 Feb 2023", FdbBadge.ON_VERIFY, 10),
                )
            }
            else -> emptyList()
        }
        return ResultState.Success(data = datas)
    }
}