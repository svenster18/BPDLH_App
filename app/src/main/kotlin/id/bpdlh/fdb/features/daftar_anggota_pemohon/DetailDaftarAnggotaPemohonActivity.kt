package id.bpdlh.fdb.features.daftar_anggota_pemohon

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
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.databinding.ActivityDetailDaftarAnggotaPemohonBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel.DetailDaftarAnggotaPemohonViewModel
import javax.inject.Inject

/**
 * Created by hahn on 25/09/23.
 * Project: BPDLH App
 */
class DetailDaftarAnggotaPemohonActivity: BaseDaggerActivity(),
ViewModelOwner<DetailDaftarAnggotaPemohonViewModel>, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    override lateinit var viewModel: DetailDaftarAnggotaPemohonViewModel

    private val binding by lazy { ActivityDetailDaftarAnggotaPemohonBinding.inflate(layoutInflater) }

    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private var data: DaftarPemohonEntity? = null
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initUI()
        progressDialog = CustomProgressDialog(this)
        data = intent.parcelable(DATA)
        data?.let { viewModel.fetchDetailAnggota(it.id) }
        observe(viewModel.getListDaftarAnggota(), ::onDataFetched)

    }

    private fun initToolbar() {
        with(binding.incToolbar.toolbar) {
            title = context.getString(R.string.text_detail_daftar_anggota_pemohon)
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
        with(binding) {
            rvDetailDaftarAnggota.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(this@DetailDaftarAnggotaPemohonActivity)
            }
        }
    }

    private fun onDataFetched(state: ResultState<List<DataDebiturEntity>>) {
        when(state) {
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.Success -> {
                progressDialog.dismiss()
                state.data?.let {
                    loadData(data, it)
                }
            }
            else -> {
                progressDialog.dismiss()
            }
        }
    }

    private fun loadData(data: DaftarPemohonEntity?, listDaftarAnggota: List<DataDebiturEntity>) {
        with(binding) {
            tvTanggalPengajuanAnggota.text = data?.date?.convertStringDate(
                input = Constants.NEW_DATE_FORMAT,
                output = Constants.DATE_FORMAT
            )
            tvTotalAnggotaDiajukan.text =
                getString(R.string.text_jumlah_orang, data?.totalAnggota.toString())
            tvTanggalDisetujui.text = "-"
            tvTotalNilaiPermohonan.gone()
            tvTotalNilaiPermohonanValue.gone()
        }
        val items = listDaftarAnggota.map { entity ->
            DetailDaftarAnggotaItem.toViewItem(entity)
        }
        items.let { itemAdapter.set(it) }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {

        const val DATA = "data"
        fun start(context: Context, data: DaftarPemohonEntity) {
            val intent = Intent(context, DetailDaftarAnggotaPemohonActivity::class.java)
            intent.putExtra(DATA, data)
            context.startActivity(intent)
        }
    }
}