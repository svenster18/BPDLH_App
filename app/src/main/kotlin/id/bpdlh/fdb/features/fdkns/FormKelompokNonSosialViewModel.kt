package id.bpdlh.fdb.features.fdkns

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import id.bpdlh.fdb.core.domain.common.AlamatUseCaseContract
import id.bpdlh.fdb.core.domain.common.FormKelompokNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class FormKelompokNonSosialViewModel @Inject constructor(
    private val formKelompokNonSosialUseCase: FormKelompokNonSosialUseCaseContract,
    private val alamatUseCase: AlamatUseCaseContract
) : BaseViewModel() {

    val resultPostDraft = MutableLiveData<ResultState<GroupProfileNonSosial>>()
    val resultPostUpdate = MutableLiveData<ResultState<GroupProfileNonSosial>>()
    val resultPostSubmit = MutableLiveData<ResultState<GroupProfileNonSosial>>()
    val submitOtherDocumentResult = MutableLiveData<ResultState<OtherDocumentEntity>>()
    val resulDraft = MutableLiveData<GroupProfileNonSosial?>()
    val resultDocumentOther = MutableLiveData<ResultState<List<OtherDocumentEntity>>>()
    val resultDocumentOtherDelete = MutableLiveData<ResultState<Any>>()
    val skBeritaAcaraFileUploadResult = MutableLiveData<ResultState<String>>()
    val adArtFileUploadResult = MutableLiveData<ResultState<String>>()
    val suratPendampingFileUploadResult = MutableLiveData<ResultState<String>>()
    val dokumenLainnyaUploadFileResult = MutableLiveData<ResultState<String>>()
    val skBeritaAcaraFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val adArtFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val suratPendampingFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()

    private val _provinceListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _regencyListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _districtListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _villageListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()
    private val _zipCodeListResult = MutableLiveData<ResultState<List<DataAlamatEntity>>>()



    fun getDraft() {
        val disposable = formKelompokNonSosialUseCase.fetchDraft()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { resulDraft.value = null }
            .doOnSuccess { resulDraft.value = it }
            .subscribe()
        addDisposable(disposable)
    }

    fun insert(formKelompokNonSosial: GroupProfileNonSosial) {
        formKelompokNonSosialUseCase.insertDraft(formKelompokNonSosial)
    }

    fun update(formKelompokNonSosial: GroupProfileNonSosial) {
        formKelompokNonSosialUseCase.updateDraft(formKelompokNonSosial)
    }

    fun draft(body: FormulirKelompokNonSosialPost, userId: String?) {
        userId?.let {
            addDisposable(
                formKelompokNonSosialUseCase.draft(userId, body)
                    .doOnSubscribe {
                        resultPostDraft.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultPostDraft.value = ResultState.HideLoading()
                        resultPostDraft.value = resultState
                    }
            )
        }
    }

    fun update(body: FormulirKelompokNonSosialPost, userId: String?) {
        userId?.let {
            addDisposable(
                formKelompokNonSosialUseCase.update(userId, body)
                    .doOnSubscribe {
                        resultPostUpdate.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultPostUpdate.value = ResultState.HideLoading()
                        resultPostUpdate.value = resultState
                    }
            )
        }
    }

    fun submit(body: FormulirKelompokNonSosialPost, userId: String?) {
        userId?.let {
            addDisposable(
                formKelompokNonSosialUseCase.submit(userId, body)
                    .doOnSubscribe {
                        resultPostSubmit.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultPostSubmit.value = ResultState.HideLoading()
                        resultPostSubmit.value = resultState
                    }
            )
        };
    }

    fun deleteDraft() {
        formKelompokNonSosialUseCase.deleteDraft()
    }


    fun getDocumentDraft(userId: String?) {
        userId?.let {
            addDisposable(
                formKelompokNonSosialUseCase.getOtherDocumentDraft(userId)
                    .doOnSubscribe {
                        resultDocumentOther.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultDocumentOther.value = ResultState.HideLoading()
                        resultDocumentOther.value = resultState
                    }
            )
        }
    }

    fun insertFile(
        filePath: String,
        nama: String = "",
        userId: String
    ) {
        val disposable = formKelompokNonSosialUseCase.postOtherDocumentDraft(
            OtherDocumentPost(
                userId,
                nama,
                file = filePath,
                description = nama
            )
        )
            .doOnSubscribe {
                submitOtherDocumentResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                submitOtherDocumentResult.value = ResultState.HideLoading()
                submitOtherDocumentResult.value = resultState
            }
        addDisposable(disposable)
    }
    fun deleteOtherDocument(id: String) {
        val disposable = formKelompokNonSosialUseCase.deleteOtherDocument(id)
            .doOnSubscribe {
                resultDocumentOtherDelete.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                resultDocumentOtherDelete.value = ResultState.HideLoading()
                resultDocumentOtherDelete.value = resultState
            }
        addDisposable(disposable)
    }

    fun setDocumentOther(
        otherDocumentEntity: List<OtherDocumentEntity>
    ) {
        otherDocumentEntity.map {

        }
    }

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

    fun uploadFile(file: File, requestCode: Int) {
        val fileUploadResult = when (requestCode) {
            Constants.REQUEST_CODE_DOKUMEN_SK_BERITA_ACARA -> skBeritaAcaraFileUploadResult
            Constants.REQUEST_CODE_DOKUMEN_AD_ART -> adArtFileUploadResult
            Constants.REQUEST_CODE_DOKUMEN_SURAT_PENDAMPING -> suratPendampingFileUploadResult
            else -> dokumenLainnyaUploadFileResult
        }
        val disposable = formKelompokNonSosialUseCase.uploadFile(file)
            .doOnSubscribe {
                fileUploadResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                fileUploadResult.value = ResultState.HideLoading()
                fileUploadResult.value = resultState
            }
        addDisposable(disposable)
    }

    fun getSingleFile(id: String, requestCode: Int) {
        val fileResult = when (requestCode) {
            Constants.REQUEST_CODE_DOKUMEN_SK_BERITA_ACARA -> skBeritaAcaraFileResult
            Constants.REQUEST_CODE_DOKUMEN_AD_ART -> adArtFileResult
            Constants.REQUEST_CODE_DOKUMEN_SURAT_PENDAMPING -> suratPendampingFileResult
            else -> dokumenLainnyaUploadFileResult
        }
        val disposable = formKelompokNonSosialUseCase.getSingleFile(
            id
        )
            .doOnSubscribe {
                fileResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                fileResult.value = ResultState.HideLoading()
                fileResult.value = resultState
            }
        addDisposable(disposable)
    }

}