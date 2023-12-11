package id.bpdlh.fdb.features.fdk

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.AlamatUseCaseContract
import id.bpdlh.fdb.core.domain.common.FormulirPendaftaranKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.core.domain.entities.AfiliasiPendampingEntity
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.entities.FungsiKawasanEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.KategoriKegiatanEntity
import javax.inject.Inject

/**
 * Created by hahn on 08/09/23.
 * Project: BPDLH App
 */
class FormulirPendaftaranKelompokViewModel  @Inject constructor(
    private val useCase: FormulirPendaftaranKelompokUseCaseContract,
    private val alamatUseCase: AlamatUseCaseContract
): BaseViewModel() {

    private val _groupProfileEntity = MutableLiveData<GroupProfileEntity>()
    val saveDraftResult = MutableLiveData<ResultState<GroupProfileEntity>>()
    private val _listActivities = MutableLiveData<List<ActivityEntity>>()
    private val _listMitraUsaha = MutableLiveData<List<ActivityEntity>>()
    private val _listGambaranUmum = MutableLiveData<List<ActivityEntity>>()
    private val _listFungsiKawasan = MutableLiveData<ResultState<List<FungsiKawasanEntity>>>()
    private val _listAfiliasiPendamping = MutableLiveData<ResultState<List<AfiliasiPendampingEntity>>>()
    private val _listKategoriKegiatan = MutableLiveData<ResultState<List<KategoriKegiatanEntity>>>()
    private val _listKategoriUsahaDibiayai = MutableLiveData<ResultState<List<KategoriKegiatanEntity>>>()

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

    fun setGroupProfile(data: GroupProfileEntity?) {
        data?.let {
            _groupProfileEntity.value = it
            _listActivities.value = it.listActivityEntity.orEmpty()
            _listMitraUsaha.value = it.listPartnerName.orEmpty()
            _listGambaranUmum.value = it.listGeneralDescription.orEmpty()
        }
    }

    fun addActivity(activityEntity: ActivityEntity) {
        val tempActivities = mutableListOf<ActivityEntity>()
        _listActivities.value?.let { tempActivities.addAll(it) }
        tempActivities.add(activityEntity)
        _listActivities.value = tempActivities
    }

    fun updateActivity(id: String, activityEntity: ActivityEntity) {
        val tempActivities = mutableListOf<ActivityEntity>()
        _listActivities.value?.let { tempActivities.addAll(it) }
        val index = tempActivities.indexOf(tempActivities.find { it.id == id })
        tempActivities[index] = activityEntity
        _listActivities.value = tempActivities
    }

    fun addMitraUsaha(data: ActivityEntity) {
        val tempData = mutableListOf<ActivityEntity>()
        _listMitraUsaha.value?.let { tempData.addAll(it) }
        tempData.add(data)
        _listMitraUsaha.value = tempData
    }

    fun addGambaranUmum(gambaranUmum: ActivityEntity) {
        val tempGambaranUmum = mutableListOf<ActivityEntity>()
        _listGambaranUmum.value?.let { tempGambaranUmum.addAll(it) }
        tempGambaranUmum.add(gambaranUmum)
        _listGambaranUmum.value = tempGambaranUmum
    }

    fun updateMitraUsaha(id: String, activityEntity: ActivityEntity) {
        val tempActivities = mutableListOf<ActivityEntity>()
        _listMitraUsaha.value?.let { tempActivities.addAll(it) }
        val index = tempActivities.indexOf(tempActivities.find { it.id == id })
        tempActivities[index] = activityEntity
        _listMitraUsaha.value = tempActivities
    }

    fun updateGambaranUmum(id: String, activityEntity: ActivityEntity) {
        val tempActivities = mutableListOf<ActivityEntity>()
        _listGambaranUmum.value?.let { tempActivities.addAll(it) }
        val index = tempActivities.indexOf(tempActivities.find { it.id == id })
        tempActivities[index] = activityEntity
        _listGambaranUmum.value = tempActivities

    }

    fun getGroupProfileEntity() = _groupProfileEntity

    fun getActivities() = _listActivities

    fun getMitraUsaha() = _listMitraUsaha

    fun getGambaranUmum() = _listGambaranUmum

    fun getFungsiKawasan() = _listFungsiKawasan

    fun getAfiliasiPendamping() = _listAfiliasiPendamping

    fun getKategoriKegiatan() = _listKategoriKegiatan

    fun getActivitiesValue() = _listActivities.value
    fun getMitraUsahaValue() = _listMitraUsaha.value

    fun getGambaranUmumValue() = _listGambaranUmum.value
    fun getKategoriKegiatanValue() = _listKategoriKegiatan.value

    fun getKategoriUsahaDibiayaiValue() = _listKategoriUsahaDibiayai.value

    fun saveDraft(data: GroupProfileEntity) {
        addDisposable(
            useCase.saveToDraft(data.userEntity?.userId.orEmpty(), data = data)
                .doOnSubscribe {
                    saveDraftResult.value = ResultState.Loading()
                }.subscribe { result ->
                    saveDraftResult.value = ResultState.HideLoading()
                    saveDraftResult.value = result
                }
        )
    }

    fun submit(data: GroupProfileEntity) {
        addDisposable(
            useCase.submitData(data.userEntity?.userId.orEmpty(), data = data)
                .doOnSubscribe {
                    saveDraftResult.value = ResultState.Loading()
                }.subscribe { result ->
                    saveDraftResult.value = ResultState.HideLoading()
                    saveDraftResult.value = result
                }
        )
    }

    fun fetchFungsiKawasan() {
        addDisposable(
            useCase.fetchFungsiKawasan()
                .doOnSubscribe {
                    _listFungsiKawasan.value = ResultState.Loading()
                }.subscribe { result ->
                    _listFungsiKawasan.value = ResultState.HideLoading()
                    _listFungsiKawasan.value = result
                }
        )
    }

    fun fetchAfiliasiPendamping() {
        addDisposable(
            useCase.fetchAfiliasiPendamping()
                .doOnSubscribe {
                    _listAfiliasiPendamping.value = ResultState.Loading()
                }.subscribe { result ->
                    _listAfiliasiPendamping.value = ResultState.HideLoading()
                    _listAfiliasiPendamping.value = result
                }
        )
    }

    fun fetchKategoriKegiatan() {
        addDisposable(
            useCase.fetchKategoriKegiatan()
                .doOnSubscribe {
                    _listKategoriKegiatan.value = ResultState.Loading()
                }.subscribe { result ->
                    _listKategoriKegiatan.value = ResultState.HideLoading()
                    _listKategoriKegiatan.value = result
                }
        )
    }

    fun fetchKategoriUsahaDibiayai() {
        addDisposable(
            useCase.fetchKategoriUsahaDibiayai()
                .doOnSubscribe {
                    _listKategoriUsahaDibiayai.value = ResultState.Loading()
                }.subscribe { result ->
                    _listKategoriUsahaDibiayai.value = ResultState.HideLoading()
                    _listKategoriUsahaDibiayai.value = result
                }
        )
    }

    fun getRegencyIdFromProvince(provinceName: String): String {
        return _provinceListResult.value?.data?.find { it.name == provinceName }?.id.orEmpty()
    }

    fun getDistrictIdFromRegency(regencyName: String): String {
        return _regencyListResult.value?.data?.find { it.name == regencyName }?.id.orEmpty()
    }

    fun getVillageIdFromDistrict(districtName: String): String {
        return _districtListResult.value?.data?.find { it.name == districtName }?.id.orEmpty()
    }

    fun fetchProvince(provinceName: String? = null) {
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
}