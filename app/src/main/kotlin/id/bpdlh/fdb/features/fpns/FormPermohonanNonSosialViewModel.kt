package id.bpdlh.fdb.features.fpns

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.domain.common.FormPermohonanNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MitraEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import java.io.File
import javax.inject.Inject

class FormPermohonanNonSosialViewModel @Inject constructor(
    private val formPermohonanNonSosialUseCase: FormPermohonanNonSosialUseCaseContract,
) :
    BaseViewModel() {

    var memberApplicationPost: MemberApplicationPost = MemberApplicationPost()
    val resultMemberApplication = MutableLiveData<ResultState<MemberApplicationDataEntity>>()
    val resultMemberApplicationUpdate = MutableLiveData<ResultState<MemberApplicationDataEntity>>()
    val resultMemberApplicationDraft = MutableLiveData<ResultState<MemberApplicationDataEntity>>()
    val submitOtherDocumentResult = MutableLiveData<ResultState<OtherDocumentEntity>>()
    val resultDocumentOther = MutableLiveData<ResultState<List<OtherDocumentEntity>>>()
    val resultDocumentOtherDelete = MutableLiveData<ResultState<Any>>()
    val resultBusinessPartner = MutableLiveData<ResultState<List<MitraEntity>>>()
    val mitraList = arrayListOf<Mitra>()
    val ijinLahanFileUploadResult = MutableLiveData<ResultState<String>>()
    val suratTanahFileUploadResult = MutableLiveData<ResultState<String>>()
    val suratJualBeliFileUploadResult = MutableLiveData<ResultState<String>>()
    val spptFileUploadResult = MutableLiveData<ResultState<String>>()
    val fotoLahanFileUploadResult = MutableLiveData<ResultState<String>>()
    val labaRugiFileUploadResult = MutableLiveData<ResultState<String>>()
    val dokumenLainnyaUploadFileResult = MutableLiveData<ResultState<String>>()
    val ijinLahanFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val suratTanahFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val suratJualBeliFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val spptFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val fotoLahanFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val labaRugiFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val dokumenLainnyaFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()

    fun getMemberApplication(userId: String?) {
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.getMemberApplication(userId)
                    .doOnSubscribe {
                        resultMemberApplication.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplication.value = ResultState.HideLoading()
                        resultMemberApplication.value = resultState
                    }
            )
        }
    }

    fun saveDraft(userId: String?, memberApplicationPost: MemberApplicationPost) {
        when(memberApplicationPost.serviceType) {
            Constants.TUNDA_TEBANG -> delayCuttingDraft(userId, memberApplicationPost)
            Constants.HASIL_HUTAN_BUKAN_KAYU -> nonWoodForestProductDraft(userId, memberApplicationPost)
            Constants.KOMODITAS_NON_KEHUTANAN -> nonForestyComodityDraft(userId, memberApplicationPost)
        }
    }

    fun saveUpdate(userId: String?, memberApplicationPost: MemberApplicationPost) {
        when(memberApplicationPost.serviceType) {
            Constants.TUNDA_TEBANG -> delayCuttingUpdate(userId, memberApplicationPost)
            Constants.HASIL_HUTAN_BUKAN_KAYU -> nonWoodForestProductUpdate(userId, memberApplicationPost)
            Constants.KOMODITAS_NON_KEHUTANAN -> nonForestyComodityUpdate(userId, memberApplicationPost)
        }
    }

    fun delayCuttingDraft(userId: String?, memberApplicationPost: MemberApplicationPost) {
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.delayCuttingDraft(userId, memberApplicationPost)
                    .doOnSubscribe {
                        resultMemberApplicationDraft.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplicationDraft.value = ResultState.HideLoading()
                        resultMemberApplicationDraft.value = resultState
                    }
            )
        }
    }

    fun delayCuttingUpdate(userId: String?, memberApplicationPost: MemberApplicationPost){
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.delayCuttingUpdate(userId, memberApplicationPost)
                    .doOnSubscribe {
                        resultMemberApplicationUpdate.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplicationUpdate.value = ResultState.HideLoading()
                        resultMemberApplicationUpdate.value = resultState
                    }
            )
        }
    }

    fun nonForestyComodityDraft(userId: String?, memberApplicationPost: MemberApplicationPost){
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.nonForestyComodityDraft(userId, memberApplicationPost)
                    .doOnSubscribe {
                        resultMemberApplicationDraft.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplicationDraft.value = ResultState.HideLoading()
                        resultMemberApplicationDraft.value = resultState
                    }
            )
        }
    }
    fun nonForestyComodityUpdate(userId: String?, memberApplicationPost: MemberApplicationPost){
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.nonForestyComodityUpdate(userId, memberApplicationPost)
                    .doOnSubscribe {
                        resultMemberApplicationUpdate.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplicationUpdate.value = ResultState.HideLoading()
                        resultMemberApplicationUpdate.value = resultState
                    }
            )
        }
    }
    fun nonWoodForestProductDraft(userId: String?, memberApplicationPost: MemberApplicationPost){
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.nonWoodForestProductDraft(userId, memberApplicationPost)
                    .doOnSubscribe {
                        resultMemberApplicationDraft.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplicationDraft.value = ResultState.HideLoading()
                        resultMemberApplicationDraft.value = resultState
                    }
            )
        }
    }
    fun nonWoodForestProductUpdate(userId: String?, memberApplicationPost: MemberApplicationPost){
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.nonWoodForestProductUpdate(userId, memberApplicationPost)
                    .doOnSubscribe {
                        resultMemberApplicationUpdate.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        resultMemberApplicationUpdate.value = ResultState.HideLoading()
                        resultMemberApplicationUpdate.value = resultState
                    }
            )
        }
    }

    fun getDocumentDraft(userId: String?) {
        userId?.let {
            addDisposable(
                formPermohonanNonSosialUseCase.getOtherDocumentDraft(userId)
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

    fun postDocument(
        otherDocumentPost: OtherDocumentPost
    ) {
        val disposable = formPermohonanNonSosialUseCase.postOtherDocumentDraft(otherDocumentPost)
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
        val disposable = formPermohonanNonSosialUseCase.deleteOtherDocument(id)
            .doOnSubscribe {
                resultDocumentOtherDelete.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                resultDocumentOtherDelete.value = ResultState.HideLoading()
                resultDocumentOtherDelete.value = resultState
            }
        addDisposable(disposable)
    }

    fun getBusinessPartner(id: String) {
        val disposable = formPermohonanNonSosialUseCase.getBusinessPartner(id)
            .doOnSubscribe {
                resultBusinessPartner.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                resultBusinessPartner.value = ResultState.HideLoading()
                resultBusinessPartner.value = resultState
            }
        addDisposable(disposable)
    }
    fun insertMitra(nama: String) {
        mitraList.add(
            Mitra(
                nama = nama,
            )
        )
    }

    fun updateMitra(nama: String, position: Int) {
        mitraList[position].nama = nama
    }

    fun removeMitra(position: Int) {
        mitraList.removeAt(position)
    }
    
    fun uploadFile(file: File, requestCode: Int) {
        val fileUploadResult = when (requestCode) {
            Constants.REQUEST_CODE_IJIN_LAHAN -> ijinLahanFileUploadResult
            Constants.REQUEST_CODE_SURAT_TANAH -> suratTanahFileUploadResult
            Constants.REQUEST_CODE_SURAT_JUAL_BELI -> suratJualBeliFileUploadResult
            Constants.REQUEST_CODE_SPPT -> spptFileUploadResult
            Constants.REQUEST_CODE_LAPORAN_LABA_RUGI -> labaRugiFileUploadResult
            Constants.REQUEST_CODE_FOTO_LAHAN -> fotoLahanFileUploadResult
            else -> dokumenLainnyaUploadFileResult
        }
        val disposable = formPermohonanNonSosialUseCase.uploadFile(
            file
        )
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
            Constants.REQUEST_CODE_IJIN_LAHAN -> ijinLahanFileResult
            Constants.REQUEST_CODE_SURAT_TANAH -> suratTanahFileResult
            Constants.REQUEST_CODE_SURAT_JUAL_BELI -> suratJualBeliFileResult
            Constants.REQUEST_CODE_SPPT -> spptFileResult
            Constants.REQUEST_CODE_FOTO_LAHAN -> fotoLahanFileResult
            Constants.REQUEST_CODE_LAPORAN_LABA_RUGI -> labaRugiFileResult
            else -> dokumenLainnyaFileResult
        }
        val disposable = formPermohonanNonSosialUseCase.getSingleFile(
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