package id.bpdlh.fdb.features.fpns

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.databinding.FragmentDataJaminanNonSosialBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DokumenLahanItem
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.JaminanItem
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.TambahJaminanBottomSheet
import id.bpdlh.fdb.features.registration.dokumen.UploadDokumenDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import javax.inject.Inject

class DataJaminanNonSosialFragment : BaseFormPermohonanNonSosialFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormPermohonanNonSosialViewModel> { factory }

    private lateinit var binding: FragmentDataJaminanNonSosialBinding
    private var communicator: IFormPermohonanKelompokNonSosialCommunicator? = null
    private var dokumenItemAdapter = ItemAdapter<ViewItem<*>>()
    private lateinit var progressDialog: CustomProgressDialog
    val dataJaminanPathList = arrayListOf<FileEntity>()
    private var jaminanAdapter = ItemAdapter<ViewItem<*>>()
    private val jaminanList = arrayListOf<JaminanEntity>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormPermohonanKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataJaminanNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        with(binding) {
            rvUploadDokumen.apply {
                adapter = FastAdapter.with(dokumenItemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            rvJaminanTambahan.apply {
                adapter = FastAdapter.with(jaminanAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        formWatcher()
    }

    private fun initUi() {
        with(binding) {
            tvDescription.text = getString(R.string.data_jaminan)
            tvStep.text = requireContext().getString(
                R.string.formulir_pendaftaran_kelompok_non_sosial_text_stepper,
                "3"
            )
            incFooter.btnKembali.setOnClickListener {
                communicator?.goToPage(1)
            }
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
            btnUploadJaminan.setOnClickListener {
                val uploadDokumenDialogFragment = UploadDokumenDialogFragment(onClick = { file ->
                    dataJaminanPathList.add(file)
                    showListDokumenJaminan()
                })
                uploadDokumenDialogFragment.show(
                    childFragmentManager,
                    UploadDokumenDialogFragment.TAG
                )
            }
            btnTambahJaminan.setOnClickListener {
                val tambahJaminanBottomSheet = TambahJaminanBottomSheet({ jaminan ->
                    jaminanList.add(jaminan)
                    showListJaminan()
                })
                tambahJaminanBottomSheet.show(parentFragmentManager, TambahJaminanBottomSheet.TAG)
            }
            memberApplicationPost.collateralFile?.let {
                dataJaminanPathList.add(FileEntity(path = it))
                showListDokumenJaminan()
            }
            fetchOtherDocument()
        }
    }

    private fun fetchOtherDocument() {
        viewModel.getDocumentDraft(memberApplicationId)
    }

    private fun showListDokumenJaminan() = runBlocking {
        var position = 0
        if(dataJaminanPathList.isNotEmpty()) memberApplicationPost.collateralFile = dataJaminanPathList.first().path
        val items =
            dataJaminanPathList.map { file ->
                DokumenLahanItem.toViewItem(
                    file, position++, { _, position ->
                        onChangeClick(file, position)
                    }, { position ->
                        onDelete(position)
                    }
                )
            }
        launch {
            dokumenItemAdapter.set(items)
        }
    }

    private fun showListJaminan() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var position = 0
        executor.execute {
            val items = jaminanList.map { jaminan ->
                JaminanItem.toViewItem(
                    jaminan, position++, { _, position ->
                        onChangeClickJaminan(jaminan, position)
                    }, {})
            }
            handler.post {
                jaminanAdapter.set(items)
            }
        }
    }

    private fun onDelete(index: Int) {
        dataJaminanPathList.removeAt(index)
        showListDokumenJaminan()
    }

    private fun onChangeClick(fileEntity: FileEntity, position: Int) {
        val uploadDokumenDialogFragment = UploadDokumenDialogFragment(fileEntity) { updatedFile ->
        }
        uploadDokumenDialogFragment.show(parentFragmentManager, UploadDokumenDialogFragment.TAG)
    }

    private fun onChangeClickJaminan(jaminan: JaminanEntity, position: Int) {
        val tambahJaminanBottomSheet = TambahJaminanBottomSheet({ updatedJaminan ->
            jaminanList[position] = updatedJaminan
            showListJaminan()
        }, jaminan)
        tambahJaminanBottomSheet.show(parentFragmentManager, TambahJaminanBottomSheet.TAG)
    }

    private fun formWatcher() {
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
        }
//        if (errorMessages.size == 0) {
        communicator?.goToPage(3)
//        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}