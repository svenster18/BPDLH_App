package id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.R as CoreR
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.DATE_PICKER_TAG
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.getDateError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.getNikError
import id.bpdlh.fdb.core.common.utils.getPhoneNumberError
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.databinding.FragmentTambahDebiturBinding

class TambahDebiturNonSosialFragment(
    private val onClickAdd: ((DataDebiturNonSosialEntity) -> Unit)? = null
): BaseDaggerBottomDialogFragment(), TextWatcherTextChange {

    private var _binding: FragmentTambahDebiturBinding? = null
    private var type = ""
    private var isEdit = false
    private var tujuanPengajuan : String = ""

    private val binding get() = _binding!!
    private var dataDebiturNonSosialEntity: DataDebiturNonSosialEntity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(STYLE_NORMAL, CoreR.style.BottomSheetDialogTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TambahDebiturNonSosialFragment.TYPE, TypeBottomSheet.SOSIAL)
            dataDebiturNonSosialEntity = it.getParcelable(TambahDebiturNonSosialFragment.DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahDebiturBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(clTambahDebitur)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        initUI()
        formWatcher()
    }

    private fun initUI() {
        with(binding) {
            val jenisKelamin = arrayListOf(
                Constants.LAKI_LAKI,
                Constants.PEREMPUAN,
            )
            val jenisKelaminAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                jenisKelamin
            )
            edtJenisKelamin.setAdapter(jenisKelaminAdapter)
            if (type == TypeBottomSheet.NON_SOSIAL) {
                val jenisLayanan = arrayListOf(
                    Constants.HASIL_HUTAN_BUKAN_KAYU,
                    Constants.TUNDA_TEBANG,
                    Constants.KOMODITAS_NON_KEHUTANAN
                )
                val jenisLayananAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    jenisLayanan
                )
                edtJenisLayanan.setAdapter(jenisLayananAdapter)
                tvNilaiPermohonan.visible()
                tilNilaiPermohonan.visible()
                tvJenisLayanan.visible()
                tilJenisLayanan.visible()
            }
            radioTujuanPengajuan.setOnCheckedChangeListener { radioGroup, i ->
                val id = radioGroup.checkedRadioButtonId
                val radio: RadioButton = radioGroup.findViewById(id)
                tujuanPengajuan = radio.text.toString()
            }
            dataDebiturNonSosialEntity?.let {
                edtNamaDebitur.setText(it.nama)
                edtEmail.setText(it.email)
                edtNik.setText(it.nik)
                edtJenisLayanan.setText(it.jenisLayanan)
                edtTelepon.setText(it.noTelp)
                edtJenisKelamin.setText(it.gender)
                it.submissionPurpose?.let { it1 ->
                    when(it1){
                        getString(R.string.untuk_modal_kerja) -> radioTujuanPengajuan.check(radioUntukModalKerja.id)
                        getString(R.string.untuk_investasi) -> radioTujuanPengajuan.check(radioUntukInventasi.id)
                    }
                }
                edtDetailTujuanPengajuan.setText(it.detailSubmissionPurpose)
                it.nilaiPermohonan?.let { it1-> edtNilaiPermohonan.setText(it1.toString()) }
                edtTanggalLahir.setText(it.tanggalLahir?.convertStringDate(input = Constants.ISO_DATE_FORMAT,
                    output = Constants.NEW_DATE_FORMAT))
            }
            btnBatal.setOnClickListener {
                dismiss()
            }

            btnClose.setOnClickListener {
                dismiss()
            }
            btnTambah.setOnClickListener {
                validateInput()
            }
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilNamaDebitur.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilNik.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilTanggalLahir.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilTanggalLahir.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilEmail.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilNoTelepon.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilJenisKelamin.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilJenisLayanan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilNilaiPermohonan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }
            tilDetailTujuanPengajuan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturNonSosialFragment)
            }

        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val strNamaDebitur = tilNamaDebitur.editText?.text.toString().getError(requireContext())
            strNamaDebitur?.let { errorMessages.add(it) }
            tilNamaDebitur.error = strNamaDebitur

            val strNik = tilNik.editText?.text.toString().getNikError(requireContext())
            strNik?.let { errorMessages.add(it) }
            tilNik.error = strNik

            val strTglLahir = tilTanggalLahir.editText?.text.toString().getDateError(requireContext())
            strTglLahir?.let { errorMessages.add(it) }
            tilTanggalLahir.error = strTglLahir

            val strEmail = tilEmail.editText?.text.toString().getError(requireContext())
            strEmail?.let { errorMessages.add(it) }
            tilEmail.error = strEmail

            val strNoTelp = tilNoTelepon.editText?.text.toString().getPhoneNumberError(requireContext())
            strNoTelp?.let { errorMessages.add(it) }
            tilNoTelepon.error = strNoTelp

            val jenisKelamin = tilJenisKelamin.editText?.text.toString().getGeneralError(requireContext())
            jenisKelamin?.let { errorMessages.add(it) }
            tilJenisKelamin.error = jenisKelamin

            if(radioTujuanPengajuan.checkedRadioButtonId <= 0 ) {
                radioUntukInventasi.error = resources.getString(R.string.wajib_di_isi)
            }

            val detailTujuanPengajuan = tilDetailTujuanPengajuan.editText?.text.toString().getGeneralError(requireContext())
            detailTujuanPengajuan?.let { errorMessages.add(it) }
            tilDetailTujuanPengajuan.error = detailTujuanPengajuan

            val strJenisLayanan = tilJenisLayanan.editText?.text.toString().getGeneralError(requireContext())
            strJenisLayanan?.let { errorMessages.add(it) }
            tilJenisLayanan.error = strJenisLayanan

            if (errorMessages.size == 0) {
                onClickAdd?.let {
                    var nilaiPermohonan = 0
                    tilNilaiPermohonan.editText?.text.toString().toIntOrNull()?.let {
                        nilaiPermohonan = it
                    }
                    it(DataDebiturNonSosialEntity(
                        nama = tilNamaDebitur.editText?.text.toString(),
                        email = tilEmail.editText?.text.toString(),
                        nik = tilNik.editText?.text.toString(),
                        tanggalLahir = tilTanggalLahir.editText?.text.toString().convertStringDate(input = Constants.NEW_DATE_FORMAT,
                            output = Constants.ISO_DATE_FORMAT),
                        noTelp = tilNoTelepon.editText?.text.toString(),
                        jenisLayanan = tilJenisLayanan.editText?.text.toString(),
                        nilaiPermohonan = nilaiPermohonan,
                        gender = tilJenisKelamin.editText?.text.toString(),
                        submissionPurpose = tujuanPengajuan,
                        detailSubmissionPurpose = tilDetailTujuanPengajuan.editText?.text.toString(),
                        isSend = false
                        ))
                }
                dismiss()
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        editText?.error = null
        with(binding) {
            when(editText) {
                tilNamaDebitur.editText -> {
                    val errNamaDebitur = tilNamaDebitur.editText?.text.toString().getError(requireContext())
                    tilNamaDebitur.error = errNamaDebitur
                }
                tilNik.editText -> {
                    val errNik = tilNik.editText?.text.toString().getNikError(requireContext())
                    tilNik.error = errNik
                }
                tilTanggalLahir.editText -> {
                    val err = tilTanggalLahir.editText?.text.toString().getError(requireContext())
                    tilTanggalLahir.error = err
                }
                tilEmail.editText -> {
                    val err = tilEmail.editText?.text.toString().getError(requireContext())
                    tilEmail.error = err
                }
                tilNoTelepon.editText -> {
                    val err = tilNoTelepon.editText?.text.toString().getPhoneNumberError(requireContext())
                    tilNoTelepon.error = err
                }
                tilJenisKelamin.editText -> {
                    val err = tilJenisKelamin.editText?.text.toString().getError(requireContext())
                    tilJenisKelamin.error = err
                }
                tilDetailTujuanPengajuan.editText -> {
                    val err = tilDetailTujuanPengajuan.editText?.text.toString().getGeneralError(requireContext())
                    tilDetailTujuanPengajuan.error = err
                }
                tilJenisKelamin.editText -> {
                    val err = tilJenisKelamin.editText?.text.toString().getError(requireContext())
                    tilJenisKelamin.error = err
                }
                tilJenisLayanan.editText -> {
                    val err = tilJenisLayanan.editText?.text.toString().getError(requireContext())
                    tilJenisLayanan.error = err
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun injectComponent() {
        //
    }

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        annotation class TypeBottomSheet {
            companion object {
                const val SOSIAL = "Sosial"
                const val NON_SOSIAL = "Non Sosial"
            }
        }
        const val TAG = "TambahDebitur"
        const val TYPE = "type"
        const val DATA = "data";

        fun newInstance(
            onClickAdd: ((DataDebiturNonSosialEntity) -> Unit)? = null,
            groupApplicationMemberPost: DataDebiturNonSosialEntity? = null,
            @TypeBottomSheet type: String = TypeBottomSheet.SOSIAL,
        ) = TambahDebiturNonSosialFragment(
            onClickAdd = onClickAdd
        ).apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
                putParcelable(DATA, groupApplicationMemberPost)
            }
        }
    }
}