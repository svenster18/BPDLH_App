package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.convertISOTimeToDate
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.getCurrentDate
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.databinding.FragmentJaminanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class JaminanFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private var _binding: FragmentJaminanBinding? = null
    private val binding get() = _binding!!
    private var itemAdapter = ItemAdapter<ViewItem<*>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJaminanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            val dibuatTempatError =
                textILDibuatPadaTempat.editText?.text.toString().getGeneralError(requireContext())
            textILDibuatPadaTempat.error(dibuatTempatError)
            if (dibuatTempatError != null) return false
            if (!checkBox.isChecked) {
                requireContext().showToast(
                    getString(R.string.wajib_menyetujui_pernyataan),
                    Constants.WARNING
                )
                return false
            }
        }
        return true
    }

    override fun initUI() {
        binding.apply {
            val formPembiayaan = viewModel.formulirPembiayaanPerhutananSosial
            if (formPembiayaan.createdAt.isEmpty()) edtDibuatPadaTanggal.setText(getCurrentDate())
            else edtDibuatPadaTanggal.setText(formPembiayaan.createdAt.convertISOTimeToDate("dd/MM/yyyy"))
            edtDibuatPadaTempat.setText(formPembiayaan.createdIn)
        }
    }

    override fun disableEditText() {
        binding.checkBox.isChecked = true
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvTambahJaminan.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahJaminan.setOnClickListener {
                val tambahJaminanBottomSheet = TambahJaminanBottomSheet({ jaminan ->
                    viewModel.jaminanList.add(jaminan)
                    showJaminanList()
                })
                tambahJaminanBottomSheet.show(parentFragmentManager, TambahJaminanBottomSheet.TAG)
            }
            textILDibuatPadaTempat.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@JaminanFragment)
            }
        }
    }

    private fun showJaminanList() {
        var position = 0
        runBlocking {
            val items = viewModel.jaminanList.map { jaminan ->
                JaminanItem.toViewItem(
                    jaminan, position++, { _, position ->
                        onChangeClick(jaminan, position)
                    },
                    onDelete = { position ->
                        onDelete(position)
                    })
            }
            launch {
                itemAdapter.set(items)
            }
        }
    }

    private fun onChangeClick(jaminan: JaminanEntity, position: Int) {
        val tambahJaminanBottomSheet = TambahJaminanBottomSheet({ updatedJaminan ->
            viewModel.jaminanList[position] = updatedJaminan
            showJaminanList()
        }, jaminan)
        tambahJaminanBottomSheet.show(parentFragmentManager, TambahJaminanBottomSheet.TAG)
    }

    private fun onDelete(position: Int) {
        viewModel.jaminanList.removeAt(position)
        showJaminanList()
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.apply {
            when (editText) {
                textILDibuatPadaTempat.editText -> {
                    val dibuatPadaTempat = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.createdIn = dibuatPadaTempat
                    textILDibuatPadaTempat.error =
                        dibuatPadaTempat.getError(requireContext())
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