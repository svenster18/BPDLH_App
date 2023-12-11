package id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.DaftarAnggotaPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by hahn on 23/09/23.
 * Project: BPDLH App
 */
class DaftarAnggotaPemohonViewModel @Inject constructor(
    private val daftarAnggotaPemohonUseCase: DaftarAnggotaPemohonUseCaseContract
): BaseViewModel() {

    private val _dataDebitur: MutableLiveData<List<DataDebiturEntity>> =
        daftarAnggotaPemohonUseCase.fetchDataFromDb()
            .asLiveData() as MutableLiveData<List<DataDebiturEntity>>
    private val _createDataResult = MutableLiveData<ResultState<List<DataDebiturEntity>>>()
    private val _submitDebiturResult = MutableLiveData<ResultState<DaftarPemohonEntity>>()
    val daftarAnggotaNonSosial = MutableLiveData<ResultState<List<DataDebiturNonSosialEntity>>>()

    fun getDataDebitur() = _dataDebitur
    fun getCreateDataResult() = _createDataResult

    fun getSubmitDebiturResult() = _submitDebiturResult

    fun getDataDebiturValue() = _dataDebitur.value

    fun setDataDebitur(data: DataDebiturEntity?) {
        data?.let {
            val tempList = mutableListOf<DataDebiturEntity>()
            _dataDebitur.value?.let { it1 -> tempList.addAll(it1) }
            tempList.add(data)
            _dataDebitur.value = tempList
            viewModelScope.launch {
                daftarAnggotaPemohonUseCase.insertDataToDb(it)
            }
        }
    }

    fun updateDataDebitur(data: DataDebiturEntity?) {
        data?.let { entity ->
            val tempList = mutableListOf<DataDebiturEntity>()
            _dataDebitur.value?.let { list -> tempList.addAll(list) }
            val index = tempList.indexOf(tempList.find { it.id == entity.id })
            if (index >= 0) tempList[index] = entity
            _dataDebitur.value = tempList
            viewModelScope.launch {
                daftarAnggotaPemohonUseCase.updateDataToDb(entity)
            }
        }
    }

    fun deleteDebitur(data: DataDebiturEntity) {
        val tempList = mutableListOf<DataDebiturEntity>()
        _dataDebitur.value?.let { debitur -> tempList.addAll(debitur) }
        tempList.remove(data)
        _dataDebitur.value = tempList
        viewModelScope.launch {
            daftarAnggotaPemohonUseCase.deleteData(data)
        }
    }

    fun deleteAllData() {
        //data akan didelete setelah submit berhasil
        viewModelScope.launch {
            daftarAnggotaPemohonUseCase.deleteAllData()
        }
    }

    fun createDataDebitur(userId: String) {
        _dataDebitur.value?.let { data ->
            addDisposable(
                daftarAnggotaPemohonUseCase.createDataDebitur(data, userId)
                    .doOnSubscribe { _createDataResult.value = ResultState.Loading() }
                    .subscribe { result ->
                        _createDataResult.value = ResultState.HideLoading()
                        _createDataResult.value = result
                    }
            )
        }
    }

    fun submitData(userId: String) {
        addDisposable(
            daftarAnggotaPemohonUseCase.submitDataDebitur(userId)
                .doOnSubscribe { _submitDebiturResult.value = ResultState.Loading() }
                .subscribe { result ->
                    _submitDebiturResult.value = ResultState.HideLoading()
                    _submitDebiturResult.value = result
                }
        )
    }

    fun fetchDaftarAnggotaPemohonNonSosial() {
        daftarAnggotaNonSosial.value =
            daftarAnggotaPemohonUseCase.fetchDaftarAnggotaPemohonNonSosial()
    }
}