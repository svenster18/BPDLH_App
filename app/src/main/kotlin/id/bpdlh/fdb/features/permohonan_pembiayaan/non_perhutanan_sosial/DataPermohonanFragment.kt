package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

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
import id.bpdlh.fdb.core.common.utils.convertToIndonesianWords
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.databinding.FragmentDataPermohonanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import javax.inject.Inject

class DataPermohonanFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private var _binding: FragmentDataPermohonanBinding? = null
    private val binding get() = _binding!!

    override fun isValid(): Boolean {
        binding.apply {
            val jangkaWaktuError =
                textILJangkaWaktu.editText?.text.toString().getError(requireContext())
            textILJangkaWaktu.error(jangkaWaktuError)
            if (jangkaWaktuError != null) return false
            val skemaPembiayaanError =
                textILSkemaPembiayaan.editText?.text.toString().getError(requireContext())
            textILSkemaPembiayaan.error(skemaPembiayaanError)
            if (skemaPembiayaanError != null) return false
            val rencanaJaminanError =
                textILRencanaJaminan.editText?.text.toString().getError(requireContext())
            textILRencanaJaminan.error(rencanaJaminanError)
            if (rencanaJaminanError != null) return false
        }
        return true
    }

    override fun initUI() {
        val skemaPembiayaanArray = resources.getStringArray(R.array.skema_pembiayaan)
        binding.apply {
            pembiayaanPerhutananEntity?.let {
                edtNominalPermohonan.setText(currencyFormatter(it.amountOfLoan, false))
                edtTerbilang.setText(
                    convertToIndonesianWords(
                        it.amountOfLoan
                    )
                )
            }
            viewModel.formulirPembiayaanNonPerhutananSosialResult.observe(
                viewLifecycleOwner
            ) { draft ->
                draft?.let {
                    edtJangkaWaktu.setText(it.jangka_waktu.toString())
                    edtSkemaPembiayaan.setText(it.skema_pembiayaan)
                    edtRencanaJaminan.setText(it.rencana_jaminan)
                }
            }

            category?.let { edtJenisLayanan.setText(it) }
            val skemaPembiayaanAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    skemaPembiayaanArray
                )
            edtSkemaPembiayaan.setAdapter(skemaPembiayaanAdapter)
        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDataPermohonanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textILJangkaWaktu.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanFragment)
            }
            textILSkemaPembiayaan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanFragment)
            }
            textILRencanaJaminan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanFragment)
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILJangkaWaktu.editText -> {
                val strJangkaWaktu = editText?.text.toString()
                if (strJangkaWaktu.isNotEmpty())
                    viewModel.formulirPembiayaanNonPerhutananSosial.jangka_waktu =
                        strJangkaWaktu.toInt()
                binding.textILJangkaWaktu.error = strJangkaWaktu.getError(requireContext())
            }

            binding.textILSkemaPembiayaan.editText -> {
                val strSkemaPembiayaan = editText?.text.toString()
                viewModel.formulirPembiayaanNonPerhutananSosial.skema_pembiayaan =
                    strSkemaPembiayaan
                binding.textILSkemaPembiayaan.error = strSkemaPembiayaan.getError(requireContext())
            }

            binding.textILRencanaJaminan.editText -> {
                val strRencanaJaminan = editText?.text.toString()
                viewModel.formulirPembiayaanNonPerhutananSosial.rencana_jaminan =
                    strRencanaJaminan
                binding.textILRencanaJaminan.error = strRencanaJaminan.getError(requireContext())
            }
        }
    }
}