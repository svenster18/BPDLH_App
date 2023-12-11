package id.bpdlh.fdb.features.fdkns

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
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.showToast
import java.io.File
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.databinding.ActivityRegistrationGroupNonPerhutaniBinding
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import timber.log.Timber
import javax.inject.Inject

class FormulirPendaftaranKelompokNonSosialActivity : BaseDaggerActivity(), ViewModelOwner<FormKelompokNonSosialViewModel>, HasAndroidInjector, IFormulirPendaftaranKelompokNonSosialCommunicator {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    override lateinit var viewModel: FormKelompokNonSosialViewModel

    private val binding by lazy { ActivityRegistrationGroupNonPerhutaniBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog
    private var isSubmit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = CustomProgressDialog(this)
        observe(viewModel.resultPostDraft, ::observePostDraft)
        observe(viewModel.resultPostUpdate, ::observePostSubmit)
        observe(viewModel.adArtFileResult, ::observeAdArtFile)
        observe(viewModel.suratPendampingFileResult, ::observeSuratPendampingFile)
        observe(viewModel.skBeritaAcaraFileResult, ::observeBeritaAcaraFile)
        val data: GroupProfileEntity? = intent.parcelable("data")
        data?.let {
            loadDraft(it)
            BaseFormDaftarNonSosialFragment.listActivities.value = it.listActivityEntity
            BaseFormDaftarNonSosialFragment.listMitraUsaha.value = it.listPartnerName
            BaseFormDaftarNonSosialFragment.listGambaranUmum.value = it.listGeneralDescription
        }
        initToolbar()
    }

    private fun initToolbar() {
        with(binding.incAppbar) {
            toolbar.title = getString(R.string.formulir_pendaftaran_kelompok_non_sosial)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
    }

    private fun replaceFragment(position: Int) {
        supportFragmentManager.beginTransaction().apply {
            val destination = when(position) {
                0 -> InformasiKelompokNonSosialFragment()
                1 -> InformasiKepengurusanKelompokNonSosialFragment()
                2 -> DetailKegiatanKelompokNonSosialFragment()
                else -> DokumenLegalitasKelompokNonSosialFragment()
            }
            replace(R.id.fl, destination)
            commit()
        }
    }

    override fun goToPage(page: Int) {
        replaceFragment(page)
    }

    override fun onSubmit() {
        val generalConfirmationBottomSheet =
            GeneralConfirmationBottomSheet(
                mode = ConfirmationType.SAVE,
                onClickSubmit = {
                    val userId = LocalPreferences(this).getValueString(Constants.USER_ID)
                    BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listActivities = BaseFormDaftarNonSosialFragment.listActivities.value
                    BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listMitraUsaha = BaseFormDaftarNonSosialFragment.listMitraUsaha.value
                    BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listGambaranUmum = BaseFormDaftarNonSosialFragment.listGambaranUmum.value
                    viewModel.draft(
                        BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost,
                        userId
                    )
                    isSubmit = true
                }
            )
        generalConfirmationBottomSheet.show(
            supportFragmentManager,
            GeneralConfirmationBottomSheet.TAG
        )
    }

    override fun onSavDraft() {
        val generalConfirmationBottomSheet =
            GeneralConfirmationBottomSheet(
                onClickSubmit = {
                    val userId = LocalPreferences(this).getValueString(Constants.USER_ID)
                    BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listActivities = BaseFormDaftarNonSosialFragment.listActivities.value
                    BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listMitraUsaha = BaseFormDaftarNonSosialFragment.listMitraUsaha.value
                    BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listGambaranUmum = BaseFormDaftarNonSosialFragment.listGambaranUmum.value
                    viewModel.draft(BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost, userId)
                }
            )
        generalConfirmationBottomSheet.show(
            supportFragmentManager,
            GeneralConfirmationBottomSheet.TAG
        )
    }

    private fun loadDraft(groupProfileEntity: GroupProfileEntity) {
        Timber.d("Grouprofile Type : $groupProfileEntity")
        val formulirKelompokNonSosialPost = FormulirKelompokNonSosialPost(
            name = groupProfileEntity.name,
            kpsName = groupProfileEntity.kupsName,
            sk = groupProfileEntity.sk,
            province = groupProfileEntity.province,
            city = groupProfileEntity.city,
            district = groupProfileEntity.district,
            village = groupProfileEntity.village,
            address = groupProfileEntity.address,
            leaderName = groupProfileEntity.leaderName,
            leaderKtp = groupProfileEntity.leaderKtp,
            leaderPhoneNumber = groupProfileEntity.leaderPhoneNumber,
            leaderGender = groupProfileEntity.leaderGender,
            secretaryName = groupProfileEntity.secretaryName,
            secretaryPhoneNumber = groupProfileEntity.secretaryPhoneNumber,
            secretaryGender = groupProfileEntity.secretaryGender,
            treasurerName = groupProfileEntity.treasurerName,
            treasurerPhoneNumber = groupProfileEntity.treasurerPhoneNumber,
            treasurerGender = groupProfileEntity.treasurerGender,
            companionName = groupProfileEntity.companionName,
            companionStatus = groupProfileEntity.status,
            companionPhoneNumber = groupProfileEntity.companionPhoneNumber,
            companionGender = groupProfileEntity.companionGender,
            administrationDeadlineDate = groupProfileEntity.administrationDeadlineDate,
            amountOfMember = groupProfileEntity.amountOfMember,
            amountOfMemberLand = groupProfileEntity.amountOfMemberLand,
            amountOfMemberLandArea = groupProfileEntity.amountOfMemberLandArea,
            amountOfMemberSubmit = groupProfileEntity.amountOfMemberSubmit,
            amountOfMemberSubmitLand = groupProfileEntity.amountOfMemberSubmitLand,
            amountOfMemberSubmitLandArea = groupProfileEntity.amountOfMemberSubmitLandArea,
            createdIn = groupProfileEntity.createdIn,
            adFile = groupProfileEntity.adFile,
            skFile = groupProfileEntity.skFile,
            companionRecomendationFile = groupProfileEntity.companionRecomendationFile
        )
        groupProfileEntity.adFile?.let {
            viewModel.getSingleFile(it, Constants.REQUEST_CODE_DOKUMEN_AD_ART)
        }
        groupProfileEntity.skFile?.let {
            viewModel.getSingleFile(it, Constants.REQUEST_CODE_DOKUMEN_SK_BERITA_ACARA)
        }
        groupProfileEntity.companionRecomendationFile?.let {
            viewModel.getSingleFile(it, Constants.REQUEST_CODE_DOKUMEN_SURAT_PENDAMPING)
        }
        BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost = formulirKelompokNonSosialPost
        replaceFragment(0)
    }

    private fun observePostDraft(state: ResultState<GroupProfileNonSosial>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GroupProfileNonSosial> -> {
                state.data.let {
                    //? save to database
                    it?.let {
                        viewModel.insert(it)
                    }
                    if (isSubmit) {
                        val userId = LocalPreferences(this).getValueString(Constants.USER_ID)
                        BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listActivities = BaseFormDaftarNonSosialFragment.listActivities.value
                        BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listMitraUsaha = BaseFormDaftarNonSosialFragment.listMitraUsaha.value
                        BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost.listGambaranUmum = BaseFormDaftarNonSosialFragment.listGambaranUmum.value
                        viewModel.update(BaseFormDaftarNonSosialFragment.formulirKelompokNonSosialPost, userId)
                    } else {
                        finish()
                    }
                }
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observePostSubmit(state: ResultState<GroupProfileNonSosial>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GroupProfileNonSosial> -> {
                state.data?.let {
                    finish()
                }
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnknownError -> showToast(state.message.orEmpty())
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeAdArtFile(state: ResultState<GetFileSourceApi>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormDaftarNonSosialFragment.adFileUrl = state.data?.url
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnknownError -> showToast(state.message.orEmpty())
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeBeritaAcaraFile(state: ResultState<GetFileSourceApi>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormDaftarNonSosialFragment.skFileUrl = state.data?.url
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnknownError -> showToast(state.message.orEmpty())
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeSuratPendampingFile(state: ResultState<GetFileSourceApi>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormDaftarNonSosialFragment.companionRecomendationFileUrl = state.data?.url
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnknownError -> showToast(state.message.orEmpty())
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun checkFile(path: String?) : File? {
        return if (path != null && path != "-"){
            File(path)
        } else {
            null
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context, data: GroupProfileEntity? = null) {
            val intent = Intent(context, FormulirPendaftaranKelompokNonSosialActivity::class.java)
            intent.putExtra("data", data)
            context.startActivity(intent)

        }
    }
}