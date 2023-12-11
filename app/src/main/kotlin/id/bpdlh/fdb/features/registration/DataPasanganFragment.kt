package id.bpdlh.fdb.features.registration

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.DATE_PICKER_TAG
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.databinding.FragmentDataPasanganBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import java.io.File
import javax.inject.Inject

class DataPasanganFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private lateinit var binding: FragmentDataPasanganBinding
    private var requestCode = 0
    private var ktpFile: File? = null
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null && requestCode == Constants.REQUEST_CODE_KTP) {
                    ktpFile = uriToFile(uri, requireContext())
                    ktpFile?.let {
                        registrasiPerorangan.ktpPathPasangan = it.path
                        viewModel.formulirPembiayaanPerhutananSosial.coupleKtpFile = it
                    }
                    binding.cvKtp.showPreviewImage(uri, requireContext())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDataPasanganBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textILNIK.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPasanganFragment)
            }
            textILNamaLengkap.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPasanganFragment)
            }
            textILTanggalLahirPasangan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPasanganFragment)
            }
            textILTempatLahirPasangan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPasanganFragment)
            }
            textILTanggalLahirPasangan.setEndIconOnClickListener {
                val datePickerSpinner =
                    DatePickerSpinner(textILTanggalLahirPasangan.editText as TextView)
                datePickerSpinner.show(
                    childFragmentManager,
                    DATE_PICKER_TAG
                )
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
        }
    }

    private fun chooseKtp() {
        requestCode = Constants.REQUEST_CODE_KTP
        startFileChooser(launcherIntentFileChooser)
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun initUI() {
        binding.apply {
            when (category) {
                Constants.PERHUTANAN_SOSIAL -> {
                    val formPembiayaan = viewModel.formulirPembiayaanPerhutananSosial
                    edtNIK.setText(formPembiayaan.coupleKtp)
                    edtNamaLengkap.setText(formPembiayaan.coupleName)
                    if (formPembiayaan.couplePlaceOfBirth != "-") edtTempatLahirPasangan.setText(
                        formPembiayaan.couplePlaceOfBirth
                    )
                    if (formPembiayaan.coupleDateOfBirth != "-") edtTanggalLahirPasangan.setText(
                        formPembiayaan.coupleDateOfBirth
                    )
                    cvKtp.showPreviewImage(formPembiayaan.coupleKtpFileUrl, requireContext())
                }

                else -> {
                    edtNIK.setText(registrasiPerorangan.nikPasangan)
                    edtNamaLengkap.setText(registrasiPerorangan.namaPasangan)
                    edtTanggalLahirPasangan.setText(registrasiPerorangan.tanggalLahirPasangan)
                    edtTempatLahirPasangan.setText(registrasiPerorangan.tempatLahirPasangan)
                    cvKtp.showPreviewImage(registrasiPerorangan.ktpPathPasangan, requireContext())
                }
            }

        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILNIK.editText -> {
                registrasiPerorangan.nikPasangan = editText?.text.toString()
                val nik = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.coupleKtp = nik
            }

            binding.textILNamaLengkap.editText -> {
                registrasiPerorangan.namaPasangan = editText?.text.toString()
                val nama = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.coupleName = nama
            }

            binding.textILTanggalLahirPasangan.editText -> {
                registrasiPerorangan.tanggalLahirPasangan = editText?.text.toString()
                val tanggalLahir = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.coupleDateOfBirth = tanggalLahir
            }

            binding.textILTempatLahirPasangan.editText -> {
                registrasiPerorangan.tempatLahirPasangan = editText?.text.toString()
                val tempatLahir = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.couplePlaceOfBirth = tempatLahir
            }
        }
    }
}