package id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.databinding.FragmentDataPermohonanPinjamanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

class DataPermohonanPinjamanFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }
    private var _binding: FragmentDataPermohonanPinjamanBinding? = null
    private val binding get() = _binding!!
    private var itemAdapter = ItemAdapter<ViewItem<*>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDataPermohonanPinjamanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            val jenisUsahaError =
                textILJenisUsaha.editText?.text.toString().getError(requireContext())
            textILJenisUsaha.error(jenisUsahaError)
            if (jenisUsahaError != null) return false
            val komoditasUsahaError =
                textILKomoditasUsahaPs.editText?.text.toString().getError(requireContext())
            textILKomoditasUsahaPs.error(komoditasUsahaError)
            if (komoditasUsahaError != null) return false
            val satuanWaktuError =
                textILLamaUsaha.editText?.text.toString().getError(requireContext())
            textILLamaUsaha.error(satuanWaktuError)
            if (satuanWaktuError != null) return false
            val lamaUsahaError =
                textILLamaUsaha2.editText?.text.toString().getError(requireContext())
            textILLamaUsaha2.error(lamaUsahaError)
            if (lamaUsahaError != null) return false
            val sumberBahanError =
                textILSumberBahanProduksiJasa.editText?.text.toString().getError(requireContext())
            textILSumberBahanProduksiJasa.error(sumberBahanError)
            if (sumberBahanError != null) return false
            val kapasitasUsahaError = textILKapasitasUsahaProduksi.editText?.text.toString()
                .getGeneralError(requireContext())
            textILKapasitasUsahaProduksi.error(kapasitasUsahaError)
            if (kapasitasUsahaError != null) return false
            val nilaiEkonomiError = textILNilaiEkonomiUsahaRpProduksiJasa.editText?.text.toString()
                .getError(requireContext())
            textILNilaiEkonomiUsahaRpProduksiJasa.error(nilaiEkonomiError)
            if (nilaiEkonomiError != null) return false
            val tujuanPemasaranError =
                textILTujuanPemasaran.editText?.text.toString().getGeneralError(requireContext())
            textILTujuanPemasaran.error(tujuanPemasaranError)
            if (tujuanPemasaranError != null) return false
            val rencanaPenggunaanError =
                textILRencanaPenggunaanModalPinjaman.editText?.text.toString()
                    .getError(requireContext())
            textILRencanaPenggunaanModalPinjaman.error(rencanaPenggunaanError)
            if (rencanaPenggunaanError != null) return false
            val perkiraanOmzetError =
                textILPerkiraanOmzetHargaJualPendapatanKotor.editText?.text.toString()
                    .getError(requireContext())
            textILPerkiraanOmzetHargaJualPendapatanKotor.error(perkiraanOmzetError)
            if (perkiraanOmzetError != null) return false
            val perkiraanBiayaError =
                textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText?.text.toString()
                    .getError(requireContext())
            textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.error(perkiraanBiayaError)
            if (perkiraanBiayaError != null) return false
            val perkiraanPendapatanError =
                textILPerkiraanPendapatanBersih.editText?.text.toString().getError(requireContext())
            textILPerkiraanPendapatanBersih.error(perkiraanPendapatanError)
            if (perkiraanPendapatanError != null) return false
            val satuanSiklusError =
                textILSiklusUsahaProduksi.editText?.text.toString().getError(requireContext())
            textILSiklusUsahaProduksi.error(satuanSiklusError)
            if (satuanSiklusError != null) return false
            val siklusUsahaError =
                textILSiklusUsahaProduksi2.editText?.text.toString().getError(requireContext())
            textILSiklusUsahaProduksi2.error(siklusUsahaError)
            if (siklusUsahaError != null) return false
        }
        return true
    }

    override fun initUI() {
        val lamaUsahaArray = resources.getStringArray(R.array.lama_usaha)
        val siklusUsahaArray = resources.getStringArray(R.array.siklus_usaha)
        binding.apply {
            val lamaUsahaAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    lamaUsahaArray
                )
            edtLamaUsaha.setAdapter(lamaUsahaAdapter)
            val siklusUsahaAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    siklusUsahaArray
                )
            edtSiklusUsahaProduksi.setAdapter(siklusUsahaAdapter)
            val formPembiayaan = viewModel.formulirPembiayaanPerhutananSosial
            edtJenisUsaha.setText(formPembiayaan.businessType)
            edtKomoditasUsahaPs.setText(formPembiayaan.businessCommodity)
            edtLamaUsaha.setText(formPembiayaan.businessDurationUnit)
            if (formPembiayaan.businessDuration > 0) edtLamaUsaha2.setText(formPembiayaan.businessDuration.toString())
            edtSumberBahanProduksiJasa.setText(formPembiayaan.sourceOfProduction)
            edtKapasitasUsahaProduksi.setText(formPembiayaan.businessCapacity)
            if (formPembiayaan.businessEconomicValue > 0L) edtNilaiEkonomiUsahaRpProduksiJasa.setText(
                currencyFormatter(
                    formPembiayaan.businessEconomicValue,
                    false
                )
            )
            edtTujuanPemasaran.setText(formPembiayaan.marketingObjective)
            edtRencanaPenggunaanModalPinjaman.setText(formPembiayaan.usagePlan)
            if (formPembiayaan.estimatedTurnover > 0L) edtPerkiraanOmzetHargaJualPendapatanKotor.setText(
                currencyFormatter(
                    formPembiayaan.estimatedTurnover,
                    false
                )
            )
            if (formPembiayaan.estimatedProductionCost > 0L) edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(
                currencyFormatter(
                    formPembiayaan.estimatedProductionCost,
                    false
                )
            )
            if (formPembiayaan.estimatedNetIncome > 0L) edtPerkiraanPendapatanBersih.setText(
                currencyFormatter(
                    formPembiayaan.estimatedNetIncome,
                    false
                )
            )
            edtSiklusUsahaProduksi.setText(formPembiayaan.productionBusinessCycleUnit)
            if (formPembiayaan.productionBusinessCycle > 0) edtSiklusUsahaProduksi2.setText(
                formPembiayaan.productionBusinessCycle.toString()
            )
        }

    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textILJenisUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILKomoditasUsahaPs.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILLamaUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILLamaUsaha2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILSumberBahanProduksiJasa.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILKapasitasUsahaProduksi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILNilaiEkonomiUsahaRpProduksiJasa.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            edtNilaiEkonomiUsahaRpProduksiJasa.setOnFocusChangeListener { _, isFocus ->
                val strNilaiEkonomi = edtNilaiEkonomiUsahaRpProduksiJasa.text.toString()
                if (strNilaiEkonomi.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strNilaiEkonomi.extractNumber())
                        edtNilaiEkonomiUsahaRpProduksiJasa.setText(strNilaiEkonomi.extractNumber())
                    } else edtNilaiEkonomiUsahaRpProduksiJasa.setText(
                        currencyFormatter(
                            strNilaiEkonomi.toLong(),
                            true
                        )
                    )
                }
            }
            textILTujuanPemasaran.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            rvMitraUsaha.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahMitraUsaha.setOnClickListener {
                category?.let { viewModel.insertMitra("", it) }
                showMitraList()
            }
            textILRencanaPenggunaanModalPinjaman.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILPerkiraanOmzetHargaJualPendapatanKotor.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            edtPerkiraanOmzetHargaJualPendapatanKotor.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanOmzet = edtPerkiraanOmzetHargaJualPendapatanKotor.text.toString()
                if (strPerkiraanOmzet.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanOmzet.extractNumber())
                        edtPerkiraanOmzetHargaJualPendapatanKotor.setText(strPerkiraanOmzet.extractNumber())
                    } else edtPerkiraanOmzetHargaJualPendapatanKotor.setText(
                        currencyFormatter(
                            strPerkiraanOmzet.toLong(),
                            true
                        )
                    )
                }
            }
            textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanBiaya =
                    edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.text.toString()
                if (strPerkiraanBiaya.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanBiaya.extractNumber())
                        edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(
                            strPerkiraanBiaya.extractNumber()
                        )
                    } else edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(
                        currencyFormatter(strPerkiraanBiaya.toLong(), true)
                    )
                }
            }
            textILPerkiraanPendapatanBersih.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            edtPerkiraanPendapatanBersih.setOnFocusChangeListener { _, isFocus ->
                val strPerkiraanBiaya = edtPerkiraanPendapatanBersih.text.toString()
                if (strPerkiraanBiaya.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPerkiraanBiaya.extractNumber())
                        edtPerkiraanPendapatanBersih.setText(strPerkiraanBiaya.extractNumber())
                    } else edtPerkiraanPendapatanBersih.setText(
                        currencyFormatter(
                            strPerkiraanBiaya.toLong(),
                            true
                        )
                    )
                }
            }
            textILSiklusUsahaProduksi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
            textILSiklusUsahaProduksi2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DataPermohonanPinjamanFragment)
            }
        }
    }

    private fun showMitraList() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var position = 0
        executor.execute {
            val items = viewModel.mitraList.map { mitra ->
                MitraItem.toViewItem(mitra.nama, position++,
                    { newMitra, position ->
                        onChange(newMitra, position)
                    },
                    onDelete = { position ->
                        onDelete(position)
                    })
            }
            handler.post {
                itemAdapter.set(items)
            }
        }
    }

    private fun onChange(mitra: String, position: Int) {
        viewModel.updateMitra(mitra, position)
    }

    private fun onDelete(position: Int) {
        viewModel.removeMitra(position)
        showMitraList()
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.apply {
            when (editText) {
                textILJenisUsaha.editText -> {
                    val strJenisUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.businessType = strJenisUsaha
                    binding.textILJenisUsaha.error = strJenisUsaha.getError(requireContext())
                }

                textILKomoditasUsahaPs.editText -> {
                    val strKomoditasUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.businessCommodity =
                        strKomoditasUsaha
                    binding.textILKomoditasUsahaPs.error =
                        strKomoditasUsaha.getError(requireContext())
                }

                textILLamaUsaha.editText -> {
                    val strLamaUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.businessDurationUnit = strLamaUsaha
                    binding.textILLamaUsaha.error = strLamaUsaha.getError(requireContext())
                }

                textILLamaUsaha2.editText -> {
                    val strLamaUsaha = editText?.text.toString()
                    if (strLamaUsaha.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.businessDuration =
                        strLamaUsaha.toInt()
                    binding.textILLamaUsaha2.error = strLamaUsaha.getError(requireContext())
                }

                textILSumberBahanProduksiJasa.editText -> {
                    val strSumberBahan = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.sourceOfProduction = strSumberBahan
                    binding.textILSumberBahanProduksiJasa.error =
                        strSumberBahan.getError(requireContext())
                }

                textILKapasitasUsahaProduksi.editText -> {
                    val strKapasitasUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.businessCapacity =
                        strKapasitasUsaha
                    binding.textILKapasitasUsahaProduksi.error =
                        strKapasitasUsaha.getGeneralError(requireContext())
                }

                textILNilaiEkonomiUsahaRpProduksiJasa.editText -> {
                    val strNilaiEkonomi = editText?.text.toString()
                    if (strNilaiEkonomi.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.businessEconomicValue =
                        strNilaiEkonomi.extractNumber().toLong()
                    binding.textILNilaiEkonomiUsahaRpProduksiJasa.error =
                        strNilaiEkonomi.getError(requireContext())
                }

                textILTujuanPemasaran.editText -> {
                    val strTujuanPemasaran = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.marketingObjective =
                        strTujuanPemasaran
                    binding.textILTujuanPemasaran.error =
                        strTujuanPemasaran.getGeneralError(requireContext())
                }

                textILRencanaPenggunaanModalPinjaman.editText -> {
                    val strRencanaPenggunaan = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.usagePlan = strRencanaPenggunaan
                    binding.textILRencanaPenggunaanModalPinjaman.error =
                        strRencanaPenggunaan.getError(requireContext())
                }

                textILPerkiraanOmzetHargaJualPendapatanKotor.editText -> {
                    val strPerkiraanOmzet = editText?.text.toString()
                    if (strPerkiraanOmzet.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.estimatedTurnover =
                        strPerkiraanOmzet.extractNumber().toLong()
                    binding.textILPerkiraanOmzetHargaJualPendapatanKotor.error =
                        strPerkiraanOmzet.getError(requireContext())
                }

                textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText -> {
                    val strPerkiraanBiaya = editText?.text.toString()
                    if (strPerkiraanBiaya.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.estimatedProductionCost =
                        strPerkiraanBiaya.extractNumber().toLong()
                    binding.textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.error =
                        strPerkiraanBiaya.getError(requireContext())
                }

                textILPerkiraanPendapatanBersih.editText -> {
                    val strPerkiraanPendapatan = editText?.text.toString()
                    if (strPerkiraanPendapatan.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.estimatedNetIncome =
                        strPerkiraanPendapatan.extractNumber().toLong()
                    binding.textILPerkiraanPendapatanBersih.error =
                        strPerkiraanPendapatan.getError(requireContext())
                }

                textILSiklusUsahaProduksi.editText -> {
                    val strSiklusUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanPerhutananSosial.productionBusinessCycleUnit =
                        strSiklusUsaha
                    binding.textILSiklusUsahaProduksi.error =
                        strSiklusUsaha.getError(requireContext())
                }

                textILSiklusUsahaProduksi2.editText -> {
                    val strSiklusUsaha = editText?.text.toString()
                    if (strSiklusUsaha.isNotEmpty()) viewModel.formulirPembiayaanPerhutananSosial.productionBusinessCycle =
                        strSiklusUsaha.toInt()
                    binding.textILSiklusUsahaProduksi2.error =
                        strSiklusUsaha.getError(requireContext())
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