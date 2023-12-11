package id.bpdlh.fdb.features.pengajuan_daftar_permohonan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewPagerAdapter
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.DATE_PICKER_TAG
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.databinding.ActivityPengajuanDaftarPermohonanListBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon.DaftarAnggotaPemohonActivity
import javax.inject.Inject

class PengajuanDaftarPermohonanListActivity : BaseDaggerActivity(), HasAndroidInjector,
     IDaftarPermohonanCommunicator {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PengajuanDaftarPermohonanViewModel
    private lateinit var progressDialog: CustomProgressDialog
    private val binding by lazy { ActivityPengajuanDaftarPermohonanListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[PengajuanDaftarPermohonanViewModel::class.java]
        initToolbar()
        initUi()
        setTabUI()
        fetchData()
        observe(viewModel.getGroupApplication(), ::onDataFetched)
    }

    private fun initToolbar() {
        with(binding.incToolbar) {
            toolbar.title = getString(R.string.pengajuan_daftar_permohonan)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUi() {
        progressDialog = CustomProgressDialog(this)
        with(binding) {
            tilSearchByDate.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilSearchByDate.editText as TextView, formatDate = Constants.DATE_FORMAT_DD_MM_YYYY)
                datePickerSpinner.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            btnAjukanAnggota.setOnClickListener {
                DaftarAnggotaPemohonActivity.start(this@PengajuanDaftarPermohonanListActivity)
            }
        }
    }

    private fun setTabUI() {
        val listFragment = listOf(
            DaftarPemohonFragment.newInstance(0),
            DaftarPemohonFragment.newInstance(1),
            DaftarPemohonFragment.newInstance(2),
            DaftarPemohonFragment.newInstance(3)
        )
        val viewPager = binding.vpPemohon
        val tabLayout = binding.tabPermohonan
        val adapter = ViewPagerAdapter(this, listFragment)
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = setTabTitle(position)
        }.attach()
    }

    private fun setTabTitle(position: Int) : String {
        return when(position) {
            0 -> getString(R.string.tab_draft)
            1 -> getString(R.string.tab_on_progress)
            2 -> getString(R.string.tab_menunggu_verifikasi)
            3 -> getString(R.string.tab_terverifikasi)
            else -> ""
        }
    }

    private fun fetchData() {
        val userId = LocalPreferences(this).getValueString(Constants.USER_ID)
        userId?.let {
            progressDialog.show()
            viewModel.fetchGroupApplication(it)
        }
    }

    private fun onDataFetched(state: ResultState<List<DaftarPemohonEntity>>) {
        when(state) {
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.Success -> {
                progressDialog.dismiss()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> Unit
        }
    }

    override fun showTicker(show: Boolean) {
        if (show) {
            binding.tickerDataAnggota.visible()
        } else {
            binding.tickerDataAnggota.gone()
        }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, PengajuanDaftarPermohonanListActivity::class.java)
            )
        }
    }
}