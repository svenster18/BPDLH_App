package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.getCurrentDate
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
import id.bpdlh.fdb.databinding.FragmentDokumenLahanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import id.bpdlh.fdb.features.registration.dokumen.UploadDokumenDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

class DokumenLahanFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }

    private var _binding: FragmentDokumenLahanBinding? = null
    private val binding get() = _binding!!
    private var requestCode = 0
    private var changePosition = -1
    private var dokumenLahanItemAdapter = ItemAdapter<ViewItem<*>>()
    private var ijinLahanFile: File? = null
    private var suratTanahFile: File? = null
    private var suratJualBeliFile: File? = null
    private var spptFile: File? = null
    private var fotoLahanFile: File? = null
    private lateinit var progressDialog: CustomProgressDialog
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
                            ijinLahanFile = uriToFile(uri, requireContext())
                            if (ijinLahanFile != null) viewModel.formulirPembiayaanNonPerhutananSosial.ijin_lahan_path =
                                (ijinLahanFile as File).path
                            binding.cvDokumenIjinLahan.showPreviewImage(uri, requireContext())
                        }

                        Constants.REQUEST_CODE_SURAT_TANAH -> {
                            suratTanahFile = uriToFile(uri, requireContext())
                            if (suratTanahFile != null) viewModel.formulirPembiayaanNonPerhutananSosial.surat_tanah_path =
                                (suratTanahFile as File).path
                            binding.cvSuratKeteranganRiwayatTanah.showPreviewImage(
                                uri,
                                requireContext()
                            )
                        }

                        Constants.REQUEST_CODE_SURAT_JUAL_BELI -> {
                            suratJualBeliFile = uriToFile(uri, requireContext())
                            if (suratJualBeliFile != null) viewModel.formulirPembiayaanNonPerhutananSosial.surat_jual_beli_path =
                                (suratJualBeliFile as File).path
                            binding.cvSuratPernyataan.showPreviewImage(uri, requireContext())
                        }

                        Constants.REQUEST_CODE_SPPT -> {
                            spptFile = uriToFile(uri, requireContext())
                            if (spptFile != null) viewModel.formulirPembiayaanNonPerhutananSosial.sppt_path =
                                (suratTanahFile as File).path
                            binding.cvSppt.showPreviewImage(uri, requireContext())
                        }

                        Constants.REQUEST_CODE_FOTO_LAHAN -> {
                            fotoLahanFile = uriToFile(uri, requireContext())
                            if (fotoLahanFile != null) viewModel.formulirPembiayaanNonPerhutananSosial.foto_lahan_path =
                                (fotoLahanFile as File).path
                            binding.cvFotoLahan.showPreviewImage(uri, requireContext())
                        }
                    }
                }
            }
        }
    }

    private fun showListDokumenLahan() = runBlocking {
        var position = 0
        val items =
            viewModel.dokumenLahanPathList.map { file ->
                DokumenLahanItem.toViewItem(
                    file, position++, { _, position ->
                        changePosition = position
                        onChangeClick(file, position)
                    }, { position ->
                        onDelete(position)
                    }
                )
            }
        launch {
            dokumenLahanItemAdapter.set(items)
        }
    }

    private fun onDelete(position: Int) {
        viewModel.removeFile(
            position,
            Constants.DOKUMEN_PENGUASAAN_LAHAN
        )
        showListDokumenLahan()
    }

    private fun onChangeClick(fileEntity: FileEntity, position: Int) {
        val uploadDokumenDialogFragment = UploadDokumenDialogFragment(fileEntity) { updatedFile ->
            viewModel.updateFile(
                updatedFile.path, position, Constants.DOKUMEN_PENGUASAAN_LAHAN, updatedFile.nama
            )
        }
        uploadDokumenDialogFragment.show(parentFragmentManager, UploadDokumenDialogFragment.TAG)
    }

    override fun isValid(): Boolean {
        binding.apply {
            if (cvDokumenIjinLahan.ivFile.isGone && cvDokumenIjinLahan.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_ijin_lahan),
                    Constants.WARNING
                )
                return false
            }
            if (cvSuratKeteranganRiwayatTanah.ivFile.isGone && cvSuratKeteranganRiwayatTanah.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_surat_tanah),
                    Constants.WARNING
                )
                return false
            }
            if (cvSuratPernyataan.ivFile.isGone && cvSuratPernyataan.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_surat_jual_beli),
                    Constants.WARNING
                )
                return false
            }
            if (cvSppt.ivFile.isGone && cvSppt.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_sppt),
                    Constants.WARNING
                )
                return false
            }
            if (cvFotoLahan.ivFile.isGone && cvFotoLahan.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_foto_lahan),
                    Constants.WARNING
                )
                return false
            }
            val dibuatPadaTempatError =
                textILDibuatPadaTempat.editText?.text.toString()
                    .getError(requireContext())
            textILDibuatPadaTempat.error(dibuatPadaTempatError)
            if (dibuatPadaTempatError != null) return false
            if (!checkBox.isChecked) {
                requireContext().showToast(
                    getString(R.string.wajib_menyetujui_pernyataan),
                    Constants.WARNING
                )
                return false
            }
        }
        return true
    }

    override fun initUI() {
        binding.apply {
            showListDokumenLahan()
            val formulirPembiayaanNonPerhutananSosial =
                viewModel.formulirPembiayaanNonPerhutananSosial
            cvDokumenIjinLahan.showPreviewImage(
                formulirPembiayaanNonPerhutananSosial.ijin_lahan_path,
                requireContext()
            )
            cvSuratKeteranganRiwayatTanah.showPreviewImage(
                formulirPembiayaanNonPerhutananSosial.surat_tanah_path,
                requireContext()
            )
            cvSuratPernyataan.showPreviewImage(
                formulirPembiayaanNonPerhutananSosial.surat_jual_beli_path,
                requireContext()
            )
            cvSppt.showPreviewImage(
                formulirPembiayaanNonPerhutananSosial.sppt_path,
                requireContext()
            )
            cvFotoLahan.showPreviewImage(
                formulirPembiayaanNonPerhutananSosial.foto_lahan_path,
                requireContext()
            )
            if (formulirPembiayaanNonPerhutananSosial.dibuat_pada_tanggal.isEmpty()) {
                edtDibuatPadaTanggal.setText(getCurrentDate())
                formulirPembiayaanNonPerhutananSosial.dibuat_pada_tanggal = getCurrentDate()
            } else {
                edtDibuatPadaTanggal.setText(formulirPembiayaanNonPerhutananSosial.dibuat_pada_tanggal)
            }
        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDokumenLahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        observe(viewModel.submitOtherDocumentResult, ::onDataSubmitted)
        observe(viewModel.dokumenLainnyaUploadFileResult, ::onFileUploaded)
        binding.apply {
            rvUploadDokumen.apply {
                adapter = FastAdapter.with(dokumenLahanItemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnUploadDokumen.setOnClickListener {
                val uploadDokumenDialogFragment = UploadDokumenDialogFragment(onClick = { file ->
                    val userId =
                        LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
                    viewModel.insertFile(
                        file.path, file.nama, Constants.DOKUMEN_PENGUASAAN_LAHAN, userId.toString()
                    )
                    showListDokumenLahan()
                })
                uploadDokumenDialogFragment.show(
                    childFragmentManager,
                    UploadDokumenDialogFragment.TAG
                )
            }
            cvDokumenIjinLahan.apply {
                root.setOnClickListener {
                    chooseIjinLahan()
                }
                clUploadFile.setOnClickListener {
                    chooseIjinLahan()
                }
                tvGantiData.setOnClickListener {
                    chooseIjinLahan()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvSuratKeteranganRiwayatTanah.apply {
                root.setOnClickListener {
                    chooseSuratTanah()
                }
                clUploadFile.setOnClickListener {
                    chooseSuratTanah()
                }
                tvGantiData.setOnClickListener {
                    chooseSuratTanah()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvSuratPernyataan.apply {
                root.setOnClickListener {
                    chooseSuratJualBeli()
                }
                clUploadFile.setOnClickListener {
                    chooseSuratJualBeli()
                }
                tvGantiData.setOnClickListener {
                    chooseSuratJualBeli()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvSppt.apply {
                root.setOnClickListener {
                    chooseSppt()
                }
                clUploadFile.setOnClickListener {
                    chooseSppt()
                }
                tvGantiData.setOnClickListener {
                    chooseSppt()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvFotoLahan.apply {
                root.setOnClickListener {
                    chooseFotoLahan()
                }
                clUploadFile.setOnClickListener {
                    chooseFotoLahan()
                }
                tvGantiData.setOnClickListener {
                    chooseFotoLahan()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            textILDibuatPadaTempat.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DokumenLahanFragment)
            }
        }
    }

    private fun onDataSubmitted(state: ResultState<OtherDocumentEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success -> {
                context?.showToast("Tambah Dokumen Lainnya berhasil")
            }

            is ResultState.Loading -> {
                progressDialog.show()
            }

            else -> {
                context?.showToast(state.message.toString())
            }
        }
    }

    private fun onFileUploaded(state: ResultState<String>) {
        when (state) {
            is ResultState.Success -> {
                val userId =
                    LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
                viewModel.createOtherDocumentDraft(userId.toString(), "Test", state.data.toString())
            }

            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            else -> context?.showToast(state.message.toString(), Constants.WARNING)
        }
    }

    private fun chooseIjinLahan() {
        requestCode = Constants.REQUEST_CODE_IJIN_LAHAN
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseSuratTanah() {
        requestCode = Constants.REQUEST_CODE_SURAT_TANAH
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseSuratJualBeli() {
        requestCode = Constants.REQUEST_CODE_SURAT_JUAL_BELI
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseSppt() {
        requestCode = Constants.REQUEST_CODE_SPPT
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseFotoLahan() {
        requestCode = Constants.REQUEST_CODE_FOTO_LAHAN
        startFileChooser(launcherIntentFileChooser)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.apply {
            when (editText) {
                textILDibuatPadaTempat.editText -> {
                    val dibuatPadaTempat = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.dibuat_pada_tempat =
                        dibuatPadaTempat
                    textILDibuatPadaTempat.error =
                        dibuatPadaTempat.getError(requireContext())
                }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}