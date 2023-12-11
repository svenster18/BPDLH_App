package id.bpdlh.fdb.features.daftar_anggota_pemohon

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.DATE_PICKER_TAG
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.getDateError
import id.bpdlh.fdb.core.common.utils.getEmailError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getNikError
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.databinding.FragmentTambahDebiturBinding
import java.util.UUID
import id.bpdlh.fdb.core.R as CoreR

class TambahDebiturFragment: BaseDaggerBottomDialogFragment(), TextWatcherTextChange {

    private var _binding: FragmentTambahDebiturBinding? = null
    private var type = ""
    private var dataDebitur: DataDebiturEntity? = null
    private var isEdit = false

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(STYLE_NORMAL, CoreR.style.BottomSheetDialogTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE, TypeBottomSheet.SOSIAL)
            dataDebitur = it.parcelable(DATA)
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
        dataDebitur?.let { initData(it) }
        formWatcher()
    }

    private fun initUI() {
        with(binding) {
            if (type == TypeBottomSheet.NON_SOSIAL) {
                tvNilaiPermohonan.visible()
                tilNilaiPermohonan.visible()
                tvJenisLayanan.visible()
                tilJenisLayanan.visible()
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

    private fun initData(data: DataDebiturEntity) {
        isEdit = true
        with(binding) {
            tilNamaDebitur.editText?.setText(data.nama.orEmpty())
            tilNik.editText?.setText(data.nik.orEmpty())
            tilTanggalLahir.editText?.setText(data.tanggalLahir?.convertStringDate(input = Constants.ISO_DATE_FORMAT,
                output = Constants.NEW_DATE_FORMAT))
            tilEmail.editText?.setText(data.email.orEmpty())
            tilNoTelepon.editText?.setText(data.noTelp.orEmpty())
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilNamaDebitur.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturFragment)
            }
            tilNik.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturFragment)
            }
            tilTanggalLahir.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilTanggalLahir.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilTanggalLahir.setErrorIconOnClickListener {
                tilTanggalLahir.error = null
                val datePickerSpinner = DatePickerSpinner(tilTanggalLahir.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilTanggalLahir.editText?.setOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilTanggalLahir.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilEmail.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturFragment)
            }
            tilNoTelepon.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahDebiturFragment)
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

            val strEmail = tilEmail.editText?.text.toString().getEmailError(requireContext())
            strEmail?.let { errorMessages.add(it) }
            tilEmail.error = strEmail

            val strNoTelp = tilNoTelepon.editText?.text.toString().getError(requireContext())
            strNoTelp?.let { errorMessages.add(it) }
            tilNoTelepon.error = strNoTelp

            if (errorMessages.size == 0) {
                val debitur = DataDebiturEntity(
                    id = if(isEdit) dataDebitur?.id.orEmpty() else UUID.randomUUID().toString(),
                    nik = tilNik.editText?.text.toString(),
                    nama = tilNamaDebitur.editText?.text.toString(),
                    tanggalLahir = tilTanggalLahir.editText?.text.toString().convertStringDate(input = Constants.NEW_DATE_FORMAT,
                        output = Constants.ISO_DATE_FORMAT),
                    email = tilEmail.editText?.text.toString().lowercase(),
                    noTelp = tilNoTelepon.editText?.text.toString(),
                )
                setFragmentResult(
                    REQUEST_KEY_DEBITUR,
                    bundleOf(REQUEST_KEY_DEBITUR to debitur, REQUEST_KEY_EDIT to isEdit)
                )
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
                    val err = tilEmail.editText?.text.toString().getEmailError(requireContext())
                    tilEmail.error = err
                }

                tilNoTelepon.editText -> {
                    val err = tilNoTelepon.editText?.text.toString().getError(requireContext())
                    tilNoTelepon.error = err
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
        const val DATA = "data"
        const val REQUEST_KEY_DEBITUR = "debitur"
        const val REQUEST_KEY_EDIT = "edit"

        fun newInstance(
            @TypeBottomSheet type: String = TypeBottomSheet.SOSIAL,
            data: DataDebiturEntity? = null
        ) = TambahDebiturFragment().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
                putParcelable(DATA, data)
            }
        }
    }
}