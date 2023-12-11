package id.bpdlh.fdb.features.permohonan_pembiayaan

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_DATA_JAMINAN
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_DOKUMEN_LAINNYA
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_FOTO_LAHAN
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_IJIN_LAHAN
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_KK
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_KTP
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_KTP_PASANGAN
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_LAPORAN_LABA_RUGI
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_SPPT
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_SURAT_JUAL_BELI
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_SURAT_TANAH
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.post.PermohonanPembiayaanPerhutananSosialPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanNonPerhutananSosial
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.domain.common.PembiayaanPerhutananUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class FormulirPermohonanViewModel @Inject constructor(private val pembiayaanPerhutananUseCase: PembiayaanPerhutananUseCaseContract) :
    BaseViewModel() {
    val mitraList = arrayListOf<Mitra>()
    val jaminanList = arrayListOf<JaminanEntity>()
    var formulirPembiayaanPerhutananSosial = PermohonanPembiayaanPerhutananSosialPost(
        businessPartners = mitraList,
        guarantee = jaminanList
    )
    var formulirPembiayaanNonPerhutananSosial = FormulirPembiayaanNonPerhutananSosial(
        businessPartners = mitraList
    )
    val dataJaminanList = arrayListOf<FileEntity>()
    val dokumenLahanPathList = arrayListOf<FileEntity>()
    val formulirPembiayaanNonPerhutananSosialResult =
        MutableLiveData<FormulirPembiayaanNonPerhutananSosial?>()
    val mitraResult = MutableLiveData<List<Mitra>>()
    val dataJaminanPathResult = MutableLiveData<List<FileEntity>>()
    val dokumenLahanPathResult = MutableLiveData<List<FileEntity>>()
    val jaminanResult = MutableLiveData<List<JaminanEntity>>()
    val updatePermohonanPembiayaanResult =
        MutableLiveData<ResultState<MemberApplicationEntity>>()
    val submitPermohonanPembiayaanResult =
        MutableLiveData<ResultState<DaftarPemohonEntity>>()
    val savePermohonanPembiayaanDraftResult =
        MutableLiveData<ResultState<MemberApplicationEntity>>()
    val submitOtherDocumentResult = MutableLiveData<ResultState<OtherDocumentEntity>>()
    val ktpFileUploadResult = MutableLiveData<ResultState<String>>()
    val kkFileUploadResult = MutableLiveData<ResultState<String>>()
    val coupleKtpFileUploadResult = MutableLiveData<ResultState<String>>()
    val laporanLabaRugiFileUploadResult = MutableLiveData<ResultState<String>>()
    val dataJaminanFileUploadResult = MutableLiveData<ResultState<String>>()
    val ijinLahanFileUploadResult = MutableLiveData<ResultState<String>>()
    val suratTanahFileUploadResult = MutableLiveData<ResultState<String>>()
    val suratJualBeliFileResult = MutableLiveData<ResultState<String>>()
    val spptFileUploadResult = MutableLiveData<ResultState<String>>()
    val fotoLahanFileResult = MutableLiveData<ResultState<String>>()
    val dokumenLainnyaUploadFileResult = MutableLiveData<ResultState<String>>()
    val jaminanFileUploadResult = MutableLiveData<ResultState<String>>()
    val ktpFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val kkFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()
    val coupleKtpFileResult = MutableLiveData<ResultState<GetFileSourceApi>>()

    init {
        getFormulirPembiayaanNonPerhutananDraft()
        getMitra()
        getFile()
        getFile(Constants.DOKUMEN_PENGUASAAN_LAHAN)
        getJaminan()
    }

    private fun getFormulirPembiayaanNonPerhutananDraft() {
        val disposable =
            pembiayaanPerhutananUseCase.fetchFormulirPembiayaanNonPerhutananSosial(FdbBadge.DRAFT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { formulirPembiayaanNonPerhutananSosialResult.value = null }
                .doOnSuccess {
                    formulirPembiayaanNonPerhutananSosialResult.value = it
                    formulirPembiayaanNonPerhutananSosial = it
                }
                .subscribe()

        addDisposable(disposable)
    }

    fun submitPermohonanPembiayaan(memberApplicationId: String) {
        val disposable = pembiayaanPerhutananUseCase.submitPermohonanPembiayaan(
            memberApplicationId
        )
            .doOnSubscribe {
                submitPermohonanPembiayaanResult.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                submitPermohonanPembiayaanResult.value = ResultState.HideLoading()
                submitPermohonanPembiayaanResult.value = resultState
            }
        addDisposable(disposable)
    }

    fun saveFormulirPembiayaanPerhutananSosialDraft(userId: String) {
        formulirPembiayaanPerhutananSosial.guarantee = jaminanList
        formulirPembiayaanPerhutananSosial.businessPartners = mitraList
        val disposable = pembiayaanPerhutananUseCase.saveFormulirPembiayaanPerhutananSosialDraft(
            userId, formulirPembiayaanPerhutananSosial
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

    fun submitFormulirPembiayaanPerhutananSosial(userId: String) {
        formulirPembiayaanPerhutananSosial.guarantee = jaminanList
        formulirPembiayaanPerhutananSosial.businessPartners = mitraList
        val disposable = pembiayaanPerhutananUseCase.submitFormulirPembiayaanPerhutananSosial(
            userId, formulirPembiayaanPerhutananSosial
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

    fun saveFormulirPembiayaanNonPerhutananSosialDraft(userId: String) {
        val disposable = pembiayaanPerhutananUseCase.saveFormulirPembiayaanNonPerhutananSosialDraft(
            userId, formulirPembiayaanNonPerhutananSosial
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

    fun submitFormulirPembiayaanNonPerhutananSosial(userId: String) {
        val disposable = pembiayaanPerhutananUseCase.submitFormulirPembiayaanNonPerhutananSosial(
            userId, formulirPembiayaanNonPerhutananSosial
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

    fun uploadFile(file: File, requestCode: Int, position: Int = -1) {
        val fileUploadResult = when (requestCode) {
            REQUEST_CODE_KTP -> ktpFileUploadResult
            REQUEST_CODE_KK -> kkFileUploadResult
            REQUEST_CODE_KTP_PASANGAN -> coupleKtpFileUploadResult
            REQUEST_CODE_LAPORAN_LABA_RUGI -> laporanLabaRugiFileUploadResult
            REQUEST_CODE_DATA_JAMINAN -> dataJaminanFileUploadResult
            REQUEST_CODE_IJIN_LAHAN -> ijinLahanFileUploadResult
            REQUEST_CODE_SURAT_TANAH -> suratTanahFileUploadResult
            REQUEST_CODE_SURAT_JUAL_BELI -> suratJualBeliFileResult
            REQUEST_CODE_FOTO_LAHAN -> fotoLahanFileResult
            REQUEST_CODE_SPPT -> spptFileUploadResult
            else -> dokumenLainnyaUploadFileResult
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
                        REQUEST_CODE_KTP -> formulirPembiayaanPerhutananSosial.ktpFileId = it
                        REQUEST_CODE_KK -> formulirPembiayaanPerhutananSosial.kkFileId = it
                        REQUEST_CODE_KTP_PASANGAN -> formulirPembiayaanPerhutananSosial.coupleKtpFileId =
                            it

                        REQUEST_CODE_LAPORAN_LABA_RUGI -> formulirPembiayaanNonPerhutananSosial.laporan_laba_rugi_id =
                            it

                        REQUEST_CODE_DATA_JAMINAN -> formulirPembiayaanNonPerhutananSosial.data_jaminan_id =
                            it

                        REQUEST_CODE_IJIN_LAHAN -> formulirPembiayaanNonPerhutananSosial.ijin_lahan_id =
                            it

                        REQUEST_CODE_SURAT_TANAH -> formulirPembiayaanNonPerhutananSosial.surat_tanah_id =
                            it

                        REQUEST_CODE_SURAT_JUAL_BELI -> formulirPembiayaanNonPerhutananSosial.surat_jual_beli_id =
                            it

                        REQUEST_CODE_FOTO_LAHAN -> formulirPembiayaanNonPerhutananSosial.foto_lahan_id =
                            it

                        REQUEST_CODE_SPPT -> formulirPembiayaanNonPerhutananSosial.sppt_id = it
                    }
                }
                fileUploadResult.value = resultState
            }
        addDisposable(disposable)
    }

    fun getSingleFile(id: String, requestCode: Int) {
        val fileResult = when (requestCode) {
            REQUEST_CODE_KTP -> ktpFileResult
            REQUEST_CODE_KK -> kkFileResult
            else -> coupleKtpFileResult
        }
        val disposable = pembiayaanPerhutananUseCase.getSingleFile(
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

    fun insertFormulirPembiayaanNonPerhutananDraft() {
        formulirPembiayaanNonPerhutananSosial.status = FdbBadge.DRAFT
        pembiayaanPerhutananUseCase.insertFormulirPembiayaanNonPerhutananSosial(
            formulirPembiayaanNonPerhutananSosial
        )
    }

    fun updateFormulirPembiayaanNonPerhutananDraft() {
        pembiayaanPerhutananUseCase.updateFormulirPembiayaanNonPerhutananSosial(
            formulirPembiayaanNonPerhutananSosial
        )
    }

    fun fetchMitra(
        userId: String,
        type: Int,
    ) {
        val parameters = HashMap<String, String>()
        val disposable = pembiayaanPerhutananUseCase.fetchDataMitra(userId, parameters, type = type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resultState ->
                resultState.data?.let { mitraList.addAll(it.toList()) }
            }
        addDisposable(disposable)
    }

    private fun getMitra() {
        val disposable =
            pembiayaanPerhutananUseCase.fetchMitra(formulirPembiayaanNonPerhutananSosial.id + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mitraResult.value = it
                    mitraList.addAll(it)
                }
        addDisposable(disposable)
    }

    fun insertMitra(nama: String, category: String = Constants.TUNDA_TEBANG) {
        Timber.d("id : " + formulirPembiayaanNonPerhutananSosial.id.toString())
        if (formulirPembiayaanNonPerhutananSosial.id != 0) {
            mitraList.add(
                Mitra(
                    nama = nama,
                    formulir_id = formulirPembiayaanNonPerhutananSosial.id
                )
            )
        } else if (category == Constants.PERHUTANAN_SOSIAL) {
            mitraList.add(
                Mitra(
                    nama = nama,
                )
            )
        } else {
            mitraList.add(
                Mitra(
                    nama = nama,
                    formulir_id = 1
                )
            )
        }
    }

    fun insertAllMitra() {
        if (mitraList.isNotEmpty()) pembiayaanPerhutananUseCase.insertMitra(
            mitraList
        )
    }

    fun updateMitra(nama: String, position: Int) {
        mitraList[position].nama = nama
    }

    fun removeMitra(position: Int) {
        mitraList.removeAt(position)
    }

    private fun getFile(type: Int = Constants.DATA_JAMINAN) {
        val disposable =
            pembiayaanPerhutananUseCase.fetchFile(
                formulirPembiayaanNonPerhutananSosial.id + 1,
                type
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isNotEmpty()) {
                        if (it.first().type == Constants.DATA_JAMINAN) {
                            dataJaminanPathResult.value = it
                            dataJaminanList.addAll(it)
                        } else if (it.first().type == Constants.DOKUMEN_PENGUASAAN_LAHAN) {
                            dokumenLahanPathResult.value = it
                            dokumenLahanPathList.addAll(it)
                        }
                    }
                }
        addDisposable(disposable)
    }

    fun insertFile(
        filePath: String,
        nama: String = "",
        type: Int = Constants.DATA_JAMINAN,
        userId: String
    ) {
        if (type == Constants.DATA_JAMINAN) dataJaminanList.add(
            FileEntity(
                path = filePath,
                parent_id = if (formulirPembiayaanNonPerhutananSosial.id != 0) formulirPembiayaanNonPerhutananSosial.id else 1,
                type = type
            )
        )
        else {
            dokumenLahanPathList.add(
                FileEntity(
                    nama = nama,
                    path = filePath,
                    parent_id = if (formulirPembiayaanNonPerhutananSosial.id != 0) formulirPembiayaanNonPerhutananSosial.id else 1,
                    type = type
                )
            )
            uploadFile(File(filePath), REQUEST_CODE_DOKUMEN_LAINNYA)
        }
    }

    fun createOtherDocumentDraft(userId: String, description: String, fileId: String) {
        val disposable = pembiayaanPerhutananUseCase.createOtherDocumentDraft(
            OtherDocumentPost(
                userId = userId,
                name = description,
                description = description,
                file = fileId
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

    fun insertAllFile() {
        if (dataJaminanList.isNotEmpty()) pembiayaanPerhutananUseCase.insertFile(
            dataJaminanList
        )
        else if (dokumenLahanPathList.isNotEmpty()) pembiayaanPerhutananUseCase.insertFile(
            dokumenLahanPathList
        )
    }

    fun updateFile(
        filePath: String,
        position: Int,
        type: Int = Constants.DATA_JAMINAN,
        nama: String = ""
    ) {
        if (type == Constants.DATA_JAMINAN) dataJaminanList[position].path = filePath
        else {
            dokumenLahanPathList[position].path = filePath
            dokumenLahanPathList[position].nama = nama
        }
    }

    fun removeFile(position: Int, type: Int = Constants.DATA_JAMINAN) {
        if (type == Constants.DATA_JAMINAN) dataJaminanList.removeAt(position)
        else dokumenLahanPathList.removeAt(position)
    }

    fun getJaminan() {
        val disposable =
            pembiayaanPerhutananUseCase.fetchJaminan(
                formulirPembiayaanNonPerhutananSosial.id + 1
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    jaminanResult.value = it
                    jaminanList.addAll(it)
                }

        addDisposable(disposable)
    }

    fun insertJaminan(jaminanEntity: JaminanEntity) {
        if (formulirPembiayaanNonPerhutananSosial.id != 0) jaminanEntity.formulir_id =
            formulirPembiayaanNonPerhutananSosial.id
        jaminanList.add(
            jaminanEntity
        )
    }

    fun insertAllJaminan() {
        if (jaminanList.isNotEmpty()) pembiayaanPerhutananUseCase.insertJaminan(
            jaminanList
        )
    }

    fun updateJaminan(jaminanEntity: JaminanEntity, position: Int) {
        if (formulirPembiayaanNonPerhutananSosial.id != 0) jaminanEntity.formulir_id =
            formulirPembiayaanNonPerhutananSosial.id
        jaminanList[position] = jaminanEntity
    }

    fun removeJaminan(position: Int) {
        jaminanList.removeAt(position)
    }
}