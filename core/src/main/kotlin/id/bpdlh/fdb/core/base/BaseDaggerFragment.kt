package id.bpdlh.fdb.core.base

import android.os.Bundle

/**
 * BaseFeatureFragment
 * Base class for module's fragment with dependency components attached
 */
abstract class BaseDaggerFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
    }

    protected abstract fun injectComponent()
}
