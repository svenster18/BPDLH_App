package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.databinding.TambahJaminanBottomSheetBinding
import timber.log.Timber
import java.io.File
import id.bpdlh.fdb.core.R as CoreR

class TambahJaminanBottomSheet(
    private val onClickSubmit: ((JaminanEntity) -> Unit),
    private val jaminanEntity: JaminanEntity? = null
) : BottomSheetDialogFragment(), TextWatcherTextChange {
    private var _binding: TambahJaminanBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var requestCode = 0
    private var buktiJaminanFile: File? = null
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null && requestCode == Constants.REQUEST_CODE_BUKTI_JAMINAN) {
                    buktiJaminanFile = uriToFile(uri, requireContext())
                    binding.cvUploadBuktiJaminan.showPreviewImage(uri, requireContext())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, CoreR.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            TambahJaminanBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            if (jaminanEntity != null) {
                edtBentukJaminan.setText(jaminanEntity.bentuk)
                edtNilaiJaminan.setText(currencyFormatter(jaminanEntity.nilai, false))
                cvUploadBuktiJaminan.showPreviewImage(jaminanEntity.photoPath, requireContext())
            }
            val bottomSheetBehavior = BottomSheetBehavior.from(modalBottom)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            cvUploadBuktiJaminan.apply {
                root.setOnClickListener {
                    chooseBuktiJaminan()
                }
                clUploadFile.setOnClickListener {
                    chooseBuktiJaminan()
                }
                tvGantiData.setOnClickListener {
                    chooseBuktiJaminan()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            btnClose.setOnClickListener { dialog?.cancel() }
            btnBatal.setOnClickListener { dialog?.cancel() }
            btnSubmit.setOnClickListener {
                if (isValid()) {
                    val bentukJaminan = edtBentukJaminan.text.toString()
                    val nilaiJaminan = edtNilaiJaminan.text.toString().extractNumber().toLong()
                    val photoPath = jaminanEntity?.photoPath ?: buktiJaminanFile?.path
                    onClickSubmit(JaminanEntity(bentukJaminan, nilaiJaminan, photoPath.toString()))
                    dialog?.dismiss()
                }
            }
            textILBentukJaminan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahJaminanBottomSheet)
            }
            textILNilaiJaminan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahJaminanBottomSheet)
            }
            edtNilaiJaminan.setOnFocusChangeListener { _, isFocus ->
                val strNilaiJaminan = edtNilaiJaminan.text.toString()
                if (strNilaiJaminan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strNilaiJaminan.extractNumber())
                        edtNilaiJaminan.setText(strNilaiJaminan.extractNumber())
                    } else edtNilaiJaminan.setText(
                        currencyFormatter(
                            strNilaiJaminan.toLong(),
                            false
                        )
                    )
                }
            }
        }
    }

    private fun isValid(): Boolean {
        binding.apply {
            val bentukJaminanError =
                textILBentukJaminan.editText?.text.toString().getError(requireContext())
            textILBentukJaminan.error(bentukJaminanError)
            if (bentukJaminanError != null) return false
            val nilaiJaminanError =
                textILNilaiJaminan.editText?.text.toString().getError(requireContext())
            textILNilaiJaminan.error(nilaiJaminanError)
            if (nilaiJaminanError != null) return false
            if (cvUploadBuktiJaminan.ivFile.isGone && cvUploadBuktiJaminan.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_bukti_jaminan),
                    Constants.WARNING
                )
                return false
            }
        }
        return true
    }

    private fun chooseBuktiJaminan() {
        requestCode = Constants.REQUEST_CODE_BUKTI_JAMINAN
        startFileChooser(launcherIntentFileChooser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "TambahJaminanBottomSheet"
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.apply {
            when (editText) {
                binding.textILBentukJaminan.editText -> {
                    val strBentukJaminan = editText?.text.toString()
                    binding.textILBentukJaminan.error = strBentukJaminan.getError(requireContext())
                }

                binding.textILNilaiJaminan.editText -> {
                    val strNilaiJaminan = editText?.text.toString()
                    binding.textILNilaiJaminan.error = strNilaiJaminan.getError(requireContext())
                }
            }
        }
    }
}