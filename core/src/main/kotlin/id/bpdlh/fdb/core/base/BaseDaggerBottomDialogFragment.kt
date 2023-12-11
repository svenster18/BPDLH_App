package id.bpdlh.fdb.core.base

import android.os.Bundle

abstract class BaseDaggerBottomDialogFragment : BaseBottomDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
    }

    protected abstract fun injectComponent()
}
