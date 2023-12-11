package id.bpdlh.fdb.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.databinding.ActivityHomeBinding
import id.bpdlh.fdb.features.profile.ProfileFragment
import javax.inject.Inject

class HomeActivity : BaseDaggerActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        initFragment()
    }

    private fun initFragment() {
        setCurrentFragment(HomeFragment())
        binding.bottomNavigation.selectedItemId = R.id.item_beranda
        binding.bottomNavigation.setOnItemSelectedListener {
            if (it.itemId == R.id.item_profil) {
                val data = viewModel.getGroupProfileEntity()
                setCurrentFragment(ProfileFragment.newInstance(data))
            } else {
                setCurrentFragment(HomeFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container, fragment)
            commit()
        }
    }

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        fun start(context: Context, intentFlag: Int) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                flags = intentFlag
            }
            context.startActivity(intent)
        }
    }
}