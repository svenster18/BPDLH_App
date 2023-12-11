package id.bpdlh.fdb.features.registration_kelompok

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.databinding.ActivityRegistrasiKelompokBinding
import javax.inject.Inject

/**
 * Created by hahn on 03/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokActivity : BaseDaggerActivity(),
    ViewModelOwner<RegistrasiKelompokViewModel>, HasAndroidInjector, IRegistrasiCommunicator {

    @Inject
    override lateinit var viewModel: RegistrasiKelompokViewModel

    private val binding by lazy { ActivityRegistrasiKelompokBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initUI()
    }

    private fun initToolbar() {
        with(binding.incToolbar) {
            toolbar.title = getString(R.string.text_daftar)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
        replaceFragment(0)
        with(binding) {
            stepper.setStep(1)
            stepper.setDescription(getString(R.string.text_general_informasi))

        }
    }

    private fun replaceFragment(position: Int) {
        supportFragmentManager.beginTransaction().apply {
            val destination = when (position) {
                0 -> RegistrasiKelompokStep1Fragment.newInstance()
                1 -> RegistrasiKelompokStep2Fragment.newInstance()
                2 -> RegistrasiKelompokStep3Fragment.newInstance()
                else -> RegistrasiKelompokStep1Fragment.newInstance()
            }
            setCustomAnimations(
                androidx.navigation.ui.R.anim.nav_default_enter_anim,
                androidx.navigation.ui.R.anim.nav_default_exit_anim,
                androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
            )
            replace(R.id.frame_container, destination)
            commit()
        }
    }

    override fun gotoStep(step: Int) {
        replaceFragment(step - 1)
        with(binding) {
            stepper.setStep(step)
            when (step) {
                1 -> {
                    stepper.setDescription(getString(R.string.text_general_informasi))
                }

                2 -> {
                    stepper.setDescription(getString(R.string.informasi_narahubung))
                }

                else -> {
                    stepper.setDescription(getString(R.string.text_informasi_akun_kelompok))
                }
            }
        }
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, RegistrasiKelompokActivity::class.java)
            )
        }
    }
}