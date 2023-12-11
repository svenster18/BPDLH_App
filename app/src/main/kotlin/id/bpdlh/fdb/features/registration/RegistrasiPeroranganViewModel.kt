package id.bpdlh.fdb.features.registration

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.common.PembiayaanPerhutananUseCaseContract
import id.bpdlh.fdb.core.domain.common.RegistrasiPeroranganUseCaseContract
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment.Companion.dokumenLegalitasList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class RegistrasiPeroranganViewModel @Inject constructor(
    private val registrasiPeroranganUseCase: RegistrasiPeroranganUseCaseContract,
    private val pembiayaanPerhutananUseCase: PembiayaanPerhutananUseCaseContract
) :
    BaseViewModel() {

    val resultDraft = MutableLiveData<RegistrasiPerorangan?>()
    val resultMemberApplication = MutableLiveData<ResultState<MemberApplicationDataEntity>>()
    val resultMemberApplicationDraft = MutableLiveData<ResultState<MemberApplicationDataEntity>>()
    val updatePermohonanPembiayaanResult =
        MutableLiveData<ResultState<MemberApplicationEntity>>()
    val savePermohonanPembiayaanDraftResult =
        MutableLiveData<ResultState<MemberApplicationEntity>>()
    val ktpFileUploadResult = MutableLiveData<ResultState<String>>()
    val kkFileUploadResult = MutableLiveData<ResultState<String>>()
    val coupleKtpFileUploadResult = MutableLiveData<ResultState<String>>()
    val swaphotoKtpFileUploadResult = MutableLiveData<ResultState<String>>()
    val housePhotoFileUploadResult = MutableLiveData<ResultState<String>>()
    val businessPhotoFileUploadResult = MutableLiveData<ResultState<String>>()

    fun getDraft() {
        val disposable = registrasiPeroranganUseCase.fetchDraft()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { resultDraft.value = null }
            .doOnSuccess { resultDraft.value = it }
            .subscribe()

        addDisposable(disposable)
    }

    fun insert(registrasiPerorangan: RegistrasiPerorangan) {
        registrasiPeroranganUseCase.insertDraft(registrasiPerorangan)
    }

    fun update(registrasiPerorangan: RegistrasiPerorangan) {
        registrasiPeroranganUseCase.updateDraft(registrasiPerorangan)
    }

    fun getMemberApplication(userId: String?) {
        userId?.let {
            addDisposable(
                registrasiPeroranganUseCase.getMemberApplication(userId)
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

    fun saveFormulirPembiayaanNonPerhutananSosialDraft(
        userId: String,
        registrasiPerorangan: RegistrasiPerorangan
    ) {
        registrasiPerorangan.otherBusiness = BaseRegistrationFragment.otherBusinessList
        val disposable = registrasiPeroranganUseCase.saveFormulirPembiayaanNonPerhutananSosialDraft(
            userId, registrasiPerorangan
        )
            .doOnSubscribe {
                savePermohonanPembiayaanDraftResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                savePermohonanPembiayaanDraftResult.value =
                    ResultState.HideLoading()
                savePermohonanPembiayaanDraftResult.value = resultState
            }
        addDisposable(disposable)
    }

    fun submitFormulirPembiayaanNonPerhutananSosial(
        userId: String,
        registrasiPerorangan: RegistrasiPerorangan
    ) {
        registrasiPerorangan.otherBusiness = BaseRegistrationFragment.otherBusinessList
        val disposable = registrasiPeroranganUseCase.submitFormulirPembiayaanNonPerhutananSosial(
            userId, registrasiPerorangan
        )
            .doOnSubscribe {
                updatePermohonanPembiayaanResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                updatePermohonanPembiayaanResult.value =
                    ResultState.HideLoading()
                updatePermohonanPembiayaanResult.value = resultState
            }
        addDisposable(disposable)
    }

    fun insertFile(
        filePath: String,
        nama: String = "",
        type: Int = Constants.DATA_JAMINAN,
        userId: String
    ) {
        dokumenLegalitasList.add(
            FileEntity(
                nama = nama,
                path = filePath,
                parent_id = 0,
                type = type
            )
        )
        uploadFile(File(filePath), Constants.REQUEST_CODE_DOKUMEN_LAINNYA)
    }

    fun updateFile(
        filePath: String,
        position: Int,
        nama: String = ""
    ) {
        dokumenLegalitasList[position].path = filePath
        dokumenLegalitasList[position].nama = nama
    }

    fun removeFile(position: Int) {
        dokumenLegalitasList.removeAt(position)
    }

    fun uploadFile(file: File, requestCode: Int, position: Int = -1) {
        val fileUploadResult = when (requestCode) {
            Constants.REQUEST_CODE_KTP -> ktpFileUploadResult
            Constants.REQUEST_CODE_KK -> kkFileUploadResult
            Constants.REQUEST_CODE_KTP_PASANGAN -> coupleKtpFileUploadResult
            Constants.REQUEST_CODE_SWAFOTO -> swaphotoKtpFileUploadResult
            Constants.REQUEST_CODE_FOTO_RUMAH -> housePhotoFileUploadResult
            Constants.REQUEST_CODE_FOTO_USAHA -> businessPhotoFileUploadResult
            else -> businessPhotoFileUploadResult
        }
        val disposable = pembiayaanPerhutananUseCase.uploadFile(
            file
        )
            .doOnSubscribe {
                fileUploadResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                fileUploadResult.value = ResultState.HideLoading()
                resultState.data?.let {
                    when (requestCode) {
                        Constants.REQUEST_CODE_KTP -> BaseRegistrationFragment.registrasiPerorangan.ktpFile =
                            it

                        Constants.REQUEST_CODE_KK -> BaseRegistrationFragment.registrasiPerorangan.kkFile =
                            it

                        Constants.REQUEST_CODE_KTP_PASANGAN -> BaseRegistrationFragment.registrasiPerorangan.coupleKtpFile =
                            it

                        Constants.REQUEST_CODE_SWAFOTO -> BaseRegistrationFragment.registrasiPerorangan.swaphotoKtpFile =
                            it

                        Constants.REQUEST_CODE_FOTO_RUMAH -> BaseRegistrationFragment.registrasiPerorangan.housePhotoFile =
                            it

                        Constants.REQUEST_CODE_FOTO_USAHA -> BaseRegistrationFragment.registrasiPerorangan.businessPhotoFile =
                            it
                    }
                }
                fileUploadResult.value = resultState
            }
        addDisposable(disposable)
    }

    fun delayCuttingDraft(userId: String?, memberApplicationPost: MemberApplicationPost) {
        userId?.let {
            addDisposable(
                registrasiPeroranganUseCase.delayCuttingDraft(userId, memberApplicationPost)
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

    fun delayCuttingUpdate(userId: String, memberApplicationPost: MemberApplicationPost) {
        registrasiPeroranganUseCase.delayCuttingUpdate(userId, memberApplicationPost)
    }

    fun nonForestyComodityDraft(userId: String, memberApplicationPost: MemberApplicationPost) {
        registrasiPeroranganUseCase.nonForestyComodityDraft(userId, memberApplicationPost)
    }

    fun nonForestyComodityUpdate(userId: String, memberApplicationPost: MemberApplicationPost) {
        registrasiPeroranganUseCase.nonForestyComodityUpdate(userId, memberApplicationPost)
    }

    fun nonWoodForestProductDraft(userId: String, memberApplicationPost: MemberApplicationPost) {
        registrasiPeroranganUseCase.nonWoodForestProductDraft(userId, memberApplicationPost)
    }

    fun nonWoodForestProductUpdate(userId: String, memberApplicationPost: MemberApplicationPost) {
        registrasiPeroranganUseCase.nonWoodForestProductUpdate(userId, memberApplicationPost)
    }
}