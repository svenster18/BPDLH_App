package id.bpdlh.fdb.features.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.convertISOTimeToDate
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.databinding.ActivityRegistrasiPeroranganBinding
import id.bpdlh.fdb.features.registration.alamat.AlamatFragment
import id.bpdlh.fdb.features.registration.dokumen.DokumenLegalitasFragment
import id.bpdlh.fdb.features.registration.keuangan.InformasiKeuanganFragment
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class RegistrasiPeroranganActivity : BaseDaggerActivity(),
    ViewModelOwner<RegistrasiPeroranganViewModel>, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var viewModel: RegistrasiPeroranganViewModel
    private val binding by lazy { ActivityRegistrasiPeroranganBinding.inflate(layoutInflater) }
    private val listStepFragment = listOf(
        GeneralInformationFragment(),
        DataPasanganFragment(),
        AlamatFragment(),
        InformasiKeuanganFragment(),
        DokumenLegalitasFragment()
    )
    private var step = 0
    private var fragment = listStepFragment[step]
    private lateinit var arrayDescription: Array<String>
    private lateinit var progressDialog: CustomProgressDialog
    private var userId: String? = null
    private var mode = ConfirmationType.DRAFT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = CustomProgressDialog(this)
        observe(viewModel.updatePermohonanPembiayaanResult, ::onDataSubmitted)
        observe(viewModel.savePermohonanPembiayaanDraftResult, ::onDraftSaved)
        observe(viewModel.ktpFileUploadResult, ::onFileUploaded)
        observe(viewModel.kkFileUploadResult, ::onFileUploaded)
        observe(viewModel.coupleKtpFileUploadResult, ::onFileUploaded)
        observe(viewModel.swaphotoKtpFileUploadResult, ::onFileUploaded)
        observe(viewModel.housePhotoFileUploadResult, ::onFileUploaded)
        observe(viewModel.businessPhotoFileUploadResult, ::onFileUploaded)
        arrayDescription = resources.getStringArray(R.array.description)
        BaseRegistrationFragment.status = intent.getIntExtra(STATUS, FdbBadge.DRAFT)
        userId = intent.getStringExtra(USER_ID)
        if (userId == "null") userId =
            LocalPreferences(this@RegistrasiPeroranganActivity).getValueString(Constants.USER_ID)
        else binding.menuFormulirPerorangan.btnSimpanDraft.gone()
        intent.parcelable<MemberApplicationEntity>(DATA)?.let {
            BaseRegistrationFragment.registrasiPerorangan.apply {
                groupName = it.groupName.orEmpty()
                nama = it.name.orEmpty()
                nik = it.ktp.orEmpty()
                tanggalLahir = it.date_of_birth.orEmpty()
                jenisKelamin = it.gender.orEmpty()
            }
        }
        intent.parcelable<DataDebiturNonSosialEntity>(DATA_DEBITUR)?.let {
            BaseRegistrationFragment.registrasiPerorangan.apply {
                groupName = it.namaKelompok.orEmpty()
                nama = it.nama.orEmpty()
                nik = it.nik.orEmpty()
                tanggalLahir = it.tanggalLahir.orEmpty().convertISOTimeToDate("dd/MM/yyyy")
                jenisKelamin = it.gender.orEmpty()
            }
        }
        //? Get Draft
        viewModel.getDraft()
        viewModel.resultDraft.observe(this) { draft ->
            if (draft != null) {
                BaseRegistrationFragment.registrasiPerorangan = draft
            }
            showFragment()
        }
        binding.apply {
            menuFormulirPerorangan.btnSimpanDraft.setOnClickListener {
                val generalConfirmationBottomSheet =
                    GeneralConfirmationBottomSheet(onClickSubmit = {
                        saveDraft()
                        mode = ConfirmationType.DRAFT
                        submit()
                    })
                generalConfirmationBottomSheet.show(
                    supportFragmentManager,
                    GeneralConfirmationBottomSheet.TAG
                )
            }

            menuFormulirPerorangan.btnSelanjutnya.setOnClickListener {
                if (step < listStepFragment.size - 1) {
                    if (!fragment.isValid()) return@setOnClickListener
                    step++
                    showFragment()
                } else {
                    if (!fragment.isValid()) return@setOnClickListener
                    val generalConfirmationBottomSheet =
                        GeneralConfirmationBottomSheet(ConfirmationType.SAVE) {
                            saveDraft()
                            mode = ConfirmationType.SAVE
                            submit()
                        }
                    generalConfirmationBottomSheet.show(
                        supportFragmentManager,
                        GeneralConfirmationBottomSheet.TAG
                    )
                }
            }
            menuFormulirPerorangan.btnKembali.setOnClickListener {
                if (step > 0) {
                    step--
                    showFragment()
                }
            }
            actionBar.btnBack.setOnClickListener { finish() }
        }
    }

    private fun submit() {
        userId?.let { id ->
            if (BaseRegistrationFragment.registrasiPerorangan.ktpPath.isNotEmpty()) {
                viewModel.uploadFile(
                    File(BaseRegistrationFragment.registrasiPerorangan.ktpPath),
                    Constants.REQUEST_CODE_KTP
                )
            }
            if (BaseRegistrationFragment.registrasiPerorangan.kkPath.isNotEmpty()) {
                viewModel.uploadFile(
                    File(BaseRegistrationFragment.registrasiPerorangan.kkPath),
                    Constants.REQUEST_CODE_KK
                )
            }
            if (BaseRegistrationFragment.registrasiPerorangan.ktpPathPasangan.isNotEmpty()) {
                viewModel.uploadFile(
                    File(BaseRegistrationFragment.registrasiPerorangan.ktpPathPasangan),
                    Constants.REQUEST_CODE_KTP_PASANGAN
                )
            }
            if (BaseRegistrationFragment.registrasiPerorangan.swafotoPath.isNotEmpty()) {
                viewModel.uploadFile(
                    File(BaseRegistrationFragment.registrasiPerorangan.swafotoPath),
                    Constants.REQUEST_CODE_SWAFOTO
                )
            }
            if (BaseRegistrationFragment.registrasiPerorangan.fotoRumahPath.isNotEmpty()) {
                viewModel.uploadFile(
                    File(BaseRegistrationFragment.registrasiPerorangan.fotoRumahPath),
                    Constants.REQUEST_CODE_FOTO_RUMAH
                )
            }
            if (BaseRegistrationFragment.registrasiPerorangan.fotoUsahaPath.isNotEmpty()) {
                viewModel.uploadFile(
                    File(BaseRegistrationFragment.registrasiPerorangan.fotoUsahaPath),
                    Constants.REQUEST_CODE_FOTO_USAHA
                )
            }
            if (BaseRegistrationFragment.registrasiPerorangan.ktpFile.isNotEmpty() &&
                BaseRegistrationFragment.registrasiPerorangan.kkFile.isNotEmpty() &&
                BaseRegistrationFragment.registrasiPerorangan.coupleKtpFile.isNotEmpty() &&
                BaseRegistrationFragment.registrasiPerorangan.swaphotoKtpFile.isNotEmpty() &&
                BaseRegistrationFragment.registrasiPerorangan.housePhotoFile.isNotEmpty() &&
                BaseRegistrationFragment.registrasiPerorangan.businessPhotoFile.isNotEmpty()
            ) {
                submitData()
            }
        }
    }

    private fun submitData() {
        userId?.let {
            if (mode == ConfirmationType.DRAFT) viewModel.saveFormulirPembiayaanNonPerhutananSosialDraft(
                it, BaseRegistrationFragment.registrasiPerorangan
            )
            else viewModel.submitFormulirPembiayaanNonPerhutananSosial(
                it, BaseRegistrationFragment.registrasiPerorangan
            )
        }
    }

    private fun onDataSubmitted(state: ResultState<MemberApplicationEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success -> {
//                pembiayaanPerhutananEntity?.let { viewModel.submitPermohonanPembiayaan(it.id) }
                showToast("Submit Permohonan Pembiayaan ${Constants.PERHUTANAN_SOSIAL} Berhasil")
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

    private fun onFileUploaded(state: ResultState<String>) {
        when (state) {
            is ResultState.Success -> submitData()
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            else -> showToast(state.message.toString(), Constants.WARNING)
        }
    }

    private fun showFragment() {
        val fragmentManager = supportFragmentManager
        fragment = listStepFragment[step]
        Timber.tag("MyFlexibleFragment")
            .d("Fragment Name :%s", fragment.javaClass.simpleName)
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
        binding.stepper.apply {
            setStep(step + 1)
            setDescription(arrayDescription[step])
        }
    }

    private fun saveDraft() {
        viewModel.getDraft()
        viewModel.resultDraft.observe(this) { draft ->
            if (draft != null) viewModel.update(draft)
            else viewModel.insert(BaseRegistrationFragment.registrasiPerorangan)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (step == 0) finish()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val TYPE = "type"
        private const val STATUS = "status"
        private const val DATA = "data"
        private const val DATA_DEBITUR = "data_debitur"
        fun start(
            context: Context,
            userId: String,
            type: String? = Constants.PERHUTANAN_SOSIAL,
            serviceType: String? = null,
            status: Int = FdbBadge.DRAFT,
            data: MemberApplicationEntity? = null,
            dataDebitur: DataDebiturNonSosialEntity? = null
        ) {
            val intent = Intent(context, RegistrasiPeroranganActivity::class.java)
            intent.putExtra(USER_ID, userId)
            intent.putExtra(TYPE, type)
            intent.putExtra(STATUS, status)
            Timber.d(data.toString())
            intent.putExtra(DATA, data)
            intent.putExtra(DATA_DEBITUR, dataDebitur)
            context.startActivity(intent)
        }
    }
}