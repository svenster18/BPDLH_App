package id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial

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
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.databinding.ActivityPengajuanDaftarPermohonanNonSosialBinding
import id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial.DaftarAnggotaPemohonNonSosialActivity
import javax.inject.Inject

class PengajuanDaftarPermohonanNonSosialActivity : BaseDaggerActivity(), HasAndroidInjector, IPengajuanDaftarPermohonanNonSosialCommunicator {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PengajuanDaftarPermohonanNonSosialViewModel

    private val binding by lazy { ActivityPengajuanDaftarPermohonanNonSosialBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = CustomProgressDialog(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PengajuanDaftarPermohonanNonSosialViewModel::class.java]
        observe(viewModel.groupApplicationResult, ::onGroupApplicationFetched)
        initToolbar()
        initUI()
    }

    private fun initToolbar() {
        with(binding.incToolbar) {
            toolbar.title = getString(R.string.pengajuan_daftar_permohonan)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        initUI()
//        observe(viewModel.groupApplicationResult, ::onGroupApplicationFetched)
    }

    private fun initUI() {
        progressDialog = CustomProgressDialog(this)
        with(binding) {
            tilSearchByDate.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilSearchByDate.editText as TextView, formatDate = Constants.DATE_FORMAT_DD_MM_YYYY)
                datePickerSpinner.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            btnAjukanAnggota.setOnClickListener {
                DaftarAnggotaPemohonNonSosialActivity.start(this@PengajuanDaftarPermohonanNonSosialActivity, addData = true)
            }
        }
        val userId = LocalPreferences(this).getValueString(Constants.USER_ID)
        viewModel.getGroupApplication(userId)
    }

    private fun setTabUI() {
        val listFragment = listOf(
            PengajuanDaftarPemohonNonSosialFragment.newInstance(0),
            PengajuanDaftarPemohonNonSosialFragment.newInstance(1),
            PengajuanDaftarPemohonNonSosialFragment.newInstance(2),
            PengajuanDaftarPemohonNonSosialFragment.newInstance(3)
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

    override fun showTicker(show: Boolean) {
        if (show) {
            binding.tickerDataAnggota.visible()
        } else {
            binding.tickerDataAnggota.gone()
        }
    }

    private fun onGroupApplicationFetched(state: ResultState<List<GroupApplicationEntity>>) {
        progressDialog.dismiss()
        when(state) {
            is ResultState.Success -> {
                viewModel.setGroupApplicationEntity(state.data)
                setTabUI()
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, PengajuanDaftarPermohonanNonSosialActivity::class.java)
            )
        }
    }
}