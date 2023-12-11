package id.bpdlh.fdb.features.permohonan_pembiayaan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ViewPagerAdapter
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.EXTRA_PEMBIAYAAN_PERHUTANAN
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import id.bpdlh.fdb.databinding.ActivityPermohonanPembiayaanPerhutananBinding
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan.IDaftarPermohonanCommunicator
import javax.inject.Inject

class PermohonanPembiayaanActivity : BaseDaggerActivity(), HasAndroidInjector,
    IDaftarPermohonanCommunicator {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private val binding by lazy {
        ActivityPermohonanPembiayaanPerhutananBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initUi()
        setTabUI()
    }

    private fun initToolbar() {
        with(binding.incToolbar) {
            toolbar.title = getString(R.string.permohonan_pembiayaan)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUi() {
        with(binding) {
            tilSearchByDate.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(
                    tilSearchByDate.editText as TextView,
                    formatDate = Constants.DATE_FORMAT_DD_MM_YYYY
                )
                datePickerSpinner.show(supportFragmentManager, Constants.DATE_PICKER_TAG)
            }
        }
    }

    private fun setTabUI() {
        val listFragment = listOf(
            PembiayaanPerhutananFragment.newInstance(0),
            PembiayaanPerhutananFragment.newInstance(1),
            PembiayaanPerhutananFragment.newInstance(2),
            PembiayaanPerhutananFragment.newInstance(3)
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

    private fun setTabTitle(position: Int): String {
        return when (position) {
            0 -> getString(R.string.tab_draft)
            1 -> getString(R.string.menunggu_review)
            2 -> getString(R.string.disetujui)
            3 -> getString(R.string.ditolak)
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

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    companion object {
        fun start(
            context: Context,
            pembiayaanPerhutananEntity: PembiayaanPerhutananEntity? = null,
        ) {
            context.startActivity(
                Intent(context, PermohonanPembiayaanActivity::class.java).apply {
                    putExtra(EXTRA_PEMBIAYAAN_PERHUTANAN, pembiayaanPerhutananEntity)
                }
            )
        }
    }
}