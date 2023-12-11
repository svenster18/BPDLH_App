package id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial

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
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.databinding.FragmentPengajuanDaftarPemohonNonSosialBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial.DaftarAnggotaPemohonNonSosialActivity
import id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial.DetailDaftarAnggotaPemohonNonSosialActivity
import javax.inject.Inject

class PengajuanDaftarPemohonNonSosialFragment : BaseDaggerFragment(), HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<PengajuanDaftarPermohonanNonSosialViewModel> { factory }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>


    private lateinit var binding: FragmentPengajuanDaftarPemohonNonSosialBinding
    private var position = 0
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private var communicator: IPengajuanDaftarPermohonanNonSosialCommunicator?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IPengajuanDaftarPermohonanNonSosialCommunicator
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
        binding = FragmentPengajuanDaftarPemohonNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.getGroupApplicationEntity()) { data ->
            initUI()
            data.let {
                val items = filterData(data).map { entity ->
                    PengajuanDaftarPermohonanNonSosialItem.toViewItem(
                        entity,
                        { id ->
                            context?.let { DetailDaftarAnggotaPemohonNonSosialActivity.start(it) }
                        },
                        { id ->
                            context?.let { DaftarAnggotaPemohonNonSosialActivity.start(it, id) }
                        },
                        { id ->
                            context?.let { DaftarAnggotaPemohonNonSosialActivity.start(it, id) }
                        }
                    )
                }
                itemAdapter.set(items)
                setData()
            }
        }
    }

    private fun initUI() {
        binding.rvDataPemohon.apply {
            adapter = FastAdapter.with(itemAdapter)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun filterData(data: List<GroupApplicationEntity>): List<GroupApplicationEntity> {
        return when(position) {
            0 -> data.filter { it.status == Constants.STATUS_DRAFT }.toMutableList()
            1 -> data.filter { it.status == Constants.STATUS_ON_PROGRESS }.toMutableList()
            2 -> data.filter { it.status == Constants.STATUS_MENUNGGU_VERIFIKASI }.toMutableList()
            else -> mutableListOf()
        }
    }

    private fun setData() {
        if (itemAdapter.itemList.size() > 0) {
            binding.rvDataPemohon.visible()
            binding.groupEmpty.gone()
            communicator?.showTicker(true)
        } else {
            binding.rvDataPemohon.gone()
            binding.groupEmpty.visible()
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {

        const val POSITION = "position"

        fun newInstance(position: Int) =
            PengajuanDaftarPemohonNonSosialFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}