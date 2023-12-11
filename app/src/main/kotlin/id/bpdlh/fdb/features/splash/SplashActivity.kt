package id.bpdlh.fdb.features.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseActivity
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.LOGGED_IN
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.databinding.ActivitySplashBinding
import id.bpdlh.fdb.features.home.HomeActivity
import id.bpdlh.fdb.features.login.LoginActivity

class SplashActivity : BaseActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val versionName = applicationContext.packageManager
            .getPackageInfo(applicationContext.packageName, 0).versionName

        binding.tvVersionName.text = getString(R.string.version_name, versionName)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLoggedIn = LocalPreferences(this).getValueBoolean(LOGGED_IN, false)
            if (isLoggedIn) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 1000)
    }
}