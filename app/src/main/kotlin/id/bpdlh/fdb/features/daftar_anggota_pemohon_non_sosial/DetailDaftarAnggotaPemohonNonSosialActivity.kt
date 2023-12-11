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
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.domain.entities.DaftarAnggotaPemohonNonSosialEntity
import id.bpdlh.fdb.databinding.ActivityDetailDaftarAnggotaPemohonBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel.DetailDaftarAnggotaPemohonViewModel
import javax.inject.Inject

class DetailDaftarAnggotaPemohonNonSosialActivity: BaseDaggerActivity(),
ViewModelOwner<DetailDaftarAnggotaPemohonViewModel>, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    override lateinit var viewModel: DetailDaftarAnggotaPemohonViewModel

    private val binding by lazy { ActivityDetailDaftarAnggotaPemohonBinding.inflate(layoutInflater) }

    private var itemAdapter = ItemAdapter<ViewItem<*>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initUI()
        observe(viewModel.detailDaftarAnggotaPemohonNonSosial, ::onDataFetched)
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
                layoutManager = LinearLayoutManager(this@DetailDaftarAnggotaPemohonNonSosialActivity)
            }

        }
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchDetailDaftarAnggotaPemohonNonSosial()
    }

    private fun onDataFetched(state: ResultState<DaftarAnggotaPemohonNonSosialEntity>) {
        when(state) {
            is ResultState.Loading -> {
                //show loading
            }
            is ResultState.Success -> {
                state.data?.let {
                    loadData(it)
                }
            }
            else -> {
                //show error
            }
        }
    }

    private fun loadData(data: DaftarAnggotaPemohonNonSosialEntity) {
        with(binding) {
            tvTanggalPengajuanAnggota.text = data.tanggalPengajuanAnggota
            tvTotalAnggotaDiajukan.text = getString(R.string.text_jumlah_orang, data.totalAnggotaDiajukan.toString())
            tvTanggalDisetujui.text = data.tanggalDisetujui
            tvTotalNilaiPermohonanValue.text =
                data.totalNilaipermohonan?.toLong()?.let { currencyFormatter(it, true) }
        }
        val items = data.daftarAnggota?.map { entity ->
            DetailDaftarAnggotaNonSosialItem.toViewItem(entity)
        }
        items?.let { itemAdapter.set(it) }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, DetailDaftarAnggotaPemohonNonSosialActivity::class.java)
            )
        }
    }
}