package id.bpdlh.fdb.features.daftar_anggota_pemohon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.orDefault
import id.bpdlh.fdb.core.common.utils.parcelableArrayListExtra
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.databinding.ActivityDaftarAnggotaPemohonSubmitBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel.DaftarAnggotaPemohonViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanActivity
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

class DaftarAnggotaPemohonSubmitActivity : BaseDaggerActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: DaftarAnggotaPemohonViewModel

    private val binding by lazy { ActivityDaftarAnggotaPemohonSubmitBinding.inflate(layoutInflater) }
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private lateinit var progressDialog: CustomProgressDialog
    private var userId: String? = null
    private var listDebiturEntity: List<DataDebiturEntity>? = null
    private var memberApplicationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        userId = LocalPreferences(this).getValueString(Constants.USER_ID)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, factory)[DaftarAnggotaPemohonViewModel::class.java]
        initToolbar()
        initUI()
        loadData()
        observe(viewModel.getSubmitDebiturResult(), ::onSubmitDataResult)
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
        with(binding) {
            rvDebitur.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(this@DaftarAnggotaPemohonSubmitActivity)
            }

            btnSubmit.setOnClickListener {
                GeneralConfirmationBottomSheet(
                    ConfirmationType.SAVE,
                    ::onClickSubmit
                ).show(supportFragmentManager, GeneralConfirmationBottomSheet.TAG)
            }
        }
    }

    private fun loadData() {
        listDebiturEntity = intent?.parcelableArrayListExtra(DATA)
        memberApplicationId = intent.getStringExtra(MEMBER_APPLICATION_ID)
        val items = listDebiturEntity?.map { entity ->
            DebiturItem.toViewItem(entity, {
                FormulirPermohonanActivity.start(
                    this,
                    Constants.PERHUTANAN_SOSIAL,
                    dataDebiturEntity = entity
                )
            }, {}, {}, true)
        }
        items?.let { itemAdapter.set(it) }
    }

    private fun onClickSubmit(type: String) {
        if (type == "Save") {
            memberApplicationId?.let {
                progressDialog.show()
                viewModel.submitData(it)
            }
        } else {
            onBackPressed()
        }
    }

    private fun onSubmitDataResult(state: ResultState<DaftarPemohonEntity>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.Success -> {
                showToast("Data berhasil disubmit")
                finish()
            }

            is ResultState.UnprocessableContent -> showToast(state.message.orDefault("Data gagal disubmit"))
            else -> {
                progressDialog.dismiss()
            }
        }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        private const val DATA = "data"
        private const val MEMBER_APPLICATION_ID = "member_application_id"
        fun start(
            context: Context,
            data: ArrayList<DataDebiturEntity>,
            memberApplicationId: String?
        ) {
            val intent = Intent(context, DaftarAnggotaPemohonSubmitActivity::class.java)
            intent.putParcelableArrayListExtra(DATA, data)
            intent.putExtra(MEMBER_APPLICATION_ID, memberApplicationId)
            context.startActivity(intent)
        }
    }
}