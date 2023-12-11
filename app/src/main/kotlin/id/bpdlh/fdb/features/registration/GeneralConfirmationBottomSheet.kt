package id.bpdlh.fdb.features.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.databinding.GeneralConfirmationBottomSheetBinding
import javax.inject.Inject
import id.bpdlh.fdb.core.R as CoreR

class GeneralConfirmationBottomSheet(
    @ConfirmationType private val mode: String = ConfirmationType.DRAFT,
    private val onClickSubmit: ((String) -> Unit)? = null
) : BaseDaggerBottomDialogFragment(), ViewModelOwner<RegistrasiPeroranganViewModel>,
    HasAndroidInjector {
    @Inject
    override lateinit var viewModel: RegistrasiPeroranganViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private var _binding: GeneralConfirmationBottomSheetBinding? = null

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

        _binding = GeneralConfirmationBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            btnClose.setOnClickListener { dialog?.cancel() }
            btnTidak.setOnClickListener { dialog?.cancel() }
            btnIyaSimpanDraft.setOnClickListener {
                // logic submit/simpan sebagai draf harusnya dilakukan
                // di activity/fragment yang manggil bottomsheet ini,
                // termasuk apakah activity/fragmentnya di-dismiss atau tidak
                onClickSubmit?.let { onClick -> onClick(mode) }
                dialog?.dismiss()
            }
            when (mode) {
                ConfirmationType.SAVE -> {
                    textView10.text = getString(R.string.pertanyaan_konfirmasi)
                    textView11.text = getString(R.string.keterangan_ubah_data)
                    btnIyaSimpanDraft.text = getString(R.string.submit)

                }
                ConfirmationType.LOGOUT -> {
                    textView10.text =
                        getString(R.string.apakah_anda_yakin_akan_keluar_dari_aplikasi)
                    textView11.text =
                        getString(R.string.keluar_dari_aplikasi_berarti_anda_berhenti_mengakses_aplikasi_dan_harus_login_kembali)
                    btnTidak.text = getString(R.string.ya_keluar)
                    btnTidak.setTextAppearance(CoreR.style.TextSM_SemiBold_3)
                    btnTidak.setBackgroundColor(requireContext().getColor(CoreR.color.error_600))
                    btnIyaSimpanDraft.text = getString(R.string.batal)
                    btnIyaSimpanDraft.setTextAppearance(CoreR.style.TextSM_SemiBold_2)
                    btnIyaSimpanDraft.background =
                        AppCompatResources.getDrawable(
                            requireContext(),
                            CoreR.drawable.btn_secondary_filled
                        )
                    btnIyaSimpanDraft.setOnClickListener { dialog?.cancel() }
                    btnTidak.setOnClickListener {
                        dialog?.dismiss()
                        onClickSubmit?.let { onClick -> onClick(mode) }
                    }
                }

                ConfirmationType.SERVICE -> {
                    textView10.text = getString(R.string.pertanyaan_konfirmasi_layanan)
                    textView11.text = getString(R.string.keterangan_lanjut)
                    btnIyaSimpanDraft.text = getString(R.string.lanjutkan)
                    btnTidak.text = getString(R.string.ubah_layanan)
                    btnIyaSimpanDraft.setOnClickListener {
                        dialog?.dismiss()
                        onClickSubmit?.let { onClick -> onClick(Constants.LANJUTKAN_LAYANAN) }
                    }
                    btnTidak.setOnClickListener {
                        dialog?.dismiss()
                        onClickSubmit?.let { onClick -> onClick(Constants.UBAH_LAYANAN) }
                    }
                }
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
        const val TAG = "GeneralConfirmationBottomSheet"
    }
}