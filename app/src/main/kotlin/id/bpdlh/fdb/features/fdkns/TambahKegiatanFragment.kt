package id.bpdlh.fdb.features.fdkns

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetBehavior
import id.bpdlh.fdb.core.R as CoreR
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.databinding.FragmentTambahKegiatanBinding

class TambahKegiatanFragment(
    private val type: String,
    private val activityEntity: ActivityEntity? = null,
    private val onAddKegiatan: ((ActivityEntity) -> Unit)? = null
): BaseDaggerBottomDialogFragment(), TextWatcherTextChange {

    private var _binding: FragmentTambahKegiatanBinding? = null

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(STYLE_NORMAL, CoreR.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahKegiatanBinding.inflate(inflater, container, false)
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
            when(type){
                Constants.KEGIATAN_KELOMPOK,
                Constants.GAMBARAN_UMUM -> {
                    tvKategoriUsaha.gone()
                    tilKategoriUsaha.gone()
                }
                Constants.MITRA_USAHA -> {
                    tvKategori.gone()
                    tilKategori.gone()
                    tvDeskripsi.gone()
                    tilDeskripsi.gone()
                }
            }
            activityEntity.let {
                edtKategori.setText(activityEntity?.category)
                edtKategoriUsaha.setText(activityEntity?.category)
                edtDeksripsi.setText(activityEntity?.description)
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
            tilKategori.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahKegiatanFragment)
            }
            tilKategoriUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahKegiatanFragment)
            }
            tilDeskripsi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@TambahKegiatanFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            when(type){
                Constants.KEGIATAN_KELOMPOK,
                Constants.GAMBARAN_UMUM -> {
                    val strKategori = tilKategori.editText?.text.toString().getGeneralError(requireContext())
                    strKategori?.let { errorMessages.add(it) }
                    tilKategori.error = strKategori

                    val strDeskripsi = tilDeskripsi.editText?.text.toString().getGeneralError(requireContext())
                    strDeskripsi?.let { errorMessages.add(it) }
                    tilDeskripsi.error = strDeskripsi

                    if (errorMessages.size == 0) {
                        onAddKegiatan?.let {
                            it(
                                ActivityEntity(
                                    description = tilDeskripsi.editText?.text.toString(),
                                    category = tilKategori.editText?.text.toString(),
                                )
                            )
                        }
                        dismiss()
                    }
                }
                Constants.MITRA_USAHA -> {
                    val strKategoriUsaha = tilKategoriUsaha.editText?.text.toString().getGeneralError(requireContext())
                    strKategoriUsaha?.let { errorMessages.add(it) }
                    tilKategoriUsaha.error = strKategoriUsaha

                    if (errorMessages.size == 0) {
                        onAddKegiatan?.let {
                            it(
                                ActivityEntity(
                                    category = tilKategoriUsaha.editText?.text.toString(),
                                )
                            )
                        }
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        editText?.error = null
        with(binding) {
            when(editText) {
                tilKategori.editText -> {
                    val errKategori = tilKategori.editText?.text.toString().getError(requireContext())
                    tilKategori.error = errKategori


                }
                tilDeskripsi.editText -> {
                    val errDeskripsi = tilDeskripsi.editText?.text.toString().getError(requireContext())
                    tilDeskripsi.error = errDeskripsi
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

        fun newInstance(
            onAddKegiatan: (ActivityEntity) -> Unit,
            type: String
        ) = TambahKegiatanFragment(
            onAddKegiatan = onAddKegiatan,
            type = type
        )
    }
}