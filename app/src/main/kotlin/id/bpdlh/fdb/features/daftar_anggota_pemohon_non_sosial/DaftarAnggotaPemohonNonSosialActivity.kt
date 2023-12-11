package id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.convertToIndonesianWords
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.databinding.ActivityDaftarAnggotaPemohonBinding
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import id.bpdlh.fdb.features.registration.RegistrasiPeroranganActivity
import javax.inject.Inject

class  DaftarAnggotaPemohonNonSosialActivity : BaseDaggerActivity(), ViewModelOwner<DaftarAnggotaPemohonNonSosialViewModel>, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var viewModel: DaftarAnggotaPemohonNonSosialViewModel
    private lateinit var progressDialog: CustomProgressDialog
    val dataDebiturList : MutableList<DataDebiturNonSosialEntity> = mutableListOf()

    private val binding by lazy { ActivityDaftarAnggotaPemohonBinding.inflate(layoutInflater) }
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private var addData: Boolean = true
    private var submit: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        addData = intent.getBooleanExtra(ADD_DATA, true)
        val memberApplicatiodId = intent.getStringExtra(MEMBER_APPLICATION_DETAIl)
        fetchData(memberApplicatiodId)
        initToolbar()
        initUI()
        observe(viewModel.memberApplicationResult, ::onGetMember)
        observe(viewModel.createApplicationResult, ::onMemberCreated)
        observe(viewModel.submitApplicationResult, ::onMemberSubmited)
        observe(viewModel.deleteApplicationResult, ::onMemberDeleted)
    }

    private fun initToolbar() {
        with(binding.incToolbar) {
            toolbar.title = getString(R.string.text_daftar_anggota_pemohon)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
        progressDialog = CustomProgressDialog(this)
        with(binding) {
            btnSaveDraft.gone()
            if (addData) {
                btnFinalisasiRegistrasi.visible()
                btnTambahDebitur.visible()
                btnSubmit.gone()
            } else {
                btnFinalisasiRegistrasi.gone()
                btnTambahDebitur.gone()
                btnSubmit.visible()
            }
            rvDebitur.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(this@DaftarAnggotaPemohonNonSosialActivity)
            }
            btnTambahDebitur.setOnClickListener {
                TambahDebiturNonSosialFragment.newInstance(
                    onClickAdd = { it1 ->
                        dataDebiturList.add(it1)
                        convertData()
                    },
                    type = TambahDebiturNonSosialFragment.Companion.TypeBottomSheet.NON_SOSIAL,
                ).show(supportFragmentManager, "TambahDebiturNonSosial")
            }
            btnFinalisasiRegistrasi.setOnClickListener {
                GeneralConfirmationBottomSheet(
                    ConfirmationType.SAVE,
                    ::onClickCreate
                ).show(supportFragmentManager, GeneralConfirmationBottomSheet.TAG)
            }
            btnSubmit.setOnClickListener {
                GeneralConfirmationBottomSheet(
                    ConfirmationType.SAVE,
                    ::onClickSubmit
                ).show(supportFragmentManager, GeneralConfirmationBottomSheet.TAG)
            }
        }
    }

    private fun onClickCreate(type: String) {
        if (type == "Save") {
            createApplication()
        } else {
            onBackPressed()
        }
    }

    private fun onClickSubmit(type: String) {
        if (type == "Save") {
            val memberApplicatiodId = intent.getStringExtra(MEMBER_APPLICATION_DETAIl)
            viewModel.submitApplication(memberApplicatiodId)
        } else {
            onBackPressed()
        }
    }

    private fun fetchData(memberApplicatiodId: String?) {
        viewModel.getMemberApplication(memberApplicatiodId)
    }

    private fun createApplication() {
        val userId = LocalPreferences(this).getValueString(Constants.USER_ID)
        viewModel.createApplication(userId, dataDebiturList)
    }

    private fun loadData(data: List<DataDebiturNonSosialEntity>) {
        var tempNilaiPemohon : Int = 0
        val items = data.mapIndexed { index, entity ->
            data[index].nilaiPermohonan?.let { it -> tempNilaiPemohon += it  }
            if (entity.isFinancingSubmittable != null) {
                if (entity.isFinancingSubmittable!!) {
                    submit = false
                }
            } else {
                submit = false
            }
            DebiturItemNonSosial.toViewItem(entity, {
                //? Lengkapi
                RegistrasiPeroranganActivity.start(
                    this,
                    entity.userId.orEmpty(),
                    dataDebitur = entity
                )
            }, {
                //? Edit
                TambahDebiturNonSosialFragment.newInstance(
                    groupApplicationMemberPost = entity,
                    onClickAdd = {
                        dataDebiturList[index] = it
                        convertData()
                    },
                    type = TambahDebiturNonSosialFragment.Companion.TypeBottomSheet.NON_SOSIAL,
                ).show(supportFragmentManager, "TambahDebiturNonSosial")
            }, {
                deleteMember(index)
            })
        }
        with(binding) {
            tvTotalNilaiPermohonanValue.text = currencyFormatter(tempNilaiPemohon.toLong(), true)
            tvTerbilangValue.text = convertToIndonesianWords(tempNilaiPemohon.toLong())
            binding.contentDesc.visible()
        }
        itemAdapter.set(items)
        if (submit) {
            with(binding) {
                btnSubmit.visible()
                btnSubmit.isEnabled = true
                btnFinalisasiRegistrasi.gone()
            }
        }
    }

    private fun convertData() {
        binding.btnFinalisasiRegistrasi.isEnabled = true
        loadData(dataDebiturList)
    }

    private fun deleteMember(index: Int) {
        if(dataDebiturList.isNotEmpty()) dataDebiturList.removeAt(index)
        convertData()
    }

    private fun onMemberCreated(state: ResultState<List<DataDebiturNonSosialEntity>>) {
        progressDialog.dismiss()
        when(state) {
            is ResultState.Success -> {
                this.showToast("Berhasil Menambahkan Data")
                addData = false
                dataDebiturList.clear()
                initUI()
                viewModel.getMemberApplication(state.data?.first()?.memberApplicationId)
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun onGetMember(state: ResultState<List<DataDebiturNonSosialEntity>>) {
        progressDialog.dismiss()
        when(state) {
            is ResultState.Success -> {
                state.data?.let { loadData(it) }
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun onMemberSubmited(state: ResultState<Any>) {
        progressDialog.dismiss()
        when(state) {
            is ResultState.Success -> {
                showToast(state.message.orEmpty())
                finish()
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }


    private fun onMemberDeleted(data: Any) {

    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {

        val MEMBER_APPLICATION_DETAIl = "member_application_id"
        val ADD_DATA = "add_data"
        fun start(context: Context, memberApplicatiodId: String? = null, addData: Boolean = false) {
            val intent = Intent(context, DaftarAnggotaPemohonNonSosialActivity::class.java)
            intent.putExtra(MEMBER_APPLICATION_DETAIl, memberApplicatiodId)
            intent.putExtra(ADD_DATA, addData)
            context.startActivity(intent)
        }
    }
}