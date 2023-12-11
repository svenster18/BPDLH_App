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
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.orFalse
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.databinding.ActivityDaftarAnggotaPemohonBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon.TambahDebiturFragment.Companion.REQUEST_KEY_DEBITUR
import id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel.DaftarAnggotaPemohonViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanActivity
import javax.inject.Inject

class DaftarAnggotaPemohonActivity : BaseDaggerActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: DaftarAnggotaPemohonViewModel

    private val binding by lazy { ActivityDaftarAnggotaPemohonBinding.inflate(layoutInflater) }
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private lateinit var progressDialog: CustomProgressDialog
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory)[DaftarAnggotaPemohonViewModel::class.java]
        initToolbar()
        initUI()
        userId = LocalPreferences(this).getValueString(Constants.USER_ID)
        progressDialog = CustomProgressDialog(this)
        observe(viewModel.getDataDebitur(), ::loadData)
        observe(viewModel.getCreateDataResult(), ::onCreateDataResult)
        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_DEBITUR, this) { key, bundle ->
            val result: DataDebiturEntity? = bundle.parcelable(REQUEST_KEY_DEBITUR)
            if (bundle.getBoolean(TambahDebiturFragment.REQUEST_KEY_EDIT).orFalse()) {
                viewModel.updateDataDebitur(result)
            } else {
                viewModel.setDataDebitur(result)
            }
        }
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
            btnSaveDraft.gone()
            rvDebitur.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(this@DaftarAnggotaPemohonActivity)
            }
            btnTambahDebitur.setOnClickListener {
                TambahDebiturFragment.newInstance().show(supportFragmentManager, "TambahDebitur")
            }

            btnFinalisasiRegistrasi.setOnClickListener {
                onClickCreateData()
            }
        }
    }

    private fun onClickCreateData() {
        userId?.let {
            progressDialog.show()
            viewModel.createDataDebitur(it)
        }
    }

    private fun loadData(data: List<DataDebiturEntity>) {
        binding.btnFinalisasiRegistrasi.isEnabled = data.isNotEmpty()
        val items = data.map { entity ->
            DebiturItem.toViewItem(entity, {
                FormulirPermohonanActivity.start(
                    this,
                    Constants.PERHUTANAN_SOSIAL,
                    dataDebiturEntity = entity
                )
            }, {
                TambahDebiturFragment.newInstance(data = entity).show(supportFragmentManager, "TambahDebitur")
            }, {
                viewModel.deleteDebitur(entity)
            })
        }
        itemAdapter.set(items)
    }

    private fun onCreateDataResult(state: ResultState<List<DataDebiturEntity>>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.Success -> {
                viewModel.deleteAllData()
                val data = state.data
                val arraylist = arrayListOf<DataDebiturEntity>()
                val memberApplicationId = data?.get(0)?.memberApplicationId
                data?.forEach {
                    arraylist.add(it)
                }
                DaftarAnggotaPemohonSubmitActivity.start(
                    this@DaftarAnggotaPemohonActivity,
                    arraylist,
                    memberApplicationId
                )
                finish()
            }
            is ResultState.UnprocessableContent -> this.showToast(state.message.orEmpty())
            else -> Unit
        }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, DaftarAnggotaPemohonActivity::class.java))
        }
    }
}