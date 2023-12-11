package id.bpdlh.fdb.features.fpns

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
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
import id.bpdlh.fdb.core.common.utils.showToast
import java.io.File
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.databinding.ActivityRegistrationGroupNonPerhutaniBinding
import id.bpdlh.fdb.features.fdkns.BaseFormDaftarNonSosialFragment
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import timber.log.Timber
import javax.inject.Inject

class FormPermohonanNonSosialActivity : BaseDaggerActivity(), ViewModelOwner<FormPermohonanNonSosialViewModel>, HasAndroidInjector, IFormPermohonanKelompokNonSosialCommunicator {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    override lateinit var viewModel: FormPermohonanNonSosialViewModel

    private val binding by lazy { ActivityRegistrationGroupNonPerhutaniBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog
    private var isSubmit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = CustomProgressDialog(this)
        observe(viewModel.resultMemberApplication, ::observeMemberApplication)
        observe(viewModel.resultMemberApplicationUpdate, ::observeMemberUpdate)
        observe(viewModel.resultMemberApplicationDraft, ::observeMemberDraft)
        observe(viewModel.ijinLahanFileResult, ::observeIjinLahanFile)
        observe(viewModel.suratJualBeliFileResult, ::observeSuratJualBeliFile)
        observe(viewModel.spptFileResult, ::observeSpptFile)
        observe(viewModel.suratTanahFileResult, ::observeSuratTanahFile)
        observe(viewModel.fotoLahanFileResult, ::observeFotoLahanFile)
        observe(viewModel.dokumenLainnyaFileResult, ::observeDokumenLainnyaFile)
        observe(viewModel.labaRugiFileResult, ::observeLabaRugiFile)
        initUI()
        initToolbar()
    }

    private fun initToolbar() {
        with(binding.incAppbar) {
            toolbar.title = getString(R.string.formulir_permohonan)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
        val memberApplicationId = intent.getStringExtra(MEMBER_APPLICATION_ID)
        if (memberApplicationId != null) {
            BaseFormPermohonanNonSosialFragment.memberApplicationId = memberApplicationId
        }
        viewModel.getMemberApplication(memberApplicationId)
    }

    private fun replaceFragment(position: Int) {
        supportFragmentManager.beginTransaction().apply {
            val destination = when(position) {
                0 -> DataPermohonanNonSosialFragment()
                1 -> UsahaYangDibiayaiNonSosialFragment()
                2 -> DataJaminanNonSosialFragment()
                else -> DokumenLegalitasNonSosialFragment()
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
                    isSubmit = true
                    val memberApplicationId = intent.getStringExtra(MEMBER_APPLICATION_ID)
                    viewModel.saveDraft(memberApplicationId, BaseFormPermohonanNonSosialFragment.memberApplicationPost)
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
                    isSubmit = false
                    val memberApplicationId = intent.getStringExtra(MEMBER_APPLICATION_ID)
                    viewModel.saveDraft(memberApplicationId, BaseFormPermohonanNonSosialFragment.memberApplicationPost)
                }
            )
        generalConfirmationBottomSheet.show(
            supportFragmentManager,
            GeneralConfirmationBottomSheet.TAG
        )
    }

    private fun observeMemberApplication(state: ResultState<MemberApplicationDataEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<MemberApplicationDataEntity> -> {
                state.data?.let {
                    val memberApplicationPost: MemberApplicationPost = MemberApplicationPost(
                        applicationValue= it.amountOfLoan,
                        serviceType = it.serviceType,
                        timePeriod = it.timePeriod,
                        financingScheme = it.financingScheme,
                        warrantyplan = it.warrantyPlan,
                        amountOfLoan = it.amountOfLoan,
                        timePeriodUnit = it.timePeriodUnit,
                        businessType = it.businessType,
                        businessCommodity = it.businessCommodity,
                        businessDuration = it.businessDuration,
                        businessDurationUnit = it.businessDurationUnit,
                        productivity = it.productivity,
                        recentSale = it.recentSale,
                        landAreaCultivated = it.landAreaCultivated,
                        estimatedTurnover =  it.estimatedTurnover,
                        estimatedProductionCost = it.estimatedProductionCost,
                        estimatedNetIncome = it.estimatedNetIncome,
                        marketingObjective = it.marketingObjective,
                        businessManagementType = it.businessManagementType,
                        businessCycle = it.businessCycle,
                        businessCycleUnit = it.businessCycleUnit,
                        qtyBusinessCommodity = it.qtyBusinessCommodity,
                        submissionPurpose = it.submissionPurpose,
                        detailSubmissionPurpose = it.detailSubmissionPurpose,
                        financingCreatedIn = it.financingCreatedIn,
                        profitLossFile = it.profitLossFile,
                        collateralFile = it.collateralFile,
                        landTenureFile = it.landTenureFile,
                        landHistoryFile = it.landHistoryFile,
                        transferDeclarationFile = it.transferDeclarationFile,
                        spptFile = it.spptFile,
                        landPhotoFile = it.landPhotoFile,
                        managementActivityType = it.managementActivityType,
                        sourceOfProduction = it.sourceOfProduction,
                        businessCapacity = it.businessCapacity,
                        otherSupportingFile = it.otherSupportingFile
                    )
                    memberApplicationPost.landTenureFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_IJIN_LAHAN)
                    }
                    memberApplicationPost.landHistoryFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_SURAT_TANAH)
                    }
                    memberApplicationPost.transferDeclarationFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_SURAT_JUAL_BELI)
                    }
                    memberApplicationPost.otherSupportingFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_SURAT_JUAL_BELI)
                    }
                    memberApplicationPost.spptFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_SPPT)
                    }
                    memberApplicationPost.landPhotoFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_FOTO_LAHAN)
                    }
                    memberApplicationPost.profitLossFile?.let { it1 ->
                        viewModel.getSingleFile(it1, Constants.REQUEST_CODE_LAPORAN_LABA_RUGI)
                    }
                    BaseFormPermohonanNonSosialFragment.memberApplicationPost = memberApplicationPost
                    replaceFragment(0)
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
    private fun observeMemberDraft(state: ResultState<MemberApplicationDataEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<MemberApplicationDataEntity> -> {
                state.data?.let {
                    if(isSubmit) {
                        val memberApplicationId = intent.getStringExtra(MEMBER_APPLICATION_ID)
                        viewModel.saveUpdate(memberApplicationId, BaseFormPermohonanNonSosialFragment.memberApplicationPost)
                    } else {
                        finish()
                    }
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
    private fun observeMemberUpdate(state: ResultState<MemberApplicationDataEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<MemberApplicationDataEntity> -> {
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

    private fun observeLabaRugiFile(state: ResultState<GetFileSourceApi>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormPermohonanNonSosialFragment.labaRugiFileUrl = state.data?.url
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

    private fun observeDokumenLainnyaFile(state: ResultState<GetFileSourceApi>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
//                BaseFormPermohonanNonSosialFragment.labaRugiFileUrl = state.data?.url
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

    private fun observeFotoLahanFile(state: ResultState<GetFileSourceApi>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormPermohonanNonSosialFragment.fotoLahanFileUrl = state.data?.url
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

    private fun observeSuratTanahFile(state: ResultState<GetFileSourceApi>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormPermohonanNonSosialFragment.suratTanahFileUrl = state.data?.url
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

    private fun observeSpptFile(state: ResultState<GetFileSourceApi>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormPermohonanNonSosialFragment.spptFileFileUrl = state.data?.url
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

    private fun observeSuratJualBeliFile(state: ResultState<GetFileSourceApi>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormPermohonanNonSosialFragment.suratJualBeliFileUrl = state.data?.url
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

    private fun observeIjinLahanFile(state: ResultState<GetFileSourceApi>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<GetFileSourceApi> -> {
                BaseFormPermohonanNonSosialFragment.ijinLahanFileUrl = state.data?.url
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

       val MEMBER_APPLICATION_ID = "member_application_id"
        fun start(context: Context, memberApplicationId: String) {
            val intent = Intent(context, FormPermohonanNonSosialActivity::class.java)
            intent.putExtra(MEMBER_APPLICATION_ID, memberApplicationId)
            context.startActivity(intent)

        }
    }
}