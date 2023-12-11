package id.bpdlh.fdb.core.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setLayoutIfDefined() {
        val layoutResId = getViewLayoutResId()
        setContentView(layoutResId)

    }

    protected open fun getViewLayoutResId(): Int {
        val layout = javaClass.annotations.find { it is ViewLayout } as? ViewLayout
        return layout?.value ?: View.NO_ID
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
