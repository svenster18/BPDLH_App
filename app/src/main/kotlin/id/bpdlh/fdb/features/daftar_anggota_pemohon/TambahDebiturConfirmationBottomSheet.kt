package id.bpdlh.fdb.features.daftar_anggota_pemohon

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.bpdlh.fdb.core.R
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.databinding.FragmentTambahDebiturConfirmationBottomSheetBinding

class TambahDebiturConfirmationBottomSheet: BaseDaggerBottomDialogFragment() {

    private var _binding: FragmentTambahDebiturConfirmationBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahDebiturConfirmationBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(clBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        initUI()
    }

    private fun initUI() {
        with(binding) {
            btnClose.setOnClickListener {
                dismiss()
            }
            btnMengerti.setOnClickListener {
                context?.showToast("Saya mengerti clicked")
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance() = TambahDebiturConfirmationBottomSheet()
    }

    override fun injectComponent() {

    }
}