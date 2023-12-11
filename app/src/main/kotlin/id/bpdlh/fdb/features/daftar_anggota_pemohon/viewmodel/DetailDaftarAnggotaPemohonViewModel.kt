package id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.DetailDaftarAnggotaPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarAnggotaPemohonNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import javax.inject.Inject

/**
 * Created by hahn on 26/09/23.
 * Project: BPDLH App
 */
class DetailDaftarAnggotaPemohonViewModel @Inject constructor(
    private val detailDaftarAnggotaPemohonUseCase: DetailDaftarAnggotaPemohonUseCaseContract
): BaseViewModel() {
    val detailDaftarAnggotaPemohonNonSosial = MutableLiveData<ResultState<DaftarAnggotaPemohonNonSosialEntity>>()
    private val _listDaftarAnggota = MutableLiveData<ResultState<List<DataDebiturEntity>>>()

    fun fetchDetailAnggota(memberApplicationId: String) {
        addDisposable(
            detailDaftarAnggotaPemohonUseCase.fetchDetailDaftarAnggota(memberApplicationId)
                .doOnSubscribe {
                    _listDaftarAnggota.value = ResultState.Loading()
                }.subscribe { result ->
                    _listDaftarAnggota.value = ResultState.HideLoading()
                    _listDaftarAnggota.value = result
                }
        )
    }

    fun getListDaftarAnggota() = _listDaftarAnggota

    fun fetchDetailDaftarAnggotaPemohonNonSosial() {
        detailDaftarAnggotaPemohonNonSosial.value = detailDaftarAnggotaPemohonUseCase.fetchDetailDaftarAnggotaPemohonNonSosial()
    }
}