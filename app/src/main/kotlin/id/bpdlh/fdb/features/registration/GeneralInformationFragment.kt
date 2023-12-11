package id.bpdlh.fdb.features.registration

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.DATE_PICKER_TAG
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_KK
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_KTP
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.getDateError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.getNikError
import id.bpdlh.fdb.core.common.utils.getNoKkError
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.databinding.FragmentGeneralInformationBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import java.io.File
import javax.inject.Inject


class GeneralInformationFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private lateinit var binding: FragmentGeneralInformationBinding
    private var requestCode = 0
    private var ktpFile: File? = null
    private var kkFile: File? = null
    private val jenisKelaminList = listOf("Laki-laki", "Perempuan")
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null) {
                    when (requestCode) {
                        REQUEST_CODE_KTP -> {
                            ktpFile = uriToFile(uri, requireContext())
                            ktpFile?.let {
                                registrasiPerorangan.ktpPath = (ktpFile as File).path
                                viewModel.formulirPembiayaanPerhutananSosial.ktpFile = it
                            }
                            binding.cvKtp.showPreviewImage(uri, requireContext())
                        }

                        REQUEST_CODE_KK -> {
                            kkFile = uriToFile(uri, requireContext())
                            kkFile?.let {
                                registrasiPerorangan.kkPath = it.path
                                viewModel.formulirPembiayaanPerhutananSosial.kkFile = it
                            }
                            binding.cvKk.showPreviewImage(uri, requireContext())
                        }
                    }
                }
            }
        }
    }

    override fun isValid(): Boolean {
        binding.apply {
            val nikError = textILNIK.editText?.text.toString().getNikError(requireContext())
            textILNIK.error(nikError)
            if (nikError != null) return false
            if (cvKtp.ivFile.isGone && cvKtp.clPreviewFile.isGone) {
                requireContext().showToast(getString(R.string.wajib_upload_ktp), Constants.WARNING)
                return false
            }
            val namaError =
                textILNamaLengkap.editText?.text.toString().getGeneralError(requireContext())
            textILNamaLengkap.error(namaError)
            if (namaError != null) return false
            val tempatLahirError =
                textILTempatLahir.editText?.text.toString().getGeneralError(requireContext())
            textILTempatLahir.error(tempatLahirError)
            if (tempatLahirError != null) return false
            val batasAkhirError = textILBatasAkhirKepengurusan.editText?.text.toString()
                .getDateError(requireContext())
            textILBatasAkhirKepengurusan.error(batasAkhirError)
            if (batasAkhirError != null) return false
            val jenisKelaminError =
                textILJenisKelaminDebitur.editText?.text.toString().getError(requireContext())
            textILJenisKelaminDebitur.error(jenisKelaminError)
            if (jenisKelaminError != null) return false
            val noKkError = textILNoKk.editText?.text.toString().getNikError(requireContext())
            textILNoKk.error(noKkError)
            if (noKkError != null) return false
            if (cvKk.ivFile.isGone && cvKk.clPreviewFile.isGone) {
                requireContext().showToast(getString(R.string.wajib_upload_kk), Constants.WARNING)
                return false
            }
            val pekerjaanUtama =
                textILPekerjaanTetap.editText?.text.toString().getGeneralError(requireContext())
            textILPekerjaanTetap.error(pekerjaanUtama)
            if (pekerjaanUtama != null) return false
            val pekerjaanLain =
                textILPekerjaanUsaha.editText?.text.toString().getGeneralError(requireContext())
            textILPekerjaanUsaha.error(pekerjaanLain)
            if (pekerjaanLain != null) return false
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGeneralInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textILNIK.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
            cvKtp.apply {
                root.setOnClickListener {
                    chooseKtp()
                }
                clUploadFile.setOnClickListener {
                    chooseKtp()
                }
                tvGantiData.setOnClickListener {
                    chooseKtp()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvKk.apply {
                root.setOnClickListener {
                    chooseKk()
                }
                clUploadFile.setOnClickListener {
                    chooseKk()
                }
                tvGantiData.setOnClickListener {
                    chooseKk()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                }
            }
            textILNamaLengkap.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
            textILTempatLahir.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
            textILBatasAkhirKepengurusan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
            textILBatasAkhirKepengurusan.setEndIconOnClickListener {
                val datePickerSpinner =
                    DatePickerSpinner(
                        textILBatasAkhirKepengurusan.editText as TextView
                    )
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            textILBatasAkhirKepengurusan.setErrorIconOnClickListener {
                val datePickerSpinner =
                    DatePickerSpinner(
                        textILBatasAkhirKepengurusan.editText as TextView
                    )
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            val jenisKelaminAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                jenisKelaminList
            )
            edtJenisKelaminDebitur.setAdapter(jenisKelaminAdapter)
            edtJenisKelaminDebitur.isFocusable = false
            edtJenisKelaminDebitur.setOnItemClickListener { _, _, _, _ ->
                val jenisKelamin = edtJenisKelaminDebitur.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.gender = jenisKelamin
                registrasiPerorangan.jenisKelamin = jenisKelamin
            }
            textILNoKk.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
            textILPekerjaanTetap.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
            textILPekerjaanUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@GeneralInformationFragment)
            }
        }
    }

    private fun chooseKk() {
        requestCode = REQUEST_CODE_KK
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseKtp() {
        requestCode = REQUEST_CODE_KTP
        startFileChooser(launcherIntentFileChooser)
    }

    override fun initUI() {
        binding.apply {
            when (category) {
                Constants.PERHUTANAN_SOSIAL -> {
                    val formPembiayaan = viewModel.formulirPembiayaanPerhutananSosial
                    edtNamaKelompok.setText(formPembiayaan.groupName)
                    edtNIK.setText(formPembiayaan.ktp)
                    cvKtp.showPreviewImage(formPembiayaan.ktpFileUrl, requireContext())
                    cvKk.showPreviewImage(formPembiayaan.kkFileUrl, requireContext())
                    edtNamaLengkap.setText(formPembiayaan.name)
                    edtTempatLahir.setText(formPembiayaan.placeOfBirth)
                    if (formPembiayaan.dateOfBirth != "-") edtBatasAkhirKepengurusan.setText(
                        formPembiayaan.dateOfBirth
                    )
                    edtJenisKelaminDebitur.setText(formPembiayaan.gender)
                    edtNoKk.setText(formPembiayaan.kk)
                    edtPekerjaanTetap.setText(formPembiayaan.job)
                    edtPekerjaanUsaha.setText(formPembiayaan.otherJob)
                }

                else -> {
                    edtNamaKelompok.setText(registrasiPerorangan.groupName)
                    edtNIK.setText(registrasiPerorangan.nik)
                    cvKtp.showPreviewImage(registrasiPerorangan.ktpPath, requireContext())
                    cvKk.showPreviewImage(registrasiPerorangan.kkPath, requireContext())
                    edtNamaLengkap.setText(registrasiPerorangan.nama)
                    edtTempatLahir.setText(registrasiPerorangan.tempatLahir)
                    edtBatasAkhirKepengurusan.setText(registrasiPerorangan.tanggalLahir)
                    edtJenisKelaminDebitur.setText(registrasiPerorangan.jenisKelamin)
                    edtNoKk.setText(registrasiPerorangan.noKk)
                    edtPekerjaanTetap.setText(registrasiPerorangan.pekerjaanUtama)
                    edtPekerjaanUsaha.setText(registrasiPerorangan.pekerjaanLain)
                }
            }

        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILNIK.editText -> {
                registrasiPerorangan.nik = editText?.text.toString()
                val nik = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.ktp = nik
                binding.textILNIK.error = nik.getNikError(requireContext())
            }

            binding.textILNamaLengkap.editText -> {
                registrasiPerorangan.nama = editText?.text.toString()
                val nama = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.name = nama
                binding.textILNamaLengkap.error = nama.getGeneralError(requireContext())
            }

            binding.textILTempatLahir.editText -> {
                registrasiPerorangan.tempatLahir = editText?.text.toString()
                val tempatLahir = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.placeOfBirth = tempatLahir
                binding.textILTempatLahir.error = tempatLahir.getGeneralError(requireContext())
            }

            binding.textILBatasAkhirKepengurusan.editText -> {
                registrasiPerorangan.tanggalLahir = editText?.text.toString()
                val batasAkhirKepengurusan = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.dateOfBirth = batasAkhirKepengurusan
                binding.textILBatasAkhirKepengurusan.error =
                    batasAkhirKepengurusan.getDateError(requireContext())
            }

            binding.textILJenisKelaminDebitur.editText -> {
                registrasiPerorangan.jenisKelamin = editText?.text.toString()
                val jenisKelamin = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.gender = jenisKelamin
                binding.textILJenisKelaminDebitur.error =
                    jenisKelamin.getError(requireContext())
            }

            binding.textILNoKk.editText -> {
                registrasiPerorangan.noKk = editText?.text.toString()
                val noKk = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.kk = noKk
                binding.textILNoKk.error = noKk.getNoKkError(requireContext())
            }

            binding.textILPekerjaanTetap.editText -> {
                registrasiPerorangan.pekerjaanUtama = editText?.text.toString()
                val pekerjaanUtama = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.job = pekerjaanUtama
                binding.textILPekerjaanTetap.error =
                    pekerjaanUtama.getGeneralError(requireContext())
            }

            binding.textILPekerjaanUsaha.editText -> {
                registrasiPerorangan.pekerjaanLain = editText?.text.toString()
                val pekerjaanUsaha = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.otherJob = pekerjaanUsaha
                binding.textILPekerjaanUsaha.error =
                    pekerjaanUsaha.getGeneralError(requireContext())
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}