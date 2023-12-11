package id.bpdlh.fdb.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


/**
 * BaseFragment
 * Base class for module's fragment
 */
abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity !is AppCompatActivity) {
            throw IllegalStateException("Host activity must extends from ${AppCompatActivity::class.java.simpleName}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getLayoutIfDefined(inflater, container)
    }

    private fun getLayoutIfDefined(inflater: LayoutInflater, container: ViewGroup?): View? {
        val layoutResId = getViewLayoutResId()
        return inflater.inflate(layoutResId, container, false)

    }

    protected open fun getViewLayoutResId(): Int {
        val layout = javaClass.annotations.find { it is ViewLayout } as? ViewLayout
        return layout?.value ?: View.NO_ID
    }
}
