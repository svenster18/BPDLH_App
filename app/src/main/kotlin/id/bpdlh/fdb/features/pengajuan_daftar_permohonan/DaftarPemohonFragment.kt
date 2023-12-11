package id.bpdlh.fdb.features.pengajuan_daftar_permohonan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.databinding.FragmentDaftarPemohonBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon.DetailDaftarAnggotaPemohonActivity
import javax.inject.Inject

class DaftarPemohonFragment : BaseDaggerFragment(), HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<PengajuanDaftarPermohonanViewModel> { factory }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var binding: FragmentDaftarPemohonBinding
    private var position = 0
    private var itemAdapter = ItemAdapter<ViewItem<*>>()

    private var communicator: IDaftarPermohonanCommunicator?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IDaftarPermohonanCommunicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaftarPemohonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        itemAdapter.clear()
        observe(viewModel.getGroupApplicationList(), ::onDataFetched)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.rvDataPemohon.apply {
            adapter = FastAdapter.with(itemAdapter)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onDataFetched(data: List<DaftarPemohonEntity>) {
        val daftarPemohonList = data.filter { it.dataType == viewModel.getDataTypeByPosition(position) }
        if (daftarPemohonList.isNotEmpty()) {
            binding.rvDataPemohon.visible()
            binding.groupEmpty.gone()
            communicator?.showTicker(false)

            val items = daftarPemohonList.map { entity ->
                DaftarPermohonanItem.toViewItem(entity, {
                    onItemClicked(entity)
                }, { id ->
                    onDraftItemClicked(id)
                })
            }
            itemAdapter.add(items)
        } else {
            communicator?.showTicker(true)
            binding.rvDataPemohon.gone()
            binding.groupEmpty.visible()
        }
    }

    private fun onItemClicked(data: DaftarPemohonEntity) {
        context?.let { DetailDaftarAnggotaPemohonActivity.start(it, data) }
    }

    private fun onDraftItemClicked(dataId: String) {
        context?.showToast("DRAFT $dataId clicked")
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        const val POSITION = "position"

        fun newInstance(position: Int) =
            DaftarPemohonFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}