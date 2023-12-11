package id.bpdlh.fdb.features.fpns

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.convertToIndonesianWords
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.databinding.FragmentDataPermohonanNonSosialBinding
import javax.inject.Inject

class DataPermohonanNonSosialFragment : BaseFormPermohonanNonSosialFragment(), TextWatcherTextChange {

    private lateinit var binding: FragmentDataPermohonanNonSosialBinding
    private var communicator: IFormPermohonanKelompokNonSosialCommunicator? = null

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormPermohonanNonSosialViewModel> { factory }
    private lateinit var progressDialog: CustomProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormPermohonanKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataPermohonanNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvDescription.text = getString(R.string.form_permohonan_non_sosial_data_permohonan)
            tvStep.text = requireContext().getString(R.string.formulir_pendaftaran_kelompok_non_sosial_text_stepper, "1")
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
        }
        formWatcher()
    }

    override fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        with(binding){
            edtJenisLayanan.setText(memberApplicationPost.serviceType)
            val skemaPembiayaan = arrayListOf(
                Constants.REVOLVING,
                Constants.NON_REVOLVING,
            )
            val skemaPembiayaanAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                skemaPembiayaan
            )
            edtSkemaPembiayaan.setAdapter(skemaPembiayaanAdapter)
            memberApplicationPost.applicationValue?.let {
                edtNominalPermohonan.setText(currencyFormatter(it.toLong(), true))
                edtTerbilang.setText(convertToIndonesianWords(it.toLong()))
            }
            memberApplicationPost.timePeriod?.let {
                if(it != 0) edtJangkaWaktu.setText(it.toString())
            }
            memberApplicationPost.financingScheme?.let {
                edtSkemaPembiayaan.setText(it)
            }
            memberApplicationPost.warrantyplan?.let {
                edtRencanaJaminan.setText(it)
            }
            when(memberApplicationPost.serviceType) {
                Constants.TUNDA_TEBANG -> {
                    rencanaJaminan.visible()
                    tilRencanaJaminan.visible()
                }
                Constants.KOMODITAS_NON_KEHUTANAN,
                Constants.HASIL_HUTAN_BUKAN_KAYU -> {
                    tilRencanaJaminan.gone()
                    rencanaJaminan.gone()

                    //? Set Default Time Periode
                    memberApplicationPost.timePeriodUnit = resources.getStringArray(R.array.lama_usaha).last()
                }
            }
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilJenisLayanan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanNonSosialFragment)
            }
            tilNominalPermohonan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanNonSosialFragment)
            }
            tilTerbilang.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanNonSosialFragment)
            }
            tilJangkaWaktu.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanNonSosialFragment)
            }
            tilSkemaPembiayaan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanNonSosialFragment)
            }
            tilRencanaJaminan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val jenisLayanan = tilJenisLayanan.editText?.text.toString().getError(requireContext())
            jenisLayanan?.let { errorMessages.add(it) }
            tilJenisLayanan.error = jenisLayanan

            val nominalPermohonan = tilNominalPermohonan.editText?.text.toString().getError(requireContext())
            nominalPermohonan?.let { errorMessages.add(it) }
            tilNominalPermohonan.error = nominalPermohonan

            val terbilang = tilTerbilang.editText?.text.toString().getError(requireContext())
            terbilang?.let { errorMessages.add(it) }
            tilTerbilang.error = terbilang

            val jangkaWaktu = tilJangkaWaktu.editText?.text.toString().getError(requireContext())
            jangkaWaktu?.let { errorMessages.add(it) }
            tilJangkaWaktu.error = jangkaWaktu

            val skemaPembiayaan = tilSkemaPembiayaan.editText?.text.toString().getError(requireContext())
            skemaPembiayaan?.let { errorMessages.add(it) }
            tilSkemaPembiayaan.error = skemaPembiayaan

            if (memberApplicationPost.serviceType == Constants.TUNDA_TEBANG) {
                val rencanaJaminan = tilRencanaJaminan.editText?.text.toString().getError(requireContext())
                rencanaJaminan?.let { errorMessages.add(it) }
                tilRencanaJaminan.error = rencanaJaminan
            }

        }
        if (errorMessages.size == 0) {
            communicator?.goToPage(1)
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText) {
            binding.tilJenisLayanan.editText -> {
            }
            binding.tilNominalPermohonan.editText -> {
            }
            binding.tilTerbilang.editText -> {
            }
            binding.tilJangkaWaktu.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.timePeriod = it }
            }
            binding.tilSkemaPembiayaan.editText -> {
                memberApplicationPost.financingScheme = editText?.text.toString()
            }
            binding.tilRencanaJaminan.editText -> {
                memberApplicationPost.warrantyplan = editText?.text.toString()
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}
