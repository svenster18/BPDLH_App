package id.bpdlh.fdb.features.profile.edit_account

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.AlamatUseCaseContract
import id.bpdlh.fdb.core.domain.common.EditAkunUseCaseContract
import id.bpdlh.fdb.core.domain.common.FormulirPendaftaranKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import javax.inject.Inject

class EditAccountViewModel @Inject constructor(
    private val editAkunUseCase: EditAkunUseCaseContract,
    private val alamatUseCase: AlamatUseCaseContract,
    private val groupUseCase: FormulirPendaftaranKelompokUseCaseContract
) : BaseViewModel() {

    val _updateGroupDataResult = MutableLiveData<ResultState<GroupProfileEntity>>()

    fun getUpdateGroupDataResult() = _updateGroupDataResult

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

    fun fetchProvince(provinceName: String?) {
        addDisposable(
            alamatUseCase.fetchProvince()
                .doOnSubscribe {
                    _provinceListResult.value = ResultState.Loading()
                }.subscribe { resultState ->
                    _provinceListResult.value = ResultState.HideLoading()
                    _provinceListResult.value = resultState
                    if (!provinceName.isNullOrEmpty()) {
                        fetchCity(getRegencyIdFromProvince(provinceName))
                    }
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

    fun updateGroupData(data: GroupProfileEntity) {
        //TODO: API nya belum jelas pakai yang mana.
//        addDisposable(
//            groupUseCase.submitData(data.userEntity?.userId.orEmpty(), data = data)
//                .doOnSubscribe {
//                    _updateGroupDataResult.value = ResultState.Loading()
//                }.subscribe { result ->
//                    _updateGroupDataResult.value = ResultState.HideLoading()
//                    _updateGroupDataResult.value = result
//                }
//        )
    }
}