package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.databinding.JenisLayananBottomSheetBinding
import javax.inject.Inject

class JenisLayananBottomSheet(private val onClick: ((String) -> Unit)) :
    BaseDaggerBottomDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private var _binding: JenisLayananBottomSheetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            JenisLayananBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            btnClose.setOnClickListener { dialog?.cancel() }
            btnBatal.setOnClickListener { dialog?.cancel() }
            cvTundaTebang.setOnClickListener {
                onClick(Constants.TUNDA_TEBANG)
                dialog?.dismiss()
            }
            cvHasilHutanBukanKayu.setOnClickListener {
                onClick(Constants.HASIL_HUTAN_BUKAN_KAYU)
                dialog?.dismiss()
            }
            cvKomoditasNonKehutanan.setOnClickListener {
                onClick(Constants.NON_KEHUTANAN)
                dialog?.dismiss()
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "JenisLayananBottomSheet"
    }
}