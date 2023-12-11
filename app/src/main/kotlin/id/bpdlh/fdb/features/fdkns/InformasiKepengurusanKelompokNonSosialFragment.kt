package id.bpdlh.fdb.features.fdkns

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.getNikError
import id.bpdlh.fdb.core.common.utils.getPhoneNumberError
import id.bpdlh.fdb.databinding.FragmentInformasiKepengurusanKelompokNonSosialBinding

class InformasiKepengurusanKelompokNonSosialFragment: BaseFormDaftarNonSosialFragment(), TextWatcherTextChange {

    private lateinit var binding: FragmentInformasiKepengurusanKelompokNonSosialBinding
    private var communicator: IFormulirPendaftaranKelompokNonSosialCommunicator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformasiKepengurusanKelompokNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvDescription.text = getString(R.string.formulir_pendaftaran_kelompok_non_sosial_informasi_kepengurusan)
            tvStep.text = requireContext().getString(R.string.formulir_pendaftaran_kelompok_non_sosial_text_stepper, "2")
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnKembali.setOnClickListener {
                communicator?.goToPage(0)
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
        }
        formWatcher()
    }

    override fun initUI() {
        with(binding){
            val jenisKelamin = arrayListOf(
                Constants.LAKI_LAKI,
                Constants.PEREMPUAN,
            )
            val jenisKelaminAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                jenisKelamin
            )
            edtJenisKelaminKetua.setAdapter(jenisKelaminAdapter)
            edtJenisKelaminSekretaris.setAdapter(jenisKelaminAdapter)
            edtJenisKelaminBendahara.setAdapter(jenisKelaminAdapter)
            edtJenisKelaminPendamping.setAdapter(jenisKelaminAdapter)
            tieNamaKetuaKelompok.setText(formulirKelompokNonSosialPost.leaderName)
            tieNoTelpKetua.setText(formulirKelompokNonSosialPost.leaderPhoneNumber)
            tieNikKetua.setText(formulirKelompokNonSosialPost.leaderKtp)
            edtJenisKelaminKetua.setText(formulirKelompokNonSosialPost.leaderGender)
            tieNamaSekretaris.setText(formulirKelompokNonSosialPost.secretaryName)
            tieNomerSekretaris.setText(formulirKelompokNonSosialPost.secretaryPhoneNumber)
            edtJenisKelaminSekretaris.setText(formulirKelompokNonSosialPost.secretaryGender)
            tieNamaBendahara.setText(formulirKelompokNonSosialPost.treasurerName)
            tieNomerBendahara.setText(formulirKelompokNonSosialPost.treasurerPhoneNumber)
            edtJenisKelaminBendahara.setText(formulirKelompokNonSosialPost.treasurerGender)
            tieNamaPendamping.setText(formulirKelompokNonSosialPost.companionName)
            tieNomerPendamping.setText(formulirKelompokNonSosialPost.companionPhoneNumber)
            tieStatusPendamping.setText(formulirKelompokNonSosialPost.companionStatus)
            edtJenisKelaminPendamping.setText(formulirKelompokNonSosialPost.companionGender)
            formulirKelompokNonSosialPost.amountOfMember?.let {
                if (formulirKelompokNonSosialPost.amountOfMember != 0) {
                    tieJumlahAnggota.setText(formulirKelompokNonSosialPost.amountOfMember.toString())
                }
            }
            formulirKelompokNonSosialPost.amountOfMemberLand?.let {
                if( formulirKelompokNonSosialPost.amountOfMemberLand != 0) {
                    tieJumlahAndilGarapan.setText(formulirKelompokNonSosialPost.amountOfMemberLand.toString())
                }
            }
            formulirKelompokNonSosialPost.amountOfMemberLandArea?.let {
                if (formulirKelompokNonSosialPost.amountOfMemberLandArea != 0) {
                    tieTotalLuasLahan.setText(formulirKelompokNonSosialPost.amountOfMemberLandArea.toString())
                }
            }
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilNamaKetuaKelompok.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNoTelpKetua.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNikKetua.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilJenisKelaminKetua.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNamaSekretaris.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNomerSekretaris.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilJenisKelaminSekretaris.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNamaBendahara.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNomerBendahara.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilJenisKelaminBendahara.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNamaPendamping.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilNomerPendamping.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilStatusPendamping.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilJenisKelaminPendamping.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilJumlahAnggota.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilJumlahAndilGarapan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
            tilTotalLuasLahan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val ketuaKelompok = tilNamaKetuaKelompok.editText?.text.toString().getGeneralError(requireContext())
            ketuaKelompok?.let { errorMessages.add(it) }
            tilNamaKetuaKelompok.error = ketuaKelompok

            val noTelpKetua = tilNoTelpKetua.editText?.text.toString().getPhoneNumberError(requireContext())
            noTelpKetua?.let { errorMessages.add(it) }
            tilNoTelpKetua.error = noTelpKetua

            val nikKtpKetua = tilNikKetua.editText?.text.toString().getNikError(requireContext())
            nikKtpKetua?.let { errorMessages.add(it) }
            tilNikKetua.error = nikKtpKetua

            val jenisKelaminKetua = tilJenisKelaminKetua.editText?.text.toString().getError(requireContext())
            jenisKelaminKetua?.let { errorMessages.add(it) }
            tilJenisKelaminKetua.error = jenisKelaminKetua

            val namaSekretaris = tilNamaSekretaris.editText?.text.toString().getGeneralError(requireContext())
            namaSekretaris?.let { errorMessages.add(it) }
            tilNamaSekretaris.error = namaSekretaris

            val noTelpSkretaris = tilNomerSekretaris.editText?.text.toString().getPhoneNumberError(requireContext())
            noTelpSkretaris?.let { errorMessages.add(it) }
            tilNomerSekretaris.error = noTelpSkretaris

            val jenisKelaminSekretaris = tilJenisKelaminSekretaris.editText?.text.toString().getError(requireContext())
            jenisKelaminSekretaris?.let { errorMessages.add(it) }
            tilJenisKelaminSekretaris.error = jenisKelaminSekretaris

            val namaBendahara = tilNamaBendahara.editText?.text.toString().getGeneralError(requireContext())
            namaBendahara?.let { errorMessages.add(it) }
            tilNamaBendahara.error = namaBendahara

            val nomerTelpBendahara = tilNomerBendahara.editText?.text.toString().getPhoneNumberError(requireContext())
            nomerTelpBendahara?.let { errorMessages.add(it) }
            tilNomerBendahara.error = nomerTelpBendahara

            val jenisKelaminBendahara = tilJenisKelaminBendahara.editText?.text.toString().getError(requireContext())
            jenisKelaminBendahara?.let { errorMessages.add(it) }
            tilJenisKelaminBendahara.error = jenisKelaminBendahara

            val namaPendamping = tilNamaPendamping.editText?.text.toString().getGeneralError(requireContext())
            namaPendamping?.let { errorMessages.add(it) }
            tilNamaPendamping.error = namaPendamping

            val noTelpPendamping = tilNomerPendamping.editText?.text.toString().getPhoneNumberError(requireContext())
            noTelpPendamping?.let { errorMessages.add(it) }
            tilNomerPendamping.error = noTelpPendamping

            val statusPendamping = tilStatusPendamping.editText?.text.toString().getError(requireContext())
            statusPendamping?.let { errorMessages.add(it) }
            tilStatusPendamping.error = statusPendamping

            val jenisKelaminPendamping = tilJenisKelaminPendamping.editText?.text.toString().getError(requireContext())
            jenisKelaminPendamping?.let { errorMessages.add(it) }
            tilJenisKelaminPendamping.error = jenisKelaminPendamping

            val jumlahAnggota = tilJumlahAnggota.editText?.text.toString().getError(requireContext())
            jumlahAnggota?.let { errorMessages.add(it) }
            tilJumlahAnggota.error = jumlahAnggota

            val jumlahAndilGarapan = tilJumlahAndilGarapan.editText?.text.toString().getError(requireContext())
            jumlahAndilGarapan?.let { errorMessages.add(it) }
            tilJumlahAndilGarapan.error = jumlahAndilGarapan

            val totalLuasLahan = tilTotalLuasLahan.editText?.text.toString().getError(requireContext())
            totalLuasLahan?.let { errorMessages.add(it) }
            tilTotalLuasLahan.error = totalLuasLahan

        }
        if (errorMessages.size == 0) {
            communicator?.goToPage(2)
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText){
            binding.tilNamaKetuaKelompok.editText -> {
                formulirKelompokNonSosialPost.leaderName = editText?.text.toString()
            }
            binding.tilNoTelpKetua.editText -> {
                formulirKelompokNonSosialPost.leaderPhoneNumber = editText?.text.toString()
            }
            binding.tilNikKetua.editText -> {
                formulirKelompokNonSosialPost.leaderKtp = editText?.text.toString()
            }
            binding.tilJenisKelaminKetua.editText -> {
                formulirKelompokNonSosialPost.leaderGender = editText?.text.toString()
            }
            binding.tilNamaSekretaris.editText -> {
                formulirKelompokNonSosialPost.secretaryName = editText?.text.toString()
            }
            binding.tilNomerSekretaris.editText -> {
                formulirKelompokNonSosialPost.secretaryPhoneNumber = editText?.text.toString()
            }
            binding.tilJenisKelaminSekretaris.editText -> {
                formulirKelompokNonSosialPost.secretaryGender = editText?.text.toString()
            }
            binding.tilNamaBendahara.editText -> {
                formulirKelompokNonSosialPost.treasurerName = editText?.text.toString()
            }
            binding.tilNomerBendahara.editText -> {
                formulirKelompokNonSosialPost.treasurerPhoneNumber = editText?.text.toString()
            }
            binding.tilJenisKelaminBendahara.editText -> {
                formulirKelompokNonSosialPost.treasurerGender = editText?.text.toString()
            }
            binding.tilNamaPendamping.editText -> {
                formulirKelompokNonSosialPost.companionName = editText?.text.toString()
            }
            binding.tilNomerPendamping.editText -> {
                formulirKelompokNonSosialPost.companionPhoneNumber = editText?.text.toString()
            }
            binding.tilStatusPendamping.editText -> {
                formulirKelompokNonSosialPost.companionStatus = editText?.text.toString()
            }
            binding.tilJenisKelaminPendamping.editText -> {
                formulirKelompokNonSosialPost.companionGender = editText?.text.toString()
            }
            binding.tilJumlahAnggota.editText -> {
                editText?.text.toString().toIntOrNull()?.let { formulirKelompokNonSosialPost.amountOfMember = it }
            }
            binding.tilJumlahAndilGarapan.editText -> {
                editText?.text.toString().toIntOrNull()?.let { formulirKelompokNonSosialPost.amountOfMemberLand = it }
            }
            binding.tilTotalLuasLahan.editText -> {
                editText?.text.toString().toIntOrNull()?.let { formulirKelompokNonSosialPost.amountOfMemberLandArea = it }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}