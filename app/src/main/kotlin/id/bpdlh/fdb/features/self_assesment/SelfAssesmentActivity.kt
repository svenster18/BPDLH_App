package id.bpdlh.fdb.features.self_assesment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.databinding.ActivitySelfAssesmentBinding
import javax.inject.Inject

class SelfAssesmentActivity : BaseDaggerActivity(),
    ViewModelOwner<SelfAssesmentViewModel>,
    HasAndroidInjector {

    @Inject
    override lateinit var viewModel: SelfAssesmentViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private val binding by lazy { ActivitySelfAssesmentBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()
        initUI()
    }

    private fun initToolbar() {
        with(binding.incAppbar) {
            toolbar.title = getString(R.string.self_assesment_title)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initUI() {
        progressDialog = CustomProgressDialog(this)
        binding.apply {
            setCurrentFragment(SelfAssesmentStartFragment())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SelfAssesmentActivity::class.java).apply {
            }
            context.startActivity(intent)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.self_assesment_content, fragment)
            commit()
        }
    }


}