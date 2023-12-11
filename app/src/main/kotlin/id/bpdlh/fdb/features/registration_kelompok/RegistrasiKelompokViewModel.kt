package id.bpdlh.fdb.features.registration_kelompok

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.core.domain.common.AlamatUseCaseContract
import id.bpdlh.fdb.core.domain.common.RegistrasiKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.entities.RegistrasiKelompokEntity
import javax.inject.Inject

/**
 * Created by hahn on 05/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokViewModel @Inject constructor(
    private val registrasiKelompokUseCase: RegistrasiKelompokUseCaseContract,
    private val alamatUseCase: AlamatUseCaseContract) :
    BaseViewModel() {

    private val _registrasiKelompok = MutableLiveData<RegistrasiKelompokPost>()
    val submitResult = MutableLiveData<ResultState<RegistrasiKelompokEntity>>()

    fun getRegistrasiKelompok(): MutableLiveData<RegistrasiKelompokPost> = _registrasiKelompok

    fun saveData(registrasiKelompokPost: RegistrasiKelompokPost) {
        _registrasiKelompok.value = registrasiKelompokPost
    }

    fun submitData() {
        _registrasiKelompok.value?.let {
            val disposable = registrasiKelompokUseCase.submitData(it)
                .doOnSubscribe {
                    submitResult.value = ResultState.Loading()
                }
                .subscribe { resultState ->
                    submitResult.value = ResultState.HideLoading()
                    submitResult.value = resultState
                }
            addDisposable(disposable)
        }
    }

    //address
    private val _provinceListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _regencyListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _districtListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _villageListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _zipCodeListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()

    fun getProvince() = _provinceListResult
    fun getRegency() = _regencyListResult
    fun getDistrict() = _districtListResult
    fun getVillage() = _villageListResult
    fun getZipCode() = _zipCodeListResult

    fun getRegencyIdFromProvince(provinceName: String): String {
        return _provinceListResult.value?.data?.find { it.name == provinceName }?.id.orEmpty()
    }

    fun getDistrictIdFromRegency(regencyName: String): String {
        return _regencyListResult.value?.data?.find { it.name == regencyName }?.id.orEmpty()
    }

    fun getVillageIdFromDistrict(districtName: String): String {
        return _districtListResult.value?.data?.find { it.name == districtName }?.id.orEmpty()
    }

    fun fetchProvince() {
        addDisposable(
            alamatUseCase.fetchProvince()
                .doOnSubscribe {
                    _provinceListResult.value = ResultState.Loading()
                }.subscribe { resultState ->
                    _provinceListResult.value = ResultState.HideLoading()
                    _provinceListResult.value = resultState
                }
        )
    }

    fun fetchCity(province: String) {
        addDisposable(
            alamatUseCase.fetchRegency(province)
                .doOnSubscribe {
                    _regencyListResult.value = ResultState.Loading()
                }.subscribe { resultState ->
                    _regencyListResult.value = ResultState.HideLoading()
                    _regencyListResult.value = resultState
                }
        )
    }

    fun fetchDistrict(regency: String) {
        addDisposable(
            alamatUseCase.fetchDistrict(regency)
                .doOnSubscribe {
                    _districtListResult.value = ResultState.Loading()
                }.subscribe { resultState ->
                    _districtListResult.value = ResultState.HideLoading()
                    _districtListResult.value = resultState
                }
        )
    }

    fun fetchVillage(district: String) {
        addDisposable(
            alamatUseCase.fetchVillage(district)
                .doOnSubscribe {
                    _villageListResult.value = ResultState.Loading()
                }.subscribe { resultState ->
                    _villageListResult.value = ResultState.HideLoading()
                    _villageListResult.value = resultState
                }
        )
    }
}