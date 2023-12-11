package id.bpdlh.fdb.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

/**
 * BaseDaggerActivity
 * Base class for module's activity with dependency components attached
 */
abstract class BaseDaggerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    protected abstract fun injectComponent()
}
