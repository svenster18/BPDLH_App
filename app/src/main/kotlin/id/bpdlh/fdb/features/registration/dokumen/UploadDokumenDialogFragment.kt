package id.bpdlh.fdb.features.registration.dokumen

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.databinding.FragmentUploadDokumenDialogBinding
import java.io.File
import javax.inject.Inject

class UploadDokumenDialogFragment(
    private val fileEntity: FileEntity? = null,
    private val onClick: ((FileEntity) -> Unit)? = null
) : BaseDaggerBottomDialogFragment(), TextWatcherTextChange,
    HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private var _binding: FragmentUploadDokumenDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var requestCode = 0
    private var dokumenLainnyaFile: File? = null
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null && requestCode == Constants.REQUEST_CODE_DOKUMEN_LAINNYA) {
                    dokumenLainnyaFile = uriToFile(uri, requireContext())
                    binding.cvUploadDokumen.showPreviewImage(uri, requireContext())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadDokumenDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.modalBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            if (fileEntity != null) {
                edtNamaDokumen.setText(fileEntity.nama)
                cvUploadDokumen.showPreviewImage(fileEntity.path, requireContext())
            }

            textILNamaDokumen.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UploadDokumenDialogFragment)
            }
            cvUploadDokumen.apply {
                root.setOnClickListener {
                    chooseDokumenLainnya()
                }
                clUploadFile.setOnClickListener {
                    chooseDokumenLainnya()
                }
                tvGantiData.setOnClickListener {
                    chooseDokumenLainnya()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                }
            }
            btnSubmit.setOnClickListener {
                dokumenLainnyaFile?.let {
                    val nama = edtNamaDokumen.text.toString()
                    val photoPath = it.path ?: fileEntity?.path
                    onClick?.let { it1 ->
                        it1(
                            FileEntity(
                                nama = nama,
                                path = photoPath.toString()
                            )
                        )
                    }
                }
                dialog?.dismiss()
            }
            btnBatal.setOnClickListener { dialog?.cancel() }
            btnClose.setOnClickListener { dialog?.cancel() }
        }

    }

    private fun chooseDokumenLainnya() {
        requestCode = Constants.REQUEST_CODE_DOKUMEN_LAINNYA
        startFileChooser(launcherIntentFileChooser)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILNamaDokumen.editText -> {
                val strNamaDokumen = editText?.text.toString()
                binding.textILNamaDokumen.error = strNamaDokumen.getError(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        const val TAG = "UploadDokumenDialogFragment"
    }
}
