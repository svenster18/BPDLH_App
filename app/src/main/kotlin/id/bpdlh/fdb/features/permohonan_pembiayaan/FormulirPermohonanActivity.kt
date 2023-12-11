package id.bpdlh.fdb.features.permohonan_pembiayaan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertISOTimeToDate
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.post.PermohonanPembiayaanPerhutananSosialPost
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import id.bpdlh.fdb.databinding.ActivityFormulirPermohonanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DataJaminanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DataPermohonanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DokumenLahanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.UsahaHasilHutanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.UsahaTundaTebangFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.DataPermohonanPinjamanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.DataPermohonanPinjamanLanjutanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.InformasiKeuanganPembiayaanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.JaminanFragment
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import id.bpdlh.fdb.features.registration.DataPasanganFragment
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import id.bpdlh.fdb.features.registration.GeneralInformationFragment
import id.bpdlh.fdb.features.registration.alamat.AlamatFragment
import java.io.File
import javax.inject.Inject


class FormulirPermohonanActivity : BaseDaggerActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FormulirPermohonanViewModel

    private val binding by lazy { ActivityFormulirPermohonanBinding.inflate(layoutInflater) }
    private val perhutananSosialListFragment = listOf<BaseRegistrationFragment>(
        GeneralInformationFragment(),
        DataPasanganFragment(),
        AlamatFragment(),
        InformasiKeuanganPembiayaanFragment(),
        DataPermohonanPinjamanFragment(), DataPermohonanPinjamanLanjutanFragment(),
        JaminanFragment()
    )
    private val nonPerhutananSosialListFragment = arrayListOf(
        DataPermohonanFragment(),
        UsahaTundaTebangFragment(),
        DataJaminanFragment(),
        DokumenLahanFragment()
    )
    private var step = 0
    private lateinit var activeListFragment: List<BaseRegistrationFragment>
    private var category: String? = null
    private var pembiayaanPerhutananEntity: PembiayaanPerhutananEntity? = null
    private var dataDebiturEntity: DataDebiturEntity? = null
    private var fragment = perhutananSosialListFragment[step]
    private lateinit var arrayDescription: Array<String>
    private lateinit var progressDialog: CustomProgressDialog
    private var mode = ConfirmationType.DRAFT
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = CustomProgressDialog(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[FormulirPermohonanViewModel::class.java]
        observe(viewModel.updatePermohonanPembiayaanResult, ::onDataSubmitted)
        observe(viewModel.submitPermohonanPembiayaanResult, ::onApplicationSubmitted)
        observe(viewModel.savePermohonanPembiayaanDraftResult, ::onDraftSaved)
        observe(viewModel.ktpFileUploadResult, ::onFileUploaded)
        observe(viewModel.kkFileUploadResult, ::onFileUploaded)
        observe(viewModel.coupleKtpFileUploadResult, ::onFileUploaded)
        observe(viewModel.jaminanFileUploadResult, ::onFileUploaded)
        observe(viewModel.laporanLabaRugiFileUploadResult, ::onFileUploaded)
        observe(viewModel.dataJaminanFileUploadResult, ::onFileUploaded)
        observe(viewModel.ijinLahanFileUploadResult, ::onFileUploaded)
        observe(viewModel.suratTanahFileUploadResult, ::onFileUploaded)
        observe(viewModel.suratJualBeliFileResult, ::onFileUploaded)
        observe(viewModel.spptFileUploadResult, ::onFileUploaded)
        observe(viewModel.fotoLahanFileResult, ::onFileUploaded)
        viewModel.ktpFileResult.observe(this) { state ->
            when (state) {
                is ResultState.Success -> {
                    state.data?.let {
                        viewModel.formulirPembiayaanPerhutananSosial.ktpFileUrl = it.url
                        setContentView(binding.root)
                    }
                }

                is ResultState.Loading -> progressDialog.show()
                is ResultState.HideLoading -> progressDialog.dismiss()
                else -> {}
            }
        }
        viewModel.kkFileResult.observe(this) { state ->
            when (state) {
                is ResultState.Success -> {
                    state.data?.let {
                        viewModel.formulirPembiayaanPerhutananSosial.kkFileUrl = it.url
                        setContentView(binding.root)
                    }
                }

                is ResultState.Loading -> progressDialog.show()
                is ResultState.HideLoading -> progressDialog.dismiss()
                else -> {}
            }
        }
        viewModel.coupleKtpFileResult.observe(this) { state ->
            when (state) {
                is ResultState.Success -> {
                    state.data?.let {
                        viewModel.formulirPembiayaanPerhutananSosial.coupleKtpFileUrl = it.url
                        setContentView(binding.root)
                    }
                }

                is ResultState.Loading -> progressDialog.show()
                is ResultState.HideLoading -> progressDialog.dismiss()
                else -> {}
            }
        }
        initToolbar()

        category = intent.getStringExtra(Constants.EXTRA_CATEGORY)
        category?.let { viewModel.formulirPembiayaanNonPerhutananSosial.jenis_layanan = it }

        activeListFragment = when (category) {
            Constants.PERHUTANAN_SOSIAL -> perhutananSosialListFragment
            else -> {
                nonPerhutananSosialListFragment
            }
        }

        pembiayaanPerhutananEntity = intent.parcelable(Constants.EXTRA_PEMBIAYAAN_PERHUTANAN)
        dataDebiturEntity = intent.parcelable(Constants.EXTRA_DATA_DEBITUR)
        pembiayaanPerhutananEntity?.let {
            it.memberApplication?.let { permohonan ->
                viewModel.formulirPembiayaanPerhutananSosial = permohonan
                viewModel.getSingleFile(permohonan.ktpFileId, Constants.REQUEST_CODE_KTP)
                viewModel.getSingleFile(permohonan.kkFileId, Constants.REQUEST_CODE_KK)
                viewModel.getSingleFile(
                    permohonan.coupleKtpFileId,
                    Constants.REQUEST_CODE_KTP_PASANGAN
                )
                if (permohonan.ktpFileId.isEmpty() && permohonan.kkFileId.isEmpty() && permohonan.coupleKtpFileId.isEmpty()) setContentView(
                    binding.root
                )
            }
            if (it.status != FdbBadge.DRAFT) binding.menuFormulirPerorangan.btnSimpanDraft.gone()
        }

        dataDebiturEntity?.let {
            userId = it.userId
            viewModel.formulirPembiayaanPerhutananSosial = PermohonanPembiayaanPerhutananSosialPost(
                ktp = it.nik.orEmpty(),
                name = it.nama.orEmpty(),
                dateOfBirth = it.tanggalLahir.orEmpty().convertISOTimeToDate("dd/MM/yyyy"),
                businessPartners = emptyList(),
                guarantee = emptyList()
            )
            binding.menuFormulirPerorangan.btnSimpanDraft.gone()
        }
        if (userId == null) userId = LocalPreferences(this).getValueString(Constants.USER_ID)

        if (category != Constants.TUNDA_TEBANG) nonPerhutananSosialListFragment[1] =
            UsahaHasilHutanFragment()

        if (category == Constants.PERHUTANAN_SOSIAL) arrayDescription =
            resources.getStringArray(R.array.pembiayaan_perhutanan_description)
        else {
            binding.stepper.gone()
            binding.fourStepper.visible()
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)
            constraintSet.connect(
                R.id.frame_container,
                ConstraintSet.TOP,
                R.id.four_stepper,
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.applyTo(binding.root)
            arrayDescription =
                resources.getStringArray(R.array.pembiayaan_non_perhutanan_description)
        }
        viewModel.formulirPembiayaanNonPerhutananSosialResult.observe(this) { draft ->
            if (draft != null) {
                viewModel.formulirPembiayaanNonPerhutananSosial = draft
            }
            showFragment()
        }

        binding.apply {
            menuFormulirPerorangan.btnSimpanDraft.setOnClickListener {
                val generalConfirmationBottomSheet =
                    GeneralConfirmationBottomSheet(onClickSubmit = {
                        mode = ConfirmationType.DRAFT
                        submit()
                    })
                generalConfirmationBottomSheet.show(
                    supportFragmentManager,
                    GeneralConfirmationBottomSheet.TAG
                )
            }

            menuFormulirPerorangan.btnSelanjutnya.setOnClickListener {
                if (pembiayaanPerhutananEntity != null) {
                    if (step < activeListFragment.size - 1) {
                        if (pembiayaanPerhutananEntity?.status == FdbBadge.DRAFT) if (!fragment.isValid()) return@setOnClickListener
                        step++
                        showFragment()
                    } else {
                        if (pembiayaanPerhutananEntity?.status == FdbBadge.DRAFT) {
                            if (!fragment.isValid()) return@setOnClickListener
                            mode = ConfirmationType.SAVE
                            val generalConfirmationBottomSheet =
                                GeneralConfirmationBottomSheet(mode) {
                                    submit()
                                }
                            generalConfirmationBottomSheet.show(
                                supportFragmentManager,
                                GeneralConfirmationBottomSheet.TAG
                            )
                        } else {
                            finish()
                        }
                    }
                } else {
                    if (step < activeListFragment.size - 1) {
                        if (!fragment.isValid()) return@setOnClickListener
                        step++
                        showFragment()
                    } else {
                        if (!fragment.isValid()) return@setOnClickListener
                        mode = ConfirmationType.SAVE
                        val generalConfirmationBottomSheet =
                            GeneralConfirmationBottomSheet(mode) {
                                submit()
                            }
                        generalConfirmationBottomSheet.show(
                            supportFragmentManager,
                            GeneralConfirmationBottomSheet.TAG
                        )
                    }
                }
            }
            menuFormulirPerorangan.btnKembali.setOnClickListener {
                if (step > 0) {
                    step--
                    showFragment()
                }
            }
        }
    }

    private fun onDataSubmitted(state: ResultState<MemberApplicationEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success -> {
                pembiayaanPerhutananEntity?.let { viewModel.submitPermohonanPembiayaan(it.id) }
            }

            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            else -> showToast(state.message.toString(), Constants.WARNING)
        }
    }

    private fun onApplicationSubmitted(state: ResultState<DaftarPemohonEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success -> {
                showToast("Submit Permohonan Pembiayaan $category Berhasil")
                finish()
            }

            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            else -> showToast(state.message.toString(), Constants.WARNING)
        }
    }

    private fun onDraftSaved(state: ResultState<MemberApplicationEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success -> {
                showToast("Berhasil Simpan Draft")
                finish()
            }

            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            else -> showToast(state.message.toString(), Constants.WARNING)
        }
    }

    private fun submit() {
        userId?.let { id ->
            when (category) {
                Constants.PERHUTANAN_SOSIAL -> {
                    viewModel.formulirPembiayaanPerhutananSosial.ktpFile?.let {
                        viewModel.uploadFile(
                            it,
                            Constants.REQUEST_CODE_KTP
                        )
                    }
                    viewModel.formulirPembiayaanPerhutananSosial.kkFile?.let {
                        viewModel.uploadFile(
                            it,
                            Constants.REQUEST_CODE_KK
                        )
                    }
                    viewModel.formulirPembiayaanPerhutananSosial.coupleKtpFile?.let {
                        viewModel.uploadFile(
                            it,
                            Constants.REQUEST_CODE_KTP_PASANGAN
                        )
                    }
                    viewModel.jaminanList.forEachIndexed { index, jaminan ->
                        viewModel.uploadFile(
                            File(jaminan.photoPath),
                            Constants.REQUEST_CODE_JAMINAN,
                            index
                        )
                    }
                    if (viewModel.formulirPembiayaanPerhutananSosial.ktpFile == null &&
                        viewModel.formulirPembiayaanPerhutananSosial.kkFile == null &&
                        viewModel.formulirPembiayaanPerhutananSosial.coupleKtpFile == null &&
                        viewModel.jaminanList.isEmpty()
                    ) {
                        submitData()
                    }
                }

                else -> {
                    viewModel.formulirPembiayaanNonPerhutananSosialResult.observe(this) { draft ->
                        if (draft != null) viewModel.updateFormulirPembiayaanNonPerhutananDraft()
                        else viewModel.insertFormulirPembiayaanNonPerhutananDraft()
                        viewModel.insertAllMitra()
                        viewModel.insertAllFile()
                        viewModel.insertAllJaminan()
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.laporan_laba_rugi_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.laporan_laba_rugi_path),
                            Constants.REQUEST_CODE_LAPORAN_LABA_RUGI
                        )
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.data_jaminan_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.data_jaminan_path),
                            Constants.REQUEST_CODE_DATA_JAMINAN
                        )
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.ijin_lahan_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.ijin_lahan_path),
                            Constants.REQUEST_CODE_IJIN_LAHAN
                        )
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.surat_tanah_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.surat_tanah_path),
                            Constants.REQUEST_CODE_SURAT_TANAH
                        )
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.surat_jual_beli_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.surat_jual_beli_path),
                            Constants.REQUEST_CODE_SURAT_JUAL_BELI
                        )
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.sppt_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.sppt_path),
                            Constants.REQUEST_CODE_SPPT
                        )
                    }
                    if (viewModel.formulirPembiayaanNonPerhutananSosial.foto_lahan_path.isNotEmpty()) {
                        viewModel.uploadFile(
                            File(viewModel.formulirPembiayaanNonPerhutananSosial.foto_lahan_path),
                            Constants.REQUEST_CODE_FOTO_LAHAN
                        )
                    }
                    if (
                        viewModel.formulirPembiayaanNonPerhutananSosial.laporan_laba_rugi_path.isEmpty() &&
                        viewModel.formulirPembiayaanNonPerhutananSosial.data_jaminan_path.isEmpty() &&
                        viewModel.formulirPembiayaanNonPerhutananSosial.ijin_lahan_path.isEmpty() &&
                        viewModel.formulirPembiayaanNonPerhutananSosial.surat_tanah_path.isEmpty() &&
                        viewModel.formulirPembiayaanNonPerhutananSosial.surat_jual_beli_path.isEmpty() &&
                        viewModel.formulirPembiayaanNonPerhutananSosial.sppt_path.isEmpty() &&
                        viewModel.formulirPembiayaanNonPerhutananSosial.foto_lahan_path.isEmpty()
                    ) {
                        submitData()
                    }

                }
            }
        }
    }

    private fun onFileUploaded(state: ResultState<String>) {
        when (state) {
            is ResultState.Success -> submitData()
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            else -> showToast(state.message.toString(), Constants.WARNING)
        }
    }

    private fun submitData() {
        userId?.let {
            when (category) {
                Constants.PERHUTANAN_SOSIAL -> {
                    if (mode == ConfirmationType.DRAFT) viewModel.saveFormulirPembiayaanPerhutananSosialDraft(
                        it
                    )
                    else viewModel.submitFormulirPembiayaanPerhutananSosial(
                        it
                    )
                }

                else -> {
                    if (mode == ConfirmationType.DRAFT) viewModel.saveFormulirPembiayaanNonPerhutananSosialDraft(
                        it
                    )
                    else viewModel.submitFormulirPembiayaanNonPerhutananSosial(
                        it
                    )
                }
            }
        }
    }

    private fun showFragment() {
        val fragmentManager = supportFragmentManager
        fragment = activeListFragment[step]
        fragment.category = category
        fragment.pembiayaanPerhutananEntity = pembiayaanPerhutananEntity
        fragmentManager
            .beginTransaction().apply {
                setCustomAnimations(
                    androidx.navigation.ui.R.anim.nav_default_enter_anim,
                    androidx.navigation.ui.R.anim.nav_default_exit_anim,
                    androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                    androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
                )
                replace(
                    R.id.frame_container,
                    fragment,
                    fragment.javaClass.simpleName
                )
                addToBackStack(null)
                commit()
            }
        if (category == Constants.PERHUTANAN_SOSIAL) {
            binding.stepper.apply {
                setStep(step + 1)
                setDescription(arrayDescription[step])
            }
        } else {
            binding.fourStepper.apply {
                setStep(step + 1)
                setDescription(arrayDescription[step])
            }
        }
    }

    private fun initToolbar() {
        with(binding.incAppbar) {
            toolbar.title = getString(R.string.formulir_permohonan)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (step > 0) {
            step--
            showFragment()
        } else {
            finish()
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(
            context: Context,
            category: String,
            data: PembiayaanPerhutananEntity? = null,
            dataDebiturEntity: DataDebiturEntity? = null
        ) {
            val intent = Intent(context, FormulirPermohonanActivity::class.java)
            intent.putExtra(Constants.EXTRA_CATEGORY, category)
            intent.putExtra(Constants.EXTRA_PEMBIAYAAN_PERHUTANAN, data)
            intent.putExtra(Constants.EXTRA_DATA_DEBITUR, dataDebiturEntity)
            context.startActivity(intent)
        }
    }
}