package id.bpdlh.fdb.features.fpns

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
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.databinding.FragmentDokumenPenguasaanLahanNonSosialBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DokumenLahanItem
import id.bpdlh.fdb.features.registration.dokumen.UploadDokumenDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

class DokumenLegalitasNonSosialFragment: BaseFormPermohonanNonSosialFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormPermohonanNonSosialViewModel> { factory }

    private lateinit var binding: FragmentDokumenPenguasaanLahanNonSosialBinding
    private var communicator: IFormPermohonanKelompokNonSosialCommunicator? = null
    private var agree = false
    private var requestCode = 0
    private var changePosition = -1
    private var landTenureFile : File? = null
    private var landHistoryFile : File? = null
    private var transferDeclarationFile : File? = null
    private var spptFile : File? = null
    private var landPhotoFile : File? = null
    private var dokumenItemAdapter = ItemAdapter<ViewItem<*>>()
    private lateinit var progressDialog: CustomProgressDialog
    val dokumenPathList = arrayListOf<FileEntity>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormPermohonanKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDokumenPenguasaanLahanNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        with(binding){
            rvUploadDokumen.apply {
                adapter = FastAdapter.with(dokumenItemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            ijinLahanFileUrl?.let {
                cvDokumenIjinLahan.showPreviewImage(it, requireContext())
            }
            suratTanahFileUrl?.let {
                cvDokumenIjinLahan.showPreviewImage(it, requireContext())
            }
            spptFileFileUrl?.let {
                cvSppt.showPreviewImage(it, requireContext())
            }
            suratJualBeliFileUrl?.let {
                cvSuratKeteranganRiwayatTanah.showPreviewImage(it, requireContext())
            }
            fotoLahanFileUrl?.let {
                cvFotoLahan.showPreviewImage(it, requireContext())
            }
            dokumenLainnyaFileUrl?.let {
            }
            memberApplicationPost.financingCreatedIn?.let {
                edtDibuatPadaTempat.setText(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        formWatcher()
        observe(viewModel.submitOtherDocumentResult, ::observePostOtherDocument)
        observe(viewModel.resultDocumentOther, ::observeFetchOtherDocument)
        observe(viewModel.resultDocumentOtherDelete, ::observeDeleteOtherDocument)
        observe(viewModel.ijinLahanFileUploadResult, ::observeIjinLahanUpload)
        observe(viewModel.fotoLahanFileUploadResult, ::observeFotoLahanUpload)
        observe(viewModel.spptFileUploadResult, ::observeSpptUpload)
        observe(viewModel.suratJualBeliFileUploadResult, ::observeSuratJualBeliUpload)
        observe(viewModel.suratTanahFileUploadResult, ::observeSuratTanahUpload)
        observe(viewModel.dokumenLainnyaUploadFileResult, ::observeDokumenLainnyaUpload)
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
                    dokumenPathList.add(file)
                    viewModel.postDocument(
                        OtherDocumentPost(
                        userId = memberApplicationId,
                        name = file.nama,
                        description = file.nama,
                        file = file.path
                    ))
                })
                    uploadDokumenDialogFragment.show(
                        childFragmentManager,
                        UploadDokumenDialogFragment.TAG
                )
            }
            cvDokumenIjinLahan.apply {
                root.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_IJIN_LAHAN)
                }
                tvGantiData.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_IJIN_LAHAN)
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landTenureFile = null
                    root.setOnClickListener {
                        choseDocument(Constants.REQUEST_CODE_IJIN_LAHAN)
                    }
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landTenureFile = null
                }
            }
            cvSuratKeteranganRiwayatTanah.apply {
                root.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_SURAT_TANAH)
                }
                tvGantiData.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_SURAT_TANAH)
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landHistoryFile = null
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landHistoryFile = null
                }
            }
            cvSuratPernyataan.apply {
                root.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_SURAT_JUAL_BELI)
                }
                tvGantiData.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_SURAT_JUAL_BELI)
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.transferDeclarationFile = null
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.transferDeclarationFile = null
                }
            }
            cvSppt.apply {
                root.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_SPPT)
                }
                tvGantiData.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_SPPT)
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.spptFile = null
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.spptFile = null
                }
            }
            cvFotoLahan.apply {
                root.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_FOTO_LAHAN)
                }
                tvGantiData.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_FOTO_LAHAN)
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landPhotoFile = null
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landPhotoFile = null
                }
            }
            checkBox.setOnClickListener {
                checkAgreement()
            }
            edtDibuatPadaTanggal.setText(getCurrentDate())
            showDokumen()
            fetchOtherDocument()
        }
    }

    private fun showDokumen() {
        with(binding) {
            when(memberApplicationPost.serviceType) {
                Constants.TUNDA_TEBANG -> {
                    cvDokumenIjinLahan.apply {
                        root.visible()
                    }
                    cvSuratKeteranganRiwayatTanah.apply {
                        root.visible()
                    }
                    cvSuratPernyataan.apply {
                        root.visible()
                    }
                    cvSppt.apply {
                        root.visible()
                    }
                    cvFotoLahan.apply {
                        root.visible()
                    }
                }
                Constants.KOMODITAS_NON_KEHUTANAN -> {
                    tv2.gone()
                    tv3.visible()
                    tv4.gone()
                    tv5.visible()
                    btnUnduhDokumenRujukan2.gone()
                    btnUnduhDokumenRujukan3.visible()
                    btnUnduhDokumenRujukan4.gone()
                    btnUnduhDokumenRujukan5.visible()
                    cvDokumenIjinLahan.apply {
                        root.visible()
                    }
                    cvSuratKeteranganRiwayatTanah.apply {
                        root.gone()
                    }
                    cvSuratPernyataan.apply {
                        root.visible()
                    }
                    cvSppt.apply {
                        root.gone()
                    }
                    cvFotoLahan.apply {
                        root.visible()
                    }
                }
                Constants.HASIL_HUTAN_BUKAN_KAYU -> {
                    tv2.gone()
                    tv3.visible()
                    tv4.gone()
                    tv5.visible()
                    btnUnduhDokumenRujukan2.gone()
                    btnUnduhDokumenRujukan3.visible()
                    btnUnduhDokumenRujukan4.gone()
                    btnUnduhDokumenRujukan5.visible()
                    cvDokumenIjinLahan.apply {
                        root.visible()
                    }
                    cvSuratKeteranganRiwayatTanah.apply {
                        root.gone()
                    }
                    cvSuratPernyataan.apply {
                        root.visible()
                    }
                    cvSppt.apply {
                        root.gone()
                    }
                    cvFotoLahan.apply {
                        root.visible()
                    }
                }
                else -> {}
            }
            //? Show Current Draft Dokumen
            ijinLahanFileUrl?.let {
                cvDokumenIjinLahan.showPreviewImage(it, requireContext())
            }
            suratTanahFileUrl?.let {
                cvSuratKeteranganRiwayatTanah.showPreviewImage(it, requireContext())
            }
            suratJualBeliFileUrl?.let {
                cvSuratPernyataan.showPreviewImage(it, requireContext())
            }
            spptFileFileUrl?.let {
                cvSppt.showPreviewImage(it, requireContext())
            }
            fotoLahanFileUrl?.let {
                cvFotoLahan.showPreviewImage(it, requireContext())
            }
        }
    }

    private fun fetchOtherDocument() {
        viewModel.getDocumentDraft(memberApplicationId)
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
                        Constants.REQUEST_CODE_IJIN_LAHAN -> {
                            landTenureFile = uriToFile(uri, requireContext())
                            binding.cvDokumenIjinLahan.showPreviewImage(uri, requireContext())
                            landTenureFile?.let {
                                viewModel.uploadFile(it, requestCode)
                            }
                        }
                        Constants.REQUEST_CODE_SURAT_TANAH -> {
                            landHistoryFile = uriToFile(uri, requireContext())
                            binding.cvSuratKeteranganRiwayatTanah.showPreviewImage(
                                uri,
                                requireContext()
                            )
                            landHistoryFile?.let {
                                viewModel.uploadFile(it, requestCode)
                            }
                        }
                        Constants.REQUEST_CODE_SURAT_JUAL_BELI -> {
                            transferDeclarationFile = uriToFile(uri, requireContext())
                            binding.cvSuratPernyataan.showPreviewImage(uri, requireContext())
                            transferDeclarationFile?.let {
                                viewModel.uploadFile(it, requestCode)
                            }
                        }
                        Constants.REQUEST_CODE_SPPT -> {
                            spptFile = uriToFile(uri, requireContext())
                            binding.cvSppt.showPreviewImage(uri, requireContext())
                            spptFile?.let {
                                viewModel.uploadFile(it, requestCode)
                            }
                        }
                        Constants.REQUEST_CODE_FOTO_LAHAN -> {
                            landPhotoFile = uriToFile(uri, requireContext())
                            binding.cvFotoLahan.showPreviewImage(uri, requireContext())
                            landPhotoFile?.let {
                                viewModel.uploadFile(it, requestCode)
                            }
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

    private fun observeDokumenLainnyaUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
//                memberApplicationPost.file
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeSuratTanahUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                memberApplicationPost.landHistoryFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeSuratJualBeliUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                memberApplicationPost.transferDeclarationFile = state.data
                memberApplicationPost.otherSupportingFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeSpptUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                memberApplicationPost.spptFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeIjinLahanUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                memberApplicationPost.landTenureFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeFotoLahanUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                memberApplicationPost.landPhotoFile = state.data
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
                checkBox.isChecked=false
            } else {
                agree = true
                checkBox.isChecked = true
            }
        }
    }

    private fun choseDocument(request: Int) {
        requestCode = request
        startFileChooser(launcherIntentFileChooser)
    }

    private fun formWatcher() {
        with(binding) {
            textILDibuatPadaTempat.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DokumenLegalitasNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val dibuatTanggal = textILDibuatPadaTanggal.editText?.text.toString().getDateError(requireContext())
            dibuatTanggal?.let { errorMessages.add(it) }
            textILDibuatPadaTanggal.error = dibuatTanggal

            val dibuatPada = textILDibuatPadaTempat.editText?.text.toString().getError(requireContext())
            dibuatPada?.let { errorMessages.add(it) }
            textILDibuatPadaTempat.error = dibuatPada
        }
        if (errorMessages.size == 0) {
            communicator?.onSubmit()
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText) {
            binding.textILDibuatPadaTempat.editText -> {
                memberApplicationPost.financingCreatedIn = editText?.text.toString()
            }
            binding.textILDibuatPadaTanggal.editText -> {
//                memberApplicationPost.finan = editText?.text.toString()
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}