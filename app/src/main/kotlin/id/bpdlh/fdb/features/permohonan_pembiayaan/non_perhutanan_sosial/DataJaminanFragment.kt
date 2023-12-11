package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

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
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants.REQUEST_CODE_ADD_FILE
import id.bpdlh.fdb.core.common.utils.Constants.WARNING
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startExcelFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.databinding.FragmentDataJaminanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.JaminanItem
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.TambahJaminanBottomSheet
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

class DataJaminanFragment : BaseRegistrationFragment() {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }

    private var _binding: FragmentDataJaminanBinding? = null
    private val binding get() = _binding!!
    private var requestCode = 0
    private var dataJaminanFile: File? = null
    private var jaminanItemAdapter = ItemAdapter<ViewItem<*>>()
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null) {
                    val newFile = uriToFile(uri, requireContext())
                    if (newFile != null) {
                        dataJaminanFile = uriToFile(uri, requireContext())
                        dataJaminanFile?.let {
                            viewModel.formulirPembiayaanNonPerhutananSosial.data_jaminan_path =
                                it.path
                        }
                        binding.cvUploadDataJaminan.showPreviewImage(uri, requireContext())
                    }
                }
            }
        }
    }

    private fun chooseDataJaminan() {
        requestCode = REQUEST_CODE_ADD_FILE
        startExcelFileChooser(launcherIntentFileChooser)
    }

    override fun isValid(): Boolean {
        if (binding.cvUploadDataJaminan.ivFile.isGone && binding.cvUploadDataJaminan.clPreviewFile.isGone) {
            requireContext().showToast(getString(R.string.wajib_upload_data_jaminan), WARNING)
            return false
        }
        return true
    }

    override fun initUI() {
        binding.cvUploadDataJaminan.showPreviewImage(
            viewModel.formulirPembiayaanNonPerhutananSosial.data_jaminan_path,
            requireContext()
        )
        showJaminanList()
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDataJaminanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            cvUploadDataJaminan.tvFormat.text = getString(R.string.xls_xlsx_up_to_10mb)
            cvUploadDataJaminan.apply {
                root.setOnClickListener {
                    chooseDataJaminan()
                }
                clUploadFile.setOnClickListener {
                    chooseDataJaminan()
                }
                tvGantiData.setOnClickListener {
                    chooseDataJaminan()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
            rvTambahJaminan.apply {
                adapter = FastAdapter.with(jaminanItemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahJaminan.setOnClickListener {
                val tambahJaminanBottomSheet = TambahJaminanBottomSheet({ jaminan ->
                    viewModel.insertJaminan(
                        jaminan
                    )
                    showJaminanList()
                })
                tambahJaminanBottomSheet.show(parentFragmentManager, TambahJaminanBottomSheet.TAG)
            }
        }
    }

    private fun showJaminanList() = runBlocking {
        var position = 0
        val items = viewModel.jaminanList.map { jaminan ->
            JaminanItem.toViewItem(
                jaminan, position++, { _, position ->
                    onChangeClick(jaminan, position)
                },
                onDelete = { position ->
                    onDeleteJaminan(position)
                })
        }
        launch {
            jaminanItemAdapter.set(items)
        }
    }

    private fun onChangeClick(jaminan: JaminanEntity, position: Int) {
        val tambahJaminanBottomSheet = TambahJaminanBottomSheet({ updatedJaminan ->
            viewModel.updateJaminan(
                updatedJaminan,
                position
            )
            showJaminanList()
        }, jaminan)
        tambahJaminanBottomSheet.show(parentFragmentManager, TambahJaminanBottomSheet.TAG)
    }

    private fun onDeleteJaminan(position: Int) {
        viewModel.jaminanList.removeAt(position)
        showJaminanList()
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}