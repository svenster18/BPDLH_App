package id.bpdlh.fdb.features.permohonan_pembiayaan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.PERHUTANAN_SOSIAL
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import id.bpdlh.fdb.databinding.FragmentDaftarPemohonBinding
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan.IDaftarPermohonanCommunicator
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.JenisLayananBottomSheet
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.PembiayaanPerhutananItem
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import timber.log.Timber
import javax.inject.Inject

class PembiayaanPerhutananFragment : BaseDaggerFragment(),
    ViewModelOwner<PembiayaanPerhutananViewModel>, HasAndroidInjector {

    @Inject
    override lateinit var viewModel: PembiayaanPerhutananViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var binding: FragmentDaftarPemohonBinding
    private var position = 0
    private var itemAdapter = ItemAdapter<ViewItem<*>>()

    private var communicator: IDaftarPermohonanCommunicator? = null
    private lateinit var progressDialog: CustomProgressDialog


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
        initUi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        observe(viewModel.result, ::onDataFetched)
    }

    private fun initUi() {
        binding.rvDataPemohon.apply {
            adapter = FastAdapter.with(itemAdapter)
            layoutManager = LinearLayoutManager(requireContext())
        }
        fetchData()
    }

    private fun fetchData() {
        val userId = LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
        userId?.let {
            viewModel.fetchPembiayaanPermohonanPerhutananSosial(
                it,
                0,
                getPermohonanType(position)
            )
        }
    }

    private fun getPermohonanType(position: Int): Int {
        return when (position) {
            0 -> FdbBadge.DRAFT
            1 -> FdbBadge.ON_REVIEW
            2 -> FdbBadge.APPROVED
            else -> FdbBadge.REJECTED
        }
    }

    private fun onDataFetched(state: ResultState<List<PembiayaanPerhutananEntity>>) {
        Timber.d(state.toString())
        when (state) {
            is ResultState.Success -> {
                state.data?.let {
                    if (it[0].status == getPermohonanType(position)) {
                        Timber.d(it.toString())
                        val items = it.map { entity ->
                            PembiayaanPerhutananItem.toViewItem(entity) { data ->
                                onItemClicked(data)
                            }
                        }
                        itemAdapter.set(items)
                        loadFakeData()
                    }
                }
            }

            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.UnprocessableContent -> Unit

            else -> {
                //logic when fetch data failed
                context?.showToast("ResultState.NotSuccess")
            }
        }
    }

    private fun onItemClicked(data: PembiayaanPerhutananEntity) {
        if (data.serviceType == 0) {
            FormulirPermohonanActivity.start(
                requireContext(), PERHUTANAN_SOSIAL, data
            )
        } else {
            val generalConfirmationBottomSheet =
                GeneralConfirmationBottomSheet(ConfirmationType.SERVICE) { action ->
                    if (action == Constants.UBAH_LAYANAN) {
                        val jenisLayananBottomSheet =
                            JenisLayananBottomSheet { category ->
                                FormulirPermohonanActivity.start(requireContext(), category, data)
                            }
                        jenisLayananBottomSheet.show(
                            parentFragmentManager,
                            JenisLayananBottomSheet.TAG
                        )
                    } else if (action == Constants.LANJUTKAN_LAYANAN) {
                        FormulirPermohonanActivity.start(
                            requireContext(), when (data.serviceType) {
                                FdbBadge.TUNDA_TEBANG -> Constants.TUNDA_TEBANG
                                FdbBadge.HHBK -> Constants.HASIL_HUTAN_BUKAN_KAYU
                                FdbBadge.REFINANCING_KEHUTANAN -> Constants.NON_KEHUTANAN
                                else -> Constants.TUNDA_TEBANG
                            },
                            data
                        )
                    }
                }
            generalConfirmationBottomSheet.show(
                parentFragmentManager,
                GeneralConfirmationBottomSheet.TAG
            )
        }
    }

    private fun loadFakeData() {
        if (itemAdapter.itemList.size() > 0) {
            binding.rvDataPemohon.visible()
            binding.groupEmpty.gone()
            communicator?.showTicker(true)
        } else {
            communicator?.showTicker(false)
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
            PembiayaanPerhutananFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}