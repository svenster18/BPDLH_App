package id.bpdlh.fdb.features.pengajuan_daftar_permohonan

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.domain.common.DaftarPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import javax.inject.Inject

/**
 * Created by hahn on 17/09/23.
 * Project: BPDLH App
 */
class PengajuanDaftarPermohonanViewModel  @Inject constructor(
    private val useCase: DaftarPemohonUseCaseContract
): BaseViewModel() {

    private val _groupApplicationResult = MutableLiveData<ResultState<List<DaftarPemohonEntity>>>()
    private val _resultList = MutableLiveData<List<DaftarPemohonEntity>>()

    fun getGroupApplication() = _groupApplicationResult
    fun getGroupApplicationList() = _resultList

    fun fetchGroupApplication(userId: String) {
        addDisposable(
            useCase.fetchGroupApplication(userId)
                .doOnSubscribe {
                    _groupApplicationResult.value = ResultState.Loading()
                }
                .subscribe { result ->
                    _groupApplicationResult.value = ResultState.HideLoading()
                    _groupApplicationResult.value = result
                    result.data?.let { _resultList.value = it }
                }
        )
    }

    fun getDataTypeByPosition(position: Int): Int {
        return when(position) {
            0 -> FdbBadge.DRAFT
            1 -> FdbBadge.ON_PROGRESS
            2 -> FdbBadge.ON_VERIFY
            3 -> FdbBadge.SUCCESS
            else -> FdbBadge.DRAFT
        }
    }
}