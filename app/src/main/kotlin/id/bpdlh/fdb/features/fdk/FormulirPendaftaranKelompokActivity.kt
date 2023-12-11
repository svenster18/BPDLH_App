package id.bpdlh.fdb.features.fdk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.databinding.ActivityFormulirPendaftaranKelompokBinding
import javax.inject.Inject

class FormulirPendaftaranKelompokActivity : BaseDaggerActivity(), HasAndroidInjector, IFormulirPendaftaranKelompokCommunicator {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FormulirPendaftaranKelompokViewModel

    private val binding by lazy { ActivityFormulirPendaftaranKelompokBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[FormulirPendaftaranKelompokViewModel::class.java]

        val data: GroupProfileEntity? = intent.parcelable("data")
        viewModel.setGroupProfile(data)
        initToolbar()
        initUI()
    }

    private fun initToolbar() {
        with(binding.incAppbar) {
            toolbar.title = getString(R.string.formulir_pendaftaran_kelompok)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
        replaceFragment(0)
        with(binding) {

        }
    }

    private fun replaceFragment(position: Int) {
        supportFragmentManager.beginTransaction().apply {
            val destination = when(position) {
                0 -> InformasiKelompokFragment()
                1 -> InformasiKepengurusanKelompokFragment()
                2 -> DetailKegiatanKelompokFragment()
                else -> InformasiKelompokFragment()
            }
            replace(R.id.fl, destination)
            commit()
        }
    }

    override fun goToPage(page: Int) {
        replaceFragment(page)
    }

    override fun onSubmit() {
        //TODO: submit data from viewmodel
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context, data: GroupProfileEntity? = null) {
            val intent = Intent(context, FormulirPendaftaranKelompokActivity::class.java)
            intent.putExtra("data", data)
            context.startActivity(intent)

        }
    }
}