package id.bpdlh.fdb.features.registration.dokumen

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.databinding.FragmentDokumenLegalitasBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DokumenLahanItem
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import id.bpdlh.fdb.features.registration.RegistrasiPeroranganViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

class DokumenLegalitasFragment : BaseRegistrationFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<RegistrasiPeroranganViewModel> { factory }
    private lateinit var binding: FragmentDokumenLegalitasBinding
    private var requestCode = 0
    private var changePosition = -1
    private var swafotoFile: File? = null
    private var fotoRumahFile: File? = null
    private var fotoUsahaFile: File? = null
    private var dokumenLegalitasItemAdapter = ItemAdapter<ViewItem<*>>()
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null) {
                    when (requestCode) {
                        Constants.REQUEST_CODE_SWAFOTO -> {
                            swafotoFile = uriToFile(uri, requireContext())
                            if (swafotoFile != null) registrasiPerorangan.swafotoPath =
                                (swafotoFile as File).path
                            binding.cvSwafoto.showPreviewImage(uri, requireContext())
                        }

                        Constants.REQUEST_CODE_FOTO_RUMAH -> {
                            fotoRumahFile = uriToFile(uri, requireContext())
                            if (fotoRumahFile != null) registrasiPerorangan.fotoRumahPath =
                                (fotoRumahFile as File).path
                            binding.cvFotoRumah.showPreviewImage(uri, requireContext())
                        }

                        Constants.REQUEST_CODE_FOTO_USAHA -> {
                            fotoUsahaFile = uriToFile(uri, requireContext())
                            if (fotoUsahaFile != null) registrasiPerorangan.fotoUsahaPath =
                                (fotoUsahaFile as File).path
                            binding.cvFotoUsaha.showPreviewImage(uri, requireContext())
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDokumenLegalitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            if (cvSwafoto.ivFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_swafoto),
                    Constants.WARNING
                )
                return false
            }
            if (cvFotoRumah.ivFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_foto_rumah),
                    Constants.WARNING
                )
                return false
            }
            if (cvFotoUsaha.ivFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_foto_usaha),
                    Constants.WARNING
                )
                return false
            }
        }
        return true
    }

    override fun initUI() {
        binding.apply {
            showListDokumenLegalitas()
            cvSwafoto.showPreviewImage(registrasiPerorangan.swafotoPath, requireContext())
            cvFotoRumah.showPreviewImage(registrasiPerorangan.fotoRumahPath, requireContext())
            cvFotoUsaha.showPreviewImage(registrasiPerorangan.fotoUsahaPath, requireContext())
        }
    }

    private fun showListDokumenLegalitas() = runBlocking {
        var position = 0
        val items =
            dokumenLegalitasList.map { file ->
                DokumenLahanItem.toViewItem(
                    file, position++, { _, position ->
                        changePosition = position
                        onChangeClick(file, changePosition)
                    }, { position ->
                        onDelete(position)
                    }
                )
            }
        launch {
            dokumenLegalitasItemAdapter.set(items)
        }
    }

    private fun onChangeClick(fileEntity: FileEntity, position: Int) {
        val uploadDokumenDialogFragment = UploadDokumenDialogFragment(fileEntity) { updatedFile ->
            viewModel.updateFile(
                updatedFile.path, position, updatedFile.nama
            )
            showListDokumenLegalitas()
        }
        uploadDokumenDialogFragment.show(parentFragmentManager, UploadDokumenDialogFragment.TAG)
    }

    private fun onDelete(position: Int) {
        viewModel.removeFile(
            position
        )
        showListDokumenLegalitas()
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvUploadDokumen.apply {
                adapter = FastAdapter.with(dokumenLegalitasItemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnUploadDokumen.setOnClickListener {
                val uploadDokumenDialogFragment = UploadDokumenDialogFragment(onClick = { file ->
                    val userId =
                        LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
                    viewModel.insertFile(
                        file.path, file.nama, Constants.DOKUMEN_PENGUASAAN_LAHAN, userId.toString()
                    )
                    showListDokumenLegalitas()
                }
                )
                uploadDokumenDialogFragment.show(
                    childFragmentManager,
                    UploadDokumenDialogFragment.TAG
                )
            }
            cvSwafoto.apply {
                root.setOnClickListener {
                    chooseSwafoto()
                }
                clUploadFile.setOnClickListener {
                    chooseSwafoto()
                }
                tvGantiData.setOnClickListener {
                    chooseSwafoto()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvFotoRumah.apply {
                root.setOnClickListener {
                    chooseFotoRumah()
                }
                clUploadFile.setOnClickListener {
                    chooseFotoRumah()
                }
                tvGantiData.setOnClickListener {
                    chooseFotoRumah()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                }
            }
            cvFotoUsaha.apply {
                root.setOnClickListener {
                    chooseFotoUsaha()
                }
                clUploadFile.setOnClickListener {
                    chooseFotoUsaha()
                }
                tvGantiData.setOnClickListener {
                    chooseFotoUsaha()
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

    private fun chooseSwafoto() {
        requestCode = Constants.REQUEST_CODE_SWAFOTO
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseFotoRumah() {
        requestCode = Constants.REQUEST_CODE_FOTO_RUMAH
        startFileChooser(launcherIntentFileChooser)
    }

    private fun chooseFotoUsaha() {
        requestCode = Constants.REQUEST_CODE_FOTO_USAHA
        startFileChooser(launcherIntentFileChooser)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}