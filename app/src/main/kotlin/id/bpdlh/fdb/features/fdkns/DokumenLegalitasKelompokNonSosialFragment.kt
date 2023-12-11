package id.bpdlh.fdb.features.fdkns

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getCurrentDate
import id.bpdlh.fdb.core.common.utils.getDateError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.databinding.FragmentFormulirDokumenNonSosialBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DokumenLahanItem
import id.bpdlh.fdb.features.registration.dokumen.UploadDokumenDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import id.bpdlh.fdb.core.R as CoreR

class DokumenLegalitasKelompokNonSosialFragment: BaseFormDaftarNonSosialFragment(), TextWatcherTextChange {


    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormKelompokNonSosialViewModel> { factory }

    private lateinit var binding: FragmentFormulirDokumenNonSosialBinding
    private var communicator: IFormulirPendaftaranKelompokNonSosialCommunicator? = null
    private var agree = false
    private var requestCode = 0
    private var changePosition = -1
    private var skBeritaAcaraFile : File? = null
    private var adArtFile : File? = null
    private var suratPendampingFile : File? = null
    private var dokumenItemAdapter = ItemAdapter<ViewItem<*>>()
    private lateinit var progressDialog: CustomProgressDialog
    val dokumenPathList = arrayListOf<FileEntity>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormulirDokumenNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        with(binding){
            rvUploadDokumen.apply {
                adapter = FastAdapter.with(dokumenItemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            adFileUrl?.let {
                cvAdArt.showPreviewImage(it, requireContext())
            }
            skFileUrl?.let {
                cvSkPembentukanBeritaAcara.showPreviewImage(it, requireContext())
            }
            companionRecomendationFileUrl?.let {
                cvSuratRekomendasi.showPreviewImage(it, requireContext())
            }
            tieDibuatPada.setText(formulirKelompokNonSosialPost.createdIn)
            tieDibuatPadaTanggal.setText(formulirKelompokNonSosialPost.administrationDeadlineDate)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        formWatcher()
        observe(viewModel.submitOtherDocumentResult, ::observePostOtherDocument)
        observe(viewModel.resultDocumentOther, ::observeFetchOtherDocument)
        observe(viewModel.resultDocumentOtherDelete, ::observeDeleteOtherDocument)
        observe(viewModel.adArtFileUploadResult, ::observeAdArtFileUpload)
        observe(viewModel.skBeritaAcaraFileUploadResult, ::observeSkBeritaAcaraFileUpload)
        observe(viewModel.suratPendampingFileUploadResult, ::observeSuratPendampingFileUpload)
    }

    private fun initUi(){
        with(binding) {
            tvDescription.text = getString(R.string.formulir_pendaftaran_kelompok_non_sosial_dokumen_legalitas)
            tvStep.text = requireContext().getString(R.string.formulir_pendaftaran_kelompok_non_sosial_text_stepper, "4")
            incFooter.btnKembali.setOnClickListener {
                communicator?.goToPage(2)
            }
            incFooter.btnSelanjutnya.text = getString(R.string.btn_submit)
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
            btnUploadDokumen.setOnClickListener {
                val uploadDokumenDialogFragment = UploadDokumenDialogFragment(onClick = { file ->
                    val userId =
                        LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
                    viewModel.insertFile(
                        file.path, file.nama, userId.toString()
                    )
                })
                    uploadDokumenDialogFragment.show(
                        childFragmentManager,
                        UploadDokumenDialogFragment.TAG
                )
            }
            cvSkPembentukanBeritaAcara.apply {
                root.setOnClickListener {
                    choseSKDocument()
                }
                tvGantiData.setOnClickListener {
                    choseSKDocument()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    formulirKelompokNonSosialPost.skFile = null
                    root.setOnClickListener {
                        choseSKDocument()
                    }
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    formulirKelompokNonSosialPost.skFile = null
                }
            }
            cvAdArt.apply {
                root.setOnClickListener {
                    choseADARTDocument()
                }
                tvGantiData.setOnClickListener {
                    choseADARTDocument()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    formulirKelompokNonSosialPost.adFile = null
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    formulirKelompokNonSosialPost.adFile = null
                }
            }
            cvSuratRekomendasi.apply {
                root.setOnClickListener {
                    choseSuratPendampingDocument()
                }
                tvGantiData.setOnClickListener {
                    choseSuratPendampingDocument()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    formulirKelompokNonSosialPost.companionRecomendationFile = null
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    formulirKelompokNonSosialPost.companionRecomendationFile = null
                }
            }
            ivCheck.setOnClickListener {
                checkAgreement()
            }
            tieDibuatPadaTanggal.setText(getCurrentDate())
            fetchOtherDocument()
        }
    }

    private fun fetchOtherDocument() {
        val userId =
            LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
        viewModel.getDocumentDraft(userId)
    }

    private fun showListDokumen() = runBlocking {
        var position = 0
        val items =
            dokumenPathList.map { file ->
                DokumenLahanItem.toViewItem(
                    file, position++, { _, position ->
                        changePosition = position
                        onChangeClick(file, position)
                    }, { position ->
                        onDelete(file.id_temp_api)
                    }
                )
            }
        launch {
            dokumenItemAdapter.set(items)
        }
    }

    private fun onDelete(id: String) {
        viewModel.deleteOtherDocument(id)
    }

    private fun onChangeClick(fileEntity: FileEntity, position: Int) {
        val uploadDokumenDialogFragment = UploadDokumenDialogFragment(fileEntity) { updatedFile ->
        }
        uploadDokumenDialogFragment.show(parentFragmentManager, UploadDokumenDialogFragment.TAG)
    }

    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null) {
                    when (requestCode) {
                        Constants.REQUEST_CODE_DOKUMEN_SK_BERITA_ACARA -> {
                            skBeritaAcaraFile = uriToFile(uri, requireContext())
                            binding.cvSkPembentukanBeritaAcara.showPreviewImage(uri, requireContext())
                            skBeritaAcaraFile?.let { viewModel.uploadFile(it, requestCode) }
                        }
                        Constants.REQUEST_CODE_DOKUMEN_AD_ART -> {
                            adArtFile = uriToFile(uri, requireContext())
                            adArtFile?.let { viewModel.uploadFile(it, requestCode) }
                            binding.cvAdArt.showPreviewImage(uri, requireContext())
                        }
                        Constants.REQUEST_CODE_DOKUMEN_SURAT_PENDAMPING -> {
                            suratPendampingFile = uriToFile(uri, requireContext())
                            binding.cvSuratRekomendasi.showPreviewImage(uri, requireContext())
                            suratPendampingFile?.let { viewModel.uploadFile(it, requestCode) }
                        }
                    }
                }
            }
        }
    }

    private fun observePostOtherDocument(state: ResultState<OtherDocumentEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<OtherDocumentEntity> -> {
                fetchOtherDocument()
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeFetchOtherDocument(state: ResultState<List<OtherDocumentEntity>>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<List<OtherDocumentEntity>> -> {
                dokumenPathList.clear()
                state.data?.map {
                    dokumenPathList.add(FileEntity(
                        nama = it.name.toString(),
                        path = it.file.toString(),
                        id_temp_api = it.id.toString()
                    ))
                }
                showListDokumen()
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeDeleteOtherDocument(state: ResultState<Any>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                context?.showToast(state.message.orEmpty())
                fetchOtherDocument()
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }
    private fun observeAdArtFileUpload(state: ResultState<String>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                formulirKelompokNonSosialPost.adFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeSkBeritaAcaraFileUpload(state: ResultState<String>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                formulirKelompokNonSosialPost.skFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeSuratPendampingFileUpload(state: ResultState<String>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                formulirKelompokNonSosialPost.companionRecomendationFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun checkAgreement() {
        with(binding) {
            if (agree) {
                agree = false
                ivCheck.setImageResource(CoreR.drawable.ic_checkbox_base)
            } else {
                agree = true
                ivCheck.setImageResource(CoreR.drawable.ic_checkbox_checked)
            }
        }
    }

    private fun choseSKDocument() {
        requestCode = Constants.REQUEST_CODE_DOKUMEN_SK_BERITA_ACARA
        startFileChooser(launcherIntentFileChooser)
    }


    private fun choseADARTDocument() {
        requestCode = Constants.REQUEST_CODE_DOKUMEN_AD_ART
        startFileChooser(launcherIntentFileChooser)
    }


    private fun choseSuratPendampingDocument() {
        requestCode = Constants.REQUEST_CODE_DOKUMEN_SURAT_PENDAMPING
        startFileChooser(launcherIntentFileChooser)
    }

    private fun formWatcher() {
        with(binding) {
//            tilDibuatPadaTanggal.setEndIconOnClickListener {
//                val datePickerSpinner = DatePickerSpinner(tilDibuatPadaTanggal.editText as TextView)
//                datePickerSpinner.show(childFragmentManager, Constants.DATE_PICKER_TAG)
//            }
//            tilDibuatPadaTanggal.editText?.let {
//                CustomTextWatcher().registerEditText(it)
//                    .setCallBackOnTextChange(this@DokumenLegalitasKelompokNonSosialFragment)
//            }
            tilDibuatPada.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DokumenLegalitasKelompokNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val dibuatTanggal = tilDibuatPadaTanggal.editText?.text.toString().getDateError(requireContext())
            dibuatTanggal?.let { errorMessages.add(it) }
            tilDibuatPadaTanggal.error = dibuatTanggal

            val dibuatPada = tilDibuatPada.editText?.text.toString().getError(requireContext())
            dibuatPada?.let { errorMessages.add(it) }
            tilDibuatPada.error = dibuatPada
        }
        if (errorMessages.size == 0) {
            communicator?.onSubmit()
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText) {
            binding.tilDibuatPada.editText -> {
                formulirKelompokNonSosialPost.createdIn = editText?.text.toString()
            }
            binding.tilDibuatPadaTanggal.editText -> {
                formulirKelompokNonSosialPost.administrationDeadlineDate = editText?.text.toString()
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}