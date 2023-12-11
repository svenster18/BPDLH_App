package id.bpdlh.fdb.features.registration.keuangan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.domain.entities.UsahaLainEntity
import id.bpdlh.fdb.databinding.TambahUsahaBottomSheetBinding
import timber.log.Timber
import javax.inject.Inject

class TambahUsahaBottomSheet(
    private val onClickSubmit: ((UsahaLainEntity) -> Unit),
    private val usahaLainEntity: UsahaLainEntity? = null
) : BaseDaggerBottomDialogFragment(), TextWatcherTextChange, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private var _binding: TambahUsahaBottomSheetBinding? = null
    private val siklusList = listOf("Minggu", "Bulan", "Tahun")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TambahUsahaBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            if (usahaLainEntity != null) {
                edtJenisUsaha.setText(usahaLainEntity.jenis)
                edtPerkiraanPendapatan.setText(
                    currencyFormatter(
                        usahaLainEntity.perkiraanPendapatan,
                        false
                    )
                )
                edtSiklusPendapatan.setText(usahaLainEntity.siklusPendapatan.toString())
                edtSiklusPendapatanAkhir.setText(usahaLainEntity.satuanSiklusPendapatan)
            }
            val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            textILJenisUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahUsahaBottomSheet)
            }
            textILPerkiraanPendapatan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahUsahaBottomSheet)
            }
            edtPerkiraanPendapatan.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanPendapatan = edtPerkiraanPendapatan.text.toString()
                if (strPerkiraanPendapatan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanPendapatan.extractNumber())
                        edtPerkiraanPendapatan.setText(strPerkiraanPendapatan.extractNumber())
                    } else edtPerkiraanPendapatan.setText(
                        currencyFormatter(
                            strPerkiraanPendapatan.toLong(),
                            true
                        )
                    )
                }
            }
            textILSiklusPendapatanAwal.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahUsahaBottomSheet)
            }
            val siklusAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                siklusList
            )
            edtSiklusPendapatanAkhir.setAdapter(siklusAdapter)
            edtSiklusPendapatanAkhir.isFocusable = false
            btnBatal.setOnClickListener { dialog?.cancel() }
            btnClose.setOnClickListener { dialog?.cancel() }
            btnTambahUsaha.setOnClickListener {
                if (isValid()) {
                    val jenisUsaha = edtJenisUsaha.text.toString()
                    val perkiraanPendapatan =
                        edtPerkiraanPendapatan.text.toString().extractNumber().toLong()
                    val siklusUsaha = edtSiklusPendapatan.text.toString().toInt()
                    val satuanSiklusUsaha = edtSiklusPendapatanAkhir.text.toString()
                    onClickSubmit(
                        UsahaLainEntity(
                            jenisUsaha,
                            perkiraanPendapatan,
                            siklusUsaha,
                            satuanSiklusUsaha
                        )
                    )
                    dialog?.dismiss()
                }
            }
        }
    }

    private fun isValid(): Boolean {
        binding.apply {
            val jenisUsahaError =
                textILJenisUsaha.editText?.text.toString().getGeneralError(requireContext())
            textILJenisUsaha.error(jenisUsahaError)
            if (jenisUsahaError != null) return false
            val perkiraanPendapatanError =
                textILPerkiraanPendapatan.editText?.text.toString().getError(requireContext())
            textILPerkiraanPendapatan.error(perkiraanPendapatanError)
            if (perkiraanPendapatanError != null) return false
            val siklusPendapatanError =
                textILSiklusPendapatanAwal.editText?.text.toString().getError(requireContext())
            textILSiklusPendapatanAwal.error(siklusPendapatanError)
            if (siklusPendapatanError != null) return false
            val satuanSiklusPendapatanError =
                textILSiklusPendapatanAkhir.editText?.text.toString().getError(requireContext())
            textILSiklusPendapatanAkhir.error(satuanSiklusPendapatanError)
            if (satuanSiklusPendapatanError != null) return false
        }
        return true
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILJenisUsaha.editText -> {
                val strJenisUsaha = editText?.text.toString()
                binding.textILJenisUsaha.error = strJenisUsaha.getGeneralError(requireContext())
            }

            binding.textILPerkiraanPendapatan.editText -> {
                val strPerkiraanPendapatan = editText?.text.toString()
                binding.textILPerkiraanPendapatan.error =
                    strPerkiraanPendapatan.getError(requireContext())
            }

            binding.textILSiklusPendapatanAwal.editText -> {
                val strSiklusPendapatan = editText?.text.toString()
                binding.textILSiklusPendapatanAwal.error =
                    strSiklusPendapatan.getError(requireContext())
            }

            binding.textILSiklusPendapatanAkhir.editText -> {
                val strSatuanSiklusPendapatan = editText?.text.toString()
                binding.textILSiklusPendapatanAkhir.error =
                    strSatuanSiklusPendapatan.getError(requireContext())
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
        const val TAG = "TambahUsahaBottomSheet"
    }
}
