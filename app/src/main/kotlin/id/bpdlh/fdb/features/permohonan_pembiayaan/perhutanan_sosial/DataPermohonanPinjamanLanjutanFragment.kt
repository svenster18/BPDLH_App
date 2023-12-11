package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.convertToIndonesianWords
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.databinding.FragmentDataPermohonanPinjamanLanjutanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import timber.log.Timber
import javax.inject.Inject

class DataPermohonanPinjamanLanjutanFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private var _binding: FragmentDataPermohonanPinjamanLanjutanBinding? = null
    private val binding get() = _binding!!
    private val satuanWaktuList = listOf("Minggu", "Bulan")
    private val jangkaWaktuMingguList = listOf(24, 36)
    private val jangkaWaktuBulanList = listOf(6, 9)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDataPermohonanPinjamanLanjutanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            if (rbLainnya.isChecked) {
                val nominalPinjamanError =
                    textILNominalPinjaman.editText?.text.toString().getError(requireContext())
                textILNominalPinjaman.error(nominalPinjamanError)
                if (nominalPinjamanError != null) return false
            }
            val pinjamanTerbilangError =
                textILTerbilang.editText?.text.toString().getError(requireContext())
            textILTerbilang.error(pinjamanTerbilangError)
            if (pinjamanTerbilangError != null) return false
            val satuanWaktuError =
                textILJangkaWaktu.editText?.text.toString().getError(requireContext())
            textILJangkaWaktu.error(satuanWaktuError)
            if (satuanWaktuError != null) return false
            val jangkaWaktuError =
                textILJangkaWaktu2.editText?.text.toString().getError(requireContext())
            textILJangkaWaktu2.error(jangkaWaktuError)
            if (jangkaWaktuError != null) return false
            val nominalAngsuranError =
                textILNominalAngsuran.editText?.text.toString().getError(requireContext())
            textILNominalAngsuran.error(nominalAngsuranError)
            if (nominalAngsuranError != null) return false
            val angsuranTerbilangError =
                textILTerbilang2.editText?.text.toString().getError(requireContext())
            textILTerbilang2.error(angsuranTerbilangError)
            if (angsuranTerbilangError != null) return false
        }
        return true
    }

    override fun initUI() {
        binding.apply {
            val satuanWaktuAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    satuanWaktuList
                )
            edtJangkaWaktu.setAdapter(satuanWaktuAdapter)
            val formPembiayaan = viewModel.formulirPembiayaanPerhutananSosial
            if (formPembiayaan.amountOfLoan > 0L) {
                rbLainnya.isChecked = true
                textILNominalPinjaman.isEnabled = true
                edtNominalPinjaman.setText(
                    currencyFormatter(
                        formPembiayaan.amountOfLoan,
                        false
                    )
                )
                edtTerbilang.setText(convertToIndonesianWords(formPembiayaan.amountOfLoan))
            }
            edtJangkaWaktu.setText(formPembiayaan.timePeriodUnit)
            if (formPembiayaan.timePeriod > 0) edtJangkaWaktu2.setText(formPembiayaan.timePeriod.toString())
            edtNominalAngsuran.setText(currencyFormatter(formPembiayaan.amountOfInstallment, false))
            edtTerbilang2.setText(convertToIndonesianWords(formPembiayaan.amountOfInstallment))
        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if (edtTerbilang.text.toString().isEmpty()) edtTerbilang.setText(
                convertToIndonesianWords(500000L)
            )
            viewModel.formulirPembiayaanPerhutananSosial.amountOfLoan = 500000
            rbOpsi1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.formulirPembiayaanPerhutananSosial.amountOfLoan = 500000
                    edtTerbilang.setText(convertToIndonesianWords(500000L))
                    if (edtJangkaWaktu2.text.toString().isNotEmpty()) {
                        val nominalAngsuran = hitungNominalAngsuran(
                            500000L,
                            edtJangkaWaktu2.text.toString().toInt()
                        )
                        edtNominalAngsuran.setText(
                            nominalAngsuran.toString()
                        )
                        viewModel.formulirPembiayaanPerhutananSosial.amountOfInstallment =
                            nominalAngsuran
                        edtTerbilang2.setText(
                            convertToIndonesianWords(
                                edtNominalAngsuran.text.toString().extractNumber().toLong()
                            )
                        )
                    }
                }
            }
            rbOpsi2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.formulirPembiayaanPerhutananSosial.amountOfLoan = 1000000
                    edtTerbilang.setText(convertToIndonesianWords(1000000L))
                    if (edtJangkaWaktu2.text.toString().isNotEmpty()) {
                        val nominalAngsuran = hitungNominalAngsuran(
                            1000000L,
                            edtJangkaWaktu2.text.toString().toInt()
                        )
                        edtNominalAngsuran.setText(
                            nominalAngsuran.toString()
                        )
                        viewModel.formulirPembiayaanPerhutananSosial.amountOfInstallment =
                            nominalAngsuran
                        edtTerbilang2.setText(
                            convertToIndonesianWords(
                                edtNominalAngsuran.text.toString().extractNumber().toLong()
                            )
                        )
                    }
                }
            }
            rbLainnya.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    edtTerbilang.setText("")
                    textILNominalPinjaman.isEnabled = true
                    textILNominalPinjaman.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@DataPermohonanPinjamanLanjutanFragment)
                    }
                    edtNominalPinjaman.setOnFocusChangeListener { _, isFocus ->
                        val strNominalPinjaman = edtNominalPinjaman.text.toString()
                        if (strNominalPinjaman.isNotEmpty()) {
                            if (isFocus) {
                                Timber.d(strNominalPinjaman.extractNumber())
                                edtNominalPinjaman.setText(strNominalPinjaman.extractNumber())
                            } else {
                                edtTerbilang.setText(
                                    convertToIndonesianWords(
                                        strNominalPinjaman.extractNumber().toLong()
                                    )
                                )
                                if (edtJangkaWaktu2.text.toString()
                                        .isNotEmpty()
                                ) {
                                    val nominalAngsuran = hitungNominalAngsuran(
                                        strNominalPinjaman.toLong(),
                                        edtJangkaWaktu2.text.toString().toInt()
                                    )
                                    edtNominalAngsuran.setText(
                                        nominalAngsuran.toString()
                                    )
                                    viewModel.formulirPembiayaanPerhutananSosial.amountOfInstallment =
                                        nominalAngsuran
                                    edtTerbilang2.setText(
                                        convertToIndonesianWords(
                                            edtNominalAngsuran.text.toString().extractNumber()
                                                .toLong()
                                        )
                                    )
                                }
                                edtNominalPinjaman.setText(
                                    currencyFormatter(
                                        strNominalPinjaman.toLong(),
                                        false
                                    )
                                )
                            }
                        }
                    }
                    textILNominalPinjaman.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@DataPermohonanPinjamanLanjutanFragment)
                    }
                } else {
                    textILNominalPinjaman.isEnabled = false
                    edtNominalPinjaman.setText("")
                    textILNominalPinjaman.error = null
                }
            }

            textILJangkaWaktu.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanLanjutanFragment)
            }

            textILJangkaWaktu2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanLanjutanFragment)
            }

            edtJangkaWaktu.setOnItemClickListener { _, _, position, _ ->
                val jangkaWaktuList =
                    if (position == 0) jangkaWaktuMingguList else jangkaWaktuBulanList
                val jangkaWaktuAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    jangkaWaktuList
                )
                edtJangkaWaktu2.setAdapter(jangkaWaktuAdapter)
                edtTerbilang2.setText(
                    convertToIndonesianWords(
                        edtNominalAngsuran.text.toString().extractNumber().toLong()
                    )
                )
            }
            edtJangkaWaktu2.setOnItemClickListener { _, _, _, _ ->
                val nominalPinjaman = when {
                    rbOpsi1.isChecked -> 500000L
                    rbOpsi2.isChecked -> 1000000L
                    rbLainnya.isChecked && edtNominalPinjaman.text.toString()
                        .isNotEmpty() -> edtNominalPinjaman.text.toString().extractNumber().toLong()

                    else -> 0
                }
                val nominalAngsuran = hitungNominalAngsuran(
                    nominalPinjaman,
                    edtJangkaWaktu2.text.toString().toInt()
                )
                edtNominalAngsuran.setText(
                    currencyFormatter(
                        nominalAngsuran, false
                    )

                )
                viewModel.formulirPembiayaanPerhutananSosial.amountOfInstallment = nominalAngsuran
                edtTerbilang2.setText(
                    convertToIndonesianWords(
                        edtNominalAngsuran.text.toString().extractNumber().toLong()
                    )
                )
            }
        }
    }

    private fun hitungNominalAngsuran(nominalPinjaman: Long, jangkaWaktu: Int): Long {
        if (jangkaWaktu > 0) {
            if (nominalPinjaman == 1000000L) {
                if (jangkaWaktu == 24 || jangkaWaktu == 6) return 45500L
                else if (jangkaWaktu == 36 || jangkaWaktu == 9) return 31625L
            } else if (nominalPinjaman == 500000L) {
                if (jangkaWaktu == 24 || jangkaWaktu == 6) return 22750L
                else if (jangkaWaktu == 36 || jangkaWaktu == 9) return 15850L
            }
            var jangkaWaktuFix = jangkaWaktu
            if (jangkaWaktu == 6 || jangkaWaktu == 9) jangkaWaktuFix *= 4
            return nominalPinjaman / jangkaWaktuFix
        }
        return 0
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILNominalPinjaman.editText -> {
                val strNominalPinjaman = editText?.text.toString()
                if (strNominalPinjaman.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.amountOfLoan =
                    strNominalPinjaman.extractNumber().toLong()
                binding.textILNominalPinjaman.error = strNominalPinjaman.getError(requireContext())
            }

            binding.textILJangkaWaktu.editText -> {
                val strSatuanWaktu = editText?.text.toString()
                viewModel.formulirPembiayaanPerhutananSosial.timePeriodUnit = strSatuanWaktu
                binding.textILJangkaWaktu.error = strSatuanWaktu.getError(requireContext())
            }

            binding.textILJangkaWaktu2.editText -> {
                val strSatuanWaktu = editText?.text.toString()
                if (strSatuanWaktu.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.timePeriod =
                    strSatuanWaktu.toInt()
                binding.textILJangkaWaktu2.error = strSatuanWaktu.getError(requireContext())
            }

            binding.textILNominalAngsuran.editText -> {
                val strNominalAngsuran = editText?.text.toString()
                if (strNominalAngsuran.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.amountOfInstallment =
                    strNominalAngsuran.extractNumber().toLong()
                binding.edtTerbilang2.setText(
                    convertToIndonesianWords(
                        strNominalAngsuran.extractNumber().toLong()
                    )
                )
                binding.textILNominalAngsuran.error = strNominalAngsuran.getError(requireContext())
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