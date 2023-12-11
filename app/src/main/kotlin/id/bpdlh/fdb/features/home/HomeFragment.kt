package id.bpdlh.fdb.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.PERHUTANAN_SOSIAL_TYPE
import id.bpdlh.fdb.core.common.utils.Constants.TYPE_KELOMPOK
import id.bpdlh.fdb.core.common.utils.Constants.TYPE_PERORANGAN
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.orDefault
import id.bpdlh.fdb.core.common.utils.orFalse
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.HomeSectionEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.databinding.FragmentHomeBinding
import id.bpdlh.fdb.features.fdk.FormulirPendaftaranKelompokActivity
import id.bpdlh.fdb.features.fdkns.FormulirPendaftaranKelompokNonSosialActivity
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan.PengajuanDaftarPermohonanListActivity
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial.PengajuanDaftarPermohonanNonSosialActivity
import id.bpdlh.fdb.features.permohonan_pembiayaan.PermohonanPembiayaanActivity
import id.bpdlh.fdb.features.registration.RegistrasiPeroranganActivity
import id.bpdlh.fdb.features.self_assesment.SelfAssesmentActivity
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseDaggerFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<HomeViewModel> { factory }

    private lateinit var binding: FragmentHomeBinding
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        progressDialog = CustomProgressDialog(requireContext())
        checkUserRole()
        observe(viewModel.getGroupProfile(), ::onGroupProfileFetched)
        observe(viewModel.getMemberApplication(), ::onMemberApplicationFetched)
    }


    private fun initUI(role: String?) {
        val fastAdapter = FastAdapter.with(itemAdapter)
        with(binding) {
            rvLayananLain.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = fastAdapter
            }
            btnLengkapi.setOnClickListener {
                if (role == TYPE_KELOMPOK) {
                    val data = viewModel.getGroupProfileEntity()
                    if (data?.type == Constants.NON_PERHUTANAN_SOSIAL_TYPE) {
                        FormulirPendaftaranKelompokNonSosialActivity.start(requireContext(), data)
                    } else {
                        FormulirPendaftaranKelompokActivity.start(requireContext(), data)
                    }
                } else {
                    val data = viewModel.getMemberApplicationEntity()
                    RegistrasiPeroranganActivity.start(
                        requireContext(), data?.userID.toString(), Constants.NON_PERHUTANAN_SOSIAL,
                        data?.serviceType, FdbBadge.ON_REVIEW
                    )
                }
            }
            btnUbah.setOnClickListener {
                if (role == TYPE_KELOMPOK) {
                    val data = viewModel.getGroupProfileEntity()
                    if (data?.type == Constants.NON_PERHUTANAN_SOSIAL_TYPE) {
                        FormulirPendaftaranKelompokNonSosialActivity.start(requireContext(), data)
                    } else {
                        FormulirPendaftaranKelompokActivity.start(requireContext(), data)
                    }
                } else {
                    val data = viewModel.getMemberApplicationEntity()
                    RegistrasiPeroranganActivity.start(
                        requireContext(), data?.userID.toString(), Constants.NON_PERHUTANAN_SOSIAL,
                        data?.serviceType, FdbBadge.DRAFT, data
                    )
                }
            }
        }
    }

    private fun checkUserRole() {
        val userId = LocalPreferences(requireContext()).getValueString(Constants.USER_ID)
        val role = LocalPreferences(requireContext()).getValueString(Constants.ROLE)
        viewModel.checkRole(role, userId)
        setHomeTitle(role)
        initUI(role)
    }

    private fun setHomeTitle(role: String?) {
        binding.incHeader.tvHeaderTitle.text =
            if (role == TYPE_KELOMPOK) getString(R.string.pendaftaran_kelompok) else getString(
                R.string.pendaftaran_perorangan
            )
    }

    private fun onGroupProfileFetched(state: ResultState<GroupProfileEntity>) {
        val role = LocalPreferences(requireContext()).getValueString(Constants.ROLE)
        initUI(role)
        progressDialog.dismiss()
        when (state) {
            is ResultState.Loading -> {
                binding.cardForm.gone()
                progressDialog.show()
            }

            is ResultState.Success -> showFormulirKelompok(state.data)
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> Unit //else ini akan selalu dipanggil terlebih dahulu, jadi lebih baik dibikin void aja?
        }
    }

    private fun onMemberApplicationFetched(state: ResultState<MemberApplicationEntity>) {
        val role = LocalPreferences(requireContext()).getValueString(Constants.ROLE)
        initUI(role)
        progressDialog.dismiss()
        when (state) {
            is ResultState.Loading -> {
                binding.cardForm.gone()
                progressDialog.show()
            }

            is ResultState.Success -> {
                showFormulirPerorangan(state.data)
                Timber.d(state.data.toString())
                binding.btnUbah.setOnClickListener {
                    RegistrasiPeroranganActivity.start(
                        requireContext(), "null", Constants.NON_PERHUTANAN_SOSIAL,
                        state.data?.serviceType, FdbBadge.DRAFT, state.data
                    )
                }
            }

            is ResultState.UnprocessableContent -> state.message?.let { context?.showToast(it) }
            else -> Unit
        }
    }

    private fun showFormulirPerorangan(data: MemberApplicationEntity?) {
        with(binding) {
            val status = data?.status.orEmpty()
            val isSubmitted = data?.isSubmitted.orFalse()
            val isSubmitable = data?.isSubmittable.orFalse()
            val serviceType = data?.serviceType
            if (serviceType == null) tvLayananLain.gone()
            serviceType?.let { cardForm.visible() }

            when {
                !isSubmitted && !isSubmitable -> {
                    serviceType?.let { tickerHome.visible() }
                    btnLengkapi.gone()
                    btnUbah.visible()
                    llBadge.setBadgeType(FdbBadge.WARNING)
                    llBadge.setMessage(getString(R.string.status_data_belum_lengkap))
                    llSection.gone()
                    setLayananLainContentList(show = serviceType == null)
                }

                !isSubmitted && isSubmitable -> {
                    serviceType?.let { tickerHome.visible() }
                    btnLengkapi.gone()
                    btnUbah.visible()
                    llBadge.setBadgeType(FdbBadge.SUCCESS)
                    llBadge.setMessage(getString(R.string.status_data_sudah_dilengkapi))
                    setLayananLainContentList(show = serviceType == null)
                }

                isSubmitted && isSubmitable -> {
                    when (status) {
                        Constants.STATUS_MENUNGGU_VERIFIKASI -> {
                            serviceType?.let { tickerHome.visible() }
                            tickerHome.setTitle(getString(R.string.ticker_title_dalam_proses_pengkajian))
                            tickerHome.setMessage(getString(R.string.ticker_message_dalam_proses_pengkajian))
                            btnLengkapi.visible()
                            btnLengkapi.text = getString(R.string.btn_lihat_detail)
                            btnUbah.gone()
                            llBadge.setBadgeType(FdbBadge.WAITING)
                            llBadge.setMessage(getString(R.string.status_menunggu_verifikasi))
                            setLayananLainContentList(show = serviceType == null)
                        }

                        Constants.STATUS_DITOLAK -> {
                            serviceType?.let { tickerHome.visible() }
                            tickerHome.setTitle(getString(R.string.ticker_title_kembali_upload_persyaratan))
                            tickerHome.setMessage(getString(R.string.ticker_message_kembali_upload_persyaratan))
                            llBadge.setBadgeType(FdbBadge.REJECTED)
                            llBadge.setMessage(getString(R.string.status_ditolak))
                            btnLengkapi.gone()
                            btnUbah.visible()
                            setLayananLainContentList(show = serviceType == null)
                        }

                        Constants.STATUS_DISETUJUI -> {
                            tickerHome.gone()
                            llBadge.setBadgeType(FdbBadge.SUCCESS)
                            llBadge.setMessage(getString(R.string.status_disetujui))
                            btnLengkapi.visible()
                            btnLengkapi.text = getString(R.string.btn_lihat_detail)
                            btnUbah.gone()
                            setLayananLainContentList(type = TYPE_PERORANGAN)
                        }
                    }
                }
            }
        }
    }

    private fun showFormulirKelompok(data: GroupProfileEntity?) {
        val kelompokType = data?.type.orDefault(PERHUTANAN_SOSIAL_TYPE)
        with(binding) {
            val status = data?.status.orEmpty()
            val isSubmitted = data?.isSubmitted.orFalse()
            val isSubmitable = data?.isSubmittable.orFalse()
            viewModel.setGroupProfileEntity(data)
            cardForm.visible()

            when {
                !isSubmitted && !isSubmitable -> {
                    tickerHome.visible()
                    btnLengkapi.visible()
                    btnUbah.gone()
                    llBadge.gone()
                    llSection.gone()
                }

                !isSubmitted && isSubmitable -> {
                    tickerHome.visible()
                    btnLengkapi.gone()
                    btnUbah.visible()
                    llBadge.visible()
                    llBadge.setBadgeType(FdbBadge.SUCCESS)
                    llBadge.setMessage(getString(R.string.status_data_sudah_dilengkapi))
                    setLayananLainContentList(type = kelompokType)
                }

                isSubmitted && isSubmitable -> {
                    when (status) {
                        Constants.STATUS_MENUNGGU_VERIFIKASI -> {
                            tickerHome.visible()
                            tickerHome.setTitle(getString(R.string.ticker_title_dalam_proses_pengkajian))
                            tickerHome.setMessage(getString(R.string.ticker_message_dalam_proses_pengkajian))
                            btnLengkapi.visible()
                            btnLengkapi.text = getString(R.string.btn_lihat_detail)
                            btnUbah.gone()
                            llBadge.setBadgeType(FdbBadge.WAITING)
                            llBadge.setMessage(getString(R.string.status_menunggu_verifikasi))
                            setLayananLainContentList(type = kelompokType)
                        }

                        Constants.STATUS_DITOLAK -> {
                            tickerHome.visible()
                            tickerHome.setTitle(getString(R.string.ticker_title_kembali_upload_persyaratan))
                            tickerHome.setMessage(getString(R.string.ticker_message_kembali_upload_persyaratan))
                            llBadge.setBadgeType(FdbBadge.REJECTED)
                            llBadge.setMessage(getString(R.string.status_ditolak))
                            btnLengkapi.gone()
                            btnUbah.visible()
                            setLayananLainContentList(show = false)
                        }

                        Constants.STATUS_DISETUJUI -> {
                            tickerHome.gone()
                            llBadge.setBadgeType(FdbBadge.SUCCESS)
                            llBadge.setMessage(getString(R.string.status_disetujui))
                            btnLengkapi.visible()
                            btnLengkapi.text = getString(R.string.btn_lihat_detail)
                            btnUbah.gone()
                            setLayananLainContentList(type = kelompokType)
                        }
                    }
                }
            }
        }
    }

    private fun setLayananLainContentList(type: String = TYPE_PERORANGAN, show: Boolean = true) {
        val sectionItemKelompok = listOf(
            if (type == PERHUTANAN_SOSIAL_TYPE) {
                HomeSectionEntity(
                    R.drawable.ic_pengajuan,
                    getString(R.string.pengajuan_daftar_permohonan)
                )
            } else {
                HomeSectionEntity(
                    R.drawable.ic_pengajuan_non_social,
                    getString(R.string.pengajuan_daftar_permohonan)
                )
            },
        )
        val sectionItemPerorangan = listOf(
            HomeSectionEntity(
                R.drawable.ic_self_assesment,
                getString(R.string.self_assesment_title)
            ),
            HomeSectionEntity(
                R.drawable.ic_permohonan_pembiayaan,
                getString(R.string.txt_permohonan_pembiayaan)
            ),
            HomeSectionEntity(
                R.drawable.ic_penawaran,
                getString(R.string.txt_penawaran)
            ),
            HomeSectionEntity(
                R.drawable.ic_perjanjian,
                getString(R.string.txt_perjanjian)
            ),
            HomeSectionEntity(
                R.drawable.ic_permohonan_pencairan,
                getString(R.string.txt_permohonan_pencairan)
            ),
            HomeSectionEntity(
                R.drawable.ic_pencairan,
                getString(R.string.txt_pencairan)
            ),
        )
        val items = if (type == TYPE_PERORANGAN) {
            sectionItemPerorangan.map {
                HomeSectionItem.toViewItem(it) { id ->
                    gotoMenu(id)
                }
            }
        } else {
            sectionItemKelompok.map {
                HomeSectionItem.toViewItem(it) { id ->
                    gotoMenu(id)
                }
            }
        }

        if (show) {
            binding.llSection.visible()
            itemAdapter.set(items)
        } else {
            binding.llSection.gone()
        }
    }

    private fun gotoMenu(id: Int) {
        when (id) {
            R.drawable.ic_self_assesment -> SelfAssesmentActivity.start(requireContext())
            R.drawable.ic_pengajuan -> PengajuanDaftarPermohonanListActivity.start(requireContext())
            R.drawable.ic_pengajuan_non_social -> PengajuanDaftarPermohonanNonSosialActivity.start(
                requireContext()
            )

            R.drawable.ic_permohonan_pembiayaan -> PermohonanPembiayaanActivity.start(requireContext())
            else -> requireContext().showToast("menu clicked")
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
