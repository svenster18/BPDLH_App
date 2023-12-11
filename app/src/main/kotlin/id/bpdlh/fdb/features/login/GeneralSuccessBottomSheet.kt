package id.bpdlh.fdb.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.invisible
import id.bpdlh.fdb.core.databinding.GeneralSuccessBottomSheetBinding
import javax.inject.Inject
import id.bpdlh.fdb.core.R as CoreR

class GeneralSuccessBottomSheet(
    private val onClose: (() -> Unit)? = null,
    private val title: String,
    private val description: String? = null,
) : BaseDaggerBottomDialogFragment(),
    HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private var _binding: GeneralSuccessBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, CoreR.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GeneralSuccessBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            tvTitle.text = title
            if (description != null){
                tvDescription.text = description
            } else {
                tvDescription.invisible()
            }
            btnClose.setOnClickListener {
                onClose ->
                onClose?.let {  }
            }
            btnConfirm.setOnClickListener {
                onClose?.let { onClick -> onClick() }
                dialog?.cancel()
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "GeneralSuccessBottomSheet"
    }
}