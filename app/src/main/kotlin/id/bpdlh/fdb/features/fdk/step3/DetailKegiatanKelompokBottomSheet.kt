package id.bpdlh.fdb.features.fdk.step3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerBottomDialogFragment
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.domain.entities.KategoriKegiatanEntity
import id.bpdlh.fdb.databinding.FragmentDetailKegiatanKelompokBottomsheetBinding
import id.bpdlh.fdb.features.fdk.FormulirPendaftaranKelompokViewModel
import javax.inject.Inject
import id.bpdlh.fdb.core.R as CoreR

/**
 * Created by hahn on 26/10/23.
 * Project: BPDLH App
 */
class DetailKegiatanKelompokBottomSheet: BaseDaggerBottomDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPendaftaranKelompokViewModel> { factory }
    private var _binding: FragmentDetailKegiatanKelompokBottomsheetBinding? = null
    private val binding get() = _binding!!

    private var type = KEGIATAN_KELOMPOK
    private var isUpdate = false
    private var idData = ""
    private var communicator: IDetailKegiatanKelompokCommunicator? = null
    private var listKategori = mutableListOf<KategoriKegiatanEntity>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(STYLE_NORMAL, CoreR.style.BottomSheetDialogTheme)
        communicator = parentFragment as? IDetailKegiatanKelompokCommunicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE, KEGIATAN_KELOMPOK)
            isUpdate = it.getBoolean(IS_UPDATE, false)
            idData = it.getString(ID_DATA, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailKegiatanKelompokBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(cl)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        initUI()
        setTitle()
        if (type == KEGIATAN_KELOMPOK) {
            viewModel.getKategoriKegiatanValue()?.data?.let { listKategori.addAll(it) }
        } else if (type == GAMBARAN_UMUM) {
            viewModel.getKategoriUsahaDibiayaiValue()?.data?.let { listKategori.addAll(it) }
        }
    }

    private fun initUI() {
        with(binding) {
            tilItemCategory.editText?.setText(arguments?.getString(CATEGORY).orEmpty())
            tilItemDesc.editText?.setText(arguments?.getString(DESCRIPTION).orEmpty())
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listKategori)
            atvItemCategory.setAdapter(adapter)
            when(type) {
                KEGIATAN_KELOMPOK -> {
                    tvItemCategory.text = getString(R.string.text_kategori)
                    atvItemCategory.hint = getString(R.string.text_kategori_hint)
                    tvItemDesc.text = getString(R.string.text_deskripsi)
                    tilItemCategory.visible()
                    tilItemMitraUsaha.gone()
                    groupDesc.visible()
                }
                MITRA_USAHA -> {
                    tvItemCategory.text = getString(R.string.text_kategori_usaha)
                    tilItemMitraUsaha.visible()
                    tilItemMitraUsaha.editText?.setText(arguments?.getString(CATEGORY).orEmpty())
                    tilItemCategory.gone()
                    groupDesc.gone()
                }
                else -> {
                    tvItemCategory.text = getString(R.string.text_kategori_usaha)
                    atvItemCategory.hint = getString(R.string.text_kategori_hint)
                    tvItemDesc.text = getString(R.string.text_deskripsi)
                    groupDesc.visible()
                    tilItemCategory.visible()
                    tilItemMitraUsaha.gone()
                }
            }
            btnSubmit.setOnClickListener {
                validateInput()
            }
            btnBatal.setOnClickListener {
                dismiss()
            }
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun validateInput() {
        with(binding) {
            val errCategory = tilItemCategory.editText?.text.toString().getError(requireContext())
            tilItemCategory.error = errCategory

            val errDescription = tilItemDesc.editText?.text.toString().getError(requireContext())
            tilItemDesc.error = errDescription

            val errMitraUsaha = tilItemMitraUsaha.editText?.text.toString().getError(requireContext())
            tilItemMitraUsaha.error = errMitraUsaha

            val category = tilItemCategory.editText?.text.toString()
            val description = tilItemDesc.editText?.text.toString()
            val mitraUsaha = tilItemMitraUsaha.editText?.text.toString()

            if (type == KEGIATAN_KELOMPOK || type == GAMBARAN_UMUM) {
                if (errCategory.isNullOrEmpty() && errDescription.isNullOrEmpty()) {
                    if (isUpdate) {
                        communicator?.updateData(idData, category, description)
                    } else {
                        communicator?.addData(category, description)
                    }
                    dismiss()
                }
            } else if (type == MITRA_USAHA){
                if (errMitraUsaha.isNullOrEmpty()) {
                    if (isUpdate) {
                        communicator?.updateData(idData, mitraUsaha)
                    } else {
                        communicator?.addData(mitraUsaha)

                    }
                    dismiss()
                }
            }
        }
    }

    private fun setTitle() {
        val title = when(type) {
            KEGIATAN_KELOMPOK -> getString(R.string.tambah_kegiatan_kelompok)
            MITRA_USAHA -> getString(R.string.text_mitra_usaha_title)
            else -> getString(R.string.text_gambaran_umum_title)
        }
        binding.tvTitle.text = title
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        const val TAG = "DetailKegiatanKelompokBottomSheet"
        const val TYPE = "type"
        const val CATEGORY = "CATEGORY"
        const val DESCRIPTION = "DESCRIPTION"
        const val IS_UPDATE = "IS_UPDATE"
        const val ID_DATA = "ID_DATA"
        const val KEGIATAN_KELOMPOK = "KEGIATAN_KELOMPOK"
        const val GAMBARAN_UMUM = "GAMBARAN_UMUM"
        const val MITRA_USAHA = "MITRA_USAHA"

        fun newInstance(
            type: String,
            category: String = "",
            description: String = "",
            isUpdate: Boolean = false,
            id: String = ""
        ) = DetailKegiatanKelompokBottomSheet().apply {
            arguments = Bundle().apply {
                putString(TYPE, type)
                putString(CATEGORY, category)
                putString(DESCRIPTION, description)
                putBoolean(IS_UPDATE, isUpdate)
                putString(ID_DATA, id)
            }
        }
    }
}