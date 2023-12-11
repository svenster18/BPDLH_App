package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.databinding.FragmentInformasiKeuanganPembiayaanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import timber.log.Timber
import javax.inject.Inject

class InformasiKeuanganPembiayaanFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private var _binding: FragmentInformasiKeuanganPembiayaanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformasiKeuanganPembiayaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            val perkiraanPendapatanError =
                textILPerkiraanPendapatanBulanTahun.editText?.text.toString()
                    .getError(requireContext())
            textILPerkiraanPendapatanBulanTahun.error(perkiraanPendapatanError)
            if (perkiraanPendapatanError != null) return false
            val siklusPendapatanError =
                textILSiklusPendapatanAwal.editText?.text.toString()
                    .getError(requireContext())
            textILSiklusPendapatanAwal.error(siklusPendapatanError)
            if (siklusPendapatanError != null) return false
            val satuanSiklusPendapatanError =
                textILSiklusPendapatanAkhir.editText?.text.toString()
                    .getError(requireContext())
            textILSiklusPendapatanAkhir.error(satuanSiklusPendapatanError)
            if (satuanSiklusPendapatanError != null) return false
            val pengeluaranRutinError =
                textILPengeluaranRutin.editText?.text.toString().getError(requireContext())
            textILPengeluaranRutin.error(pengeluaranRutinError)
            if (pengeluaranRutinError != null) return false
            val penggunaanTerbesarError =
                textILPenggunaanPengeluaranTerbesar.editText?.text.toString()
                    .getError(requireContext())
            textILPenggunaanPengeluaranTerbesar.error(penggunaanTerbesarError)
            if (penggunaanTerbesarError != null) return false
        }
        return true
    }

    override fun initUI() {
        binding.apply {
            val siklusPendapatanAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    resources.getStringArray(R.array.siklus_pendapatan)
                )
            edtSiklusPendapatanAkhir.setAdapter(siklusPendapatanAdapter)
            val formPembiayaan = viewModel.formulirPembiayaanPerhutananSosial
            if (formPembiayaan.income > 0L) edtPerkiraanPendapatanBulanTahun.setText(
                currencyFormatter(
                    formPembiayaan.income,
                    false
                )
            )
            if (formPembiayaan.incomeCycle > 0) edtSiklusPendapatan.setText(formPembiayaan.incomeCycle.toString())
            edtSiklusPendapatanAkhir.setText(formPembiayaan.incomeCycleUnit)
            if (formPembiayaan.expense > 0L) edtPengeluaranRutin.setText(
                currencyFormatter(
                    formPembiayaan.expense,
                    false
                )
            )
            if (formPembiayaan.largestExpense > 0L) edtPengeluaranTerbesar.setText(
                currencyFormatter(
                    formPembiayaan.largestExpense,
                    false
                )
            )
            edtPenggunaanPengeluaranTerbesar.setText(formPembiayaan.largestUseExpense)
        }

    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textILPerkiraanPendapatanBulanTahun.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganPembiayaanFragment)
            }
            edtPerkiraanPendapatanBulanTahun.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanPendapatan = edtPerkiraanPendapatanBulanTahun.text.toString()
                if (strPerkiraanPendapatan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanPendapatan.extractNumber())
                        edtPerkiraanPendapatanBulanTahun.setText(strPerkiraanPendapatan.extractNumber())
                    } else edtPerkiraanPendapatanBulanTahun.setText(
                        currencyFormatter(
                            strPerkiraanPendapatan.toLong(),
                            false
                        )
                    )
                }
            }
            textILSiklusPendapatanAwal.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganPembiayaanFragment)
            }
            textILSiklusPendapatanAkhir.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganPembiayaanFragment)
            }
            textILPengeluaranRutin.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganPembiayaanFragment)
            }
            edtPengeluaranRutin.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanPendapatan = edtPengeluaranRutin.text.toString()
                if (strPerkiraanPendapatan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanPendapatan.extractNumber())
                        edtPengeluaranRutin.setText(strPerkiraanPendapatan.extractNumber())
                    } else edtPengeluaranRutin.setText(
                        currencyFormatter(
                            strPerkiraanPendapatan.toLong(),
                            false
                        )
                    )
                }
            }
            textILPengeluaranTerbesar.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganPembiayaanFragment)
            }
            textILPenggunaanPengeluaranTerbesar.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKeuanganPembiayaanFragment)
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.apply {
            when (editText) {
                textILPerkiraanPendapatanBulanTahun.editText -> {
                    val strPerkiraanPendapatan = editText?.text.toString()
                    if (strPerkiraanPendapatan.isNotEmpty()) {
                        registrasiPerorangan.pengeluaranRutin =
                            strPerkiraanPendapatan.extractNumber().toLong()
                        viewModel.formulirPembiayaanPerhutananSosial.income =
                            strPerkiraanPendapatan.extractNumber().toLong()
                    }
                    textILPerkiraanPendapatanBulanTahun.error =
                        strPerkiraanPendapatan.getError(requireContext())
                }

                textILSiklusPendapatanAwal.editText -> {
                    val strPerkiraanPendapatan2 = editText?.text.toString()
                    if (strPerkiraanPendapatan2.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.incomeCycle =
                        strPerkiraanPendapatan2.toInt()
                    textILSiklusPendapatanAwal.error =
                        strPerkiraanPendapatan2.getError(requireContext())
                }

                textILSiklusPendapatanAkhir.editText -> {
                    val strPerkiraanPendapatan2 = editText?.text.toString()
                    if (strPerkiraanPendapatan2.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.incomeCycleUnit =
                        strPerkiraanPendapatan2
                    textILSiklusPendapatanAkhir.error =
                        strPerkiraanPendapatan2.getError(requireContext())
                }

                textILPengeluaranRutin.editText -> {
                    val strPengeluaranRutin = editText?.text.toString()
                    if (strPengeluaranRutin.isNotEmpty()) {
                        registrasiPerorangan.pengeluaranRutin =
                            strPengeluaranRutin.extractNumber().toLong()
                        viewModel.formulirPembiayaanPerhutananSosial.expense =
                            strPengeluaranRutin.extractNumber().toLong()
                    }
                    textILPengeluaranRutin.error = strPengeluaranRutin.getError(requireContext())
                }

                textILPengeluaranTerbesar.editText -> {
                    val strPengeluaranTerbesar = editText?.text.toString()
                    if (strPengeluaranTerbesar.isNotEmpty()) {
                        registrasiPerorangan.pengeluaranTerbesar =
                            strPengeluaranTerbesar.extractNumber().toLong()
                        viewModel.formulirPembiayaanPerhutananSosial.largestExpense =
                            strPengeluaranTerbesar.extractNumber().toLong()
                    }
                    textILPengeluaranTerbesar.error =
                        strPengeluaranTerbesar.getError(requireContext())
                }

                textILPenggunaanPengeluaranTerbesar.editText -> {
                    val strPenggunaanPengeluaranTerbesar = editText?.text.toString()
                    registrasiPerorangan.penggunaanTerbesar =
                        strPenggunaanPengeluaranTerbesar
                    viewModel.formulirPembiayaanPerhutananSosial.largestUseExpense =
                        strPenggunaanPengeluaranTerbesar
                    textILPenggunaanPengeluaranTerbesar.error =
                        strPenggunaanPengeluaranTerbesar.getError(requireContext())
                }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}