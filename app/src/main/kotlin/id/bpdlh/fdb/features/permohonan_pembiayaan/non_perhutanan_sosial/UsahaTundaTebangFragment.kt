package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.currencyFormatter
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.extractNumber
import id.bpdlh.fdb.core.common.utils.formatDecimal
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.databinding.FragmentUsahaTundaTebangBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.MitraItem
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

class UsahaTundaTebangFragment : BaseRegistrationFragment(), TextWatcherTextChange {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }

    private var _binding: FragmentUsahaTundaTebangBinding? = null
    private val binding get() = _binding!!
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private var requestCode = 0
    private var laporanLabaRugiFile: File? = null
    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null) {
                    if (requestCode == Constants.REQUEST_CODE_LAPORAN_LABA_RUGI) {
                        laporanLabaRugiFile = uriToFile(uri, requireContext())
                        if (laporanLabaRugiFile != null) viewModel.formulirPembiayaanNonPerhutananSosial.laporan_laba_rugi_path =
                            (laporanLabaRugiFile as File).path
                        binding.cvLaporanLabaRugi.showPreviewImage(uri, requireContext())
                    }
                }
            }
        }
    }

    override fun isValid(): Boolean {
        binding.apply {
            val jenisUsahaError =
                textILJenisUsaha.editText?.text.toString().getError(requireContext())
            textILJenisUsaha.error(jenisUsahaError)
            if (jenisUsahaError != null) return false
            val komoditasUsahaError =
                textILKomoditasUsaha.editText?.text.toString().getError(requireContext())
            textILKomoditasUsaha.error(komoditasUsahaError)
            if (komoditasUsahaError != null) return false
            val satuanWaktuError =
                textILLamaUsaha.editText?.text.toString().getError(requireContext())
            textILLamaUsaha.error(satuanWaktuError)
            if (satuanWaktuError != null) return false
            val lamaUsahaError =
                textILLamaUsaha2.editText?.text.toString().getError(requireContext())
            textILLamaUsaha2.error(lamaUsahaError)
            if (lamaUsahaError != null) return false
            val produktivitasTerakhirError =
                textILProduktivitasHasilPanenTerakhir.editText?.text.toString()
                    .getError(requireContext())
            textILProduktivitasHasilPanenTerakhir.error(produktivitasTerakhirError)
            if (produktivitasTerakhirError != null) return false
            val hargaError = textILHargaPenjualanTerakhir.editText?.text.toString()
                .getError(requireContext())
            textILHargaPenjualanTerakhir.error(hargaError)
            if (hargaError != null) return false
            val luasLahanError = textILLuasLahanYangDigarap.editText?.text.toString()
                .getError(requireContext())
            textILLuasLahanYangDigarap.error(luasLahanError)
            if (luasLahanError != null) return false
            val omzetUsahaError =
                textILOmzetUsaha.editText?.text.toString().getError(requireContext())
            textILOmzetUsaha.error(omzetUsahaError)
            if (omzetUsahaError != null) return false
            val hppError =
                textILHppModal.editText?.text.toString().getError(requireContext())
            textILHppModal.error(hppError)
            if (hppError != null) return false
            val pendapatanBersihError =
                textILPendapatanBersih.editText?.text.toString().getError(requireContext())
            textILPendapatanBersih.error(pendapatanBersihError)
            if (pendapatanBersihError != null) return false
            val tujuanPemasaranError =
                textILTujuanPemasaran.editText?.text.toString().getError(requireContext())
            textILTujuanPemasaran.error(tujuanPemasaranError)
            if (tujuanPemasaranError != null) return false
            val siklusUsahaError =
                textILSiklusUsaha.editText?.text.toString()
                    .getError(requireContext())
            textILSiklusUsaha.error(siklusUsahaError)
            if (siklusUsahaError != null) return false
            val siklusUsaha2Error =
                textILSiklusUsaha2.editText?.text.toString()
                    .getError(requireContext())
            textILSiklusUsaha2.error(siklusUsaha2Error)
            if (siklusUsaha2Error != null) return false
            val kuantitasKomoditasError =
                textILKuantitasKomoditasYangSudahAda.editText?.text.toString()
                    .getError(requireContext())
            textILKuantitasKomoditasYangSudahAda.error(kuantitasKomoditasError)
            if (kuantitasKomoditasError != null) return false
            val rencanaPenggunaanError =
                textILRencanaPenggunaanDanaPinjaman.editText?.text.toString()
                    .getError(requireContext())
            textILRencanaPenggunaanDanaPinjaman.error(rencanaPenggunaanError)
            if (rencanaPenggunaanError != null) return false
            val tujuanPengajuanError =
                textILJelaskanTujuanPengajuanPinjamanAnda.editText?.text.toString()
                    .getError(requireContext())
            textILJelaskanTujuanPengajuanPinjamanAnda.error(tujuanPengajuanError)
            if (tujuanPengajuanError != null) return false
            if (cvLaporanLabaRugi.ivFile.isGone && cvLaporanLabaRugi.clPreviewFile.isGone) {
                requireContext().showToast(
                    getString(R.string.wajib_upload_laporan_laba_rugi),
                    Constants.WARNING
                )
                return false
            }
        }
        return true
    }

    override fun initUI() {
        val lamaUsahaArray = resources.getStringArray(R.array.lama_usaha)
        val siklusUsahaArray = resources.getStringArray(R.array.siklus_usaha)
        binding.apply {
            viewModel.formulirPembiayaanNonPerhutananSosialResult.observe(
                viewLifecycleOwner
            ) { draft ->
                draft?.let {
                    edtJenisUsaha.setText(it.jenis_usaha)
                    edtKomoditasUsaha.setText(it.komoditas_usaha)
                    edtLamaUsaha.setText(it.satuan_lama_usaha)
                    edtLamaUsaha2.setText(it.lama_usaha.toString())
                    edtProduktivitasHasilPanenTerakhir.setText(it.produktivitas)
                    edtHargaPenjualanTerakhir.setText(currencyFormatter(it.harga, false))
                    edtLuasLahanYangDigarap.setText(it.luas_lahan.formatDecimal().replace(",", "."))
                    edtOmzetUsaha.setText(currencyFormatter(it.omzet, false))
                    edtHppModal.setText(currencyFormatter(it.hpp, false))
                    edtPendapatanBersih.setText(currencyFormatter(it.pendapatan_bersih, false))
                    edtTujuanPemasaran.setText(it.tujuan_pemasaran)
                    if (it.tipe_usaha == Constants.DIKELOLA_SENDIRI) rbDikelolaSendiri.isChecked =
                        true
                    else if (it.tipe_usaha == Constants.DIKELOLA_ORANG_LAIN) rbDikelolaOrangLain.isChecked =
                        true
                    edtSiklusUsaha.setText(it.satuan_siklus_usaha)
                    edtSiklusUsaha2.setText(it.siklus_usaha.toString())
                    edtKuantitasKomoditasYangSudahAda.setText(it.kuantitas_komoditas)
                    edtRencanaPenggunaanDanaPinjaman.setText(it.rencana_penggunaan)
                    if (it.tujuan_pengajuan == Constants.MODAL_KERJA) rbTujuanPengajuanPinjaman1.isChecked =
                        true
                    else if (it.tujuan_pengajuan == Constants.INVESTASI) rbTujuanPengajuanPinjaman2.isChecked =
                        true
                    edtJelaskanTujuanPengajuanPinjamanAnda.setText(it.penjelasan_tujuan)
                }
            }
            val lamaUsahaAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    lamaUsahaArray
                )
            edtLamaUsaha.setAdapter(lamaUsahaAdapter)
            showMitraList()
            val siklusUsahaAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    siklusUsahaArray
                )
            edtSiklusUsaha.setAdapter(siklusUsahaAdapter)
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
        _binding = FragmentUsahaTundaTebangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            textILJenisUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILKomoditasUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILLamaUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILLamaUsaha2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILProduktivitasHasilPanenTerakhir.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILHargaPenjualanTerakhir.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            edtHargaPenjualanTerakhir.setOnFocusChangeListener { _, isFocus ->
                val strHargaPenjualan = edtHargaPenjualanTerakhir.text.toString()
                if (strHargaPenjualan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strHargaPenjualan.extractNumber())
                        edtHargaPenjualanTerakhir.setText(strHargaPenjualan.extractNumber())
                    } else edtHargaPenjualanTerakhir.setText(
                        currencyFormatter(
                            strHargaPenjualan.toLong(),
                            false
                        )
                    )
                }
            }
            textILLuasLahanYangDigarap.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILOmzetUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            edtOmzetUsaha.setOnFocusChangeListener { _, isFocus ->
                val strOmzetUsaha = edtOmzetUsaha.text.toString()
                if (strOmzetUsaha.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strOmzetUsaha.extractNumber())
                        edtOmzetUsaha.setText(strOmzetUsaha.extractNumber())
                    } else edtOmzetUsaha.setText(
                        currencyFormatter(
                            strOmzetUsaha.toLong(),
                            false
                        )
                    )
                }
            }
            textILHppModal.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            edtHppModal.setOnFocusChangeListener { _, isFocus ->
                val strHppModal = edtHppModal.text.toString()
                if (strHppModal.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strHppModal.extractNumber())
                        edtHppModal.setText(strHppModal.extractNumber())
                    } else edtHppModal.setText(
                        currencyFormatter(
                            strHppModal.toLong(),
                            false
                        )
                    )
                }
            }
            textILPendapatanBersih.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            edtPendapatanBersih.setOnFocusChangeListener { _, isFocus ->
                val strPendapatanBersih = edtPendapatanBersih.text.toString()
                if (strPendapatanBersih.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPendapatanBersih.extractNumber())
                        edtPendapatanBersih.setText(strPendapatanBersih.extractNumber())
                    } else edtPendapatanBersih.setText(
                        currencyFormatter(
                            strPendapatanBersih.toLong(),
                            false
                        )
                    )
                }
            }
            textILTujuanPemasaran.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            rvMitraUsaha.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahMitraUsaha.setOnClickListener {
                viewModel.insertMitra("")
                showMitraList()
            }
            rbDikelolaSendiri.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.formulirPembiayaanNonPerhutananSosial.tipe_usaha =
                    Constants.DIKELOLA_SENDIRI
            }
            rbDikelolaOrangLain.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.formulirPembiayaanNonPerhutananSosial.tipe_usaha =
                    Constants.DIKELOLA_ORANG_LAIN
            }
            textILSiklusUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILSiklusUsaha2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILKuantitasKomoditasYangSudahAda.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            textILRencanaPenggunaanDanaPinjaman.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            rbTujuanPengajuanPinjaman1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.formulirPembiayaanNonPerhutananSosial.tujuan_pengajuan =
                    Constants.MODAL_KERJA
            }
            rbTujuanPengajuanPinjaman2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.formulirPembiayaanNonPerhutananSosial.tujuan_pengajuan =
                    Constants.INVESTASI
            }
            textILJelaskanTujuanPengajuanPinjamanAnda.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaTundaTebangFragment)
            }
            cvLaporanLabaRugi.apply {
                root.setOnClickListener {
                    chooseLaporanLabaRugi()
                }
                clUploadFile.setOnClickListener {
                    chooseLaporanLabaRugi()
                }
                tvGantiData.setOnClickListener {
                    chooseLaporanLabaRugi()
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                }
            }
        }
    }

    private fun chooseLaporanLabaRugi() {
        requestCode = Constants.REQUEST_CODE_LAPORAN_LABA_RUGI
        startFileChooser(launcherIntentFileChooser)
    }

    private fun showMitraList() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var position = 0
        executor.execute {
            val items =
                viewModel.mitraList.map { mitra ->
                    MitraItem.toViewItem(
                        mitra.nama, position++,
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
                    viewModel.formulirPembiayaanNonPerhutananSosial.jenis_usaha =
                        strJenisUsaha
                    textILJenisUsaha.error = strJenisUsaha.getError(requireContext())
                }

                textILKomoditasUsaha.editText -> {
                    val strKomoditasUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.komoditas_usaha =
                        strKomoditasUsaha
                    textILKomoditasUsaha.error = strKomoditasUsaha.getError(requireContext())
                }

                textILLamaUsaha.editText -> {
                    val strLamaUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.satuan_lama_usaha =
                        strLamaUsaha
                    textILLamaUsaha.error = strLamaUsaha.getError(requireContext())
                }

                textILLamaUsaha2.editText -> {
                    val strLamaUsaha2 = editText?.text.toString()
                    if (strLamaUsaha2.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.lama_usaha =
                        strLamaUsaha2.toInt()
                    textILLamaUsaha2.error = strLamaUsaha2.getError(requireContext())
                }

                textILProduktivitasHasilPanenTerakhir.editText -> {
                    val strProduktivitas = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.produktivitas =
                        strProduktivitas
                    textILProduktivitasHasilPanenTerakhir.error =
                        strProduktivitas.getError(requireContext())
                }

                textILHargaPenjualanTerakhir.editText -> {
                    val strHargaPenjualan = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.harga =
                        strHargaPenjualan.extractNumber().toLong()
                    textILHargaPenjualanTerakhir.error =
                        strHargaPenjualan.getError(requireContext())
                }

                textILLuasLahanYangDigarap.editText -> {
                    val strLuasLahan = editText?.text.toString()
                    if (strLuasLahan.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.luas_lahan =
                        strLuasLahan.toDouble()
                    textILLuasLahanYangDigarap.error = strLuasLahan.getError(requireContext())
                }

                textILOmzetUsaha.editText -> {
                    val strOmzetUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.omzet =
                        strOmzetUsaha.extractNumber().toLong()
                    textILOmzetUsaha.error = strOmzetUsaha.getError(requireContext())
                }

                textILHppModal.editText -> {
                    val strHppModal = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.hpp =
                        strHppModal.extractNumber().toLong()
                    textILHppModal.error = strHppModal.getError(requireContext())
                }

                textILPendapatanBersih.editText -> {
                    val strPendapatanBersih = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.pendapatan_bersih =
                        strPendapatanBersih.extractNumber().toLong()
                    textILPendapatanBersih.error = strPendapatanBersih.getError(requireContext())
                }

                textILTujuanPemasaran.editText -> {
                    val strTujuanPemasaran = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.tujuan_pemasaran =
                        strTujuanPemasaran
                    textILTujuanPemasaran.error = strTujuanPemasaran.getError(requireContext())
                }

                textILSiklusUsaha.editText -> {
                    val strSiklusUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.satuan_siklus_usaha =
                        strSiklusUsaha
                    textILSiklusUsaha.error = strSiklusUsaha.getError(requireContext())
                }

                textILSiklusUsaha2.editText -> {
                    val strSiklusUsaha2 = editText?.text.toString()
                    if (strSiklusUsaha2.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.siklus_usaha =
                        strSiklusUsaha2.toInt()
                    textILSiklusUsaha2.error = strSiklusUsaha2.getError(requireContext())
                }

                textILKuantitasKomoditasYangSudahAda.editText -> {
                    val strKuantitasKomoditas = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.kuantitas_komoditas =
                        strKuantitasKomoditas
                    textILKuantitasKomoditasYangSudahAda.error =
                        strKuantitasKomoditas.getError(requireContext())
                }

                textILRencanaPenggunaanDanaPinjaman.editText -> {
                    val strRencanaPenggunaan = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.rencana_penggunaan =
                        strRencanaPenggunaan
                    textILRencanaPenggunaanDanaPinjaman.error =
                        strRencanaPenggunaan.getError(requireContext())
                }

                textILJelaskanTujuanPengajuanPinjamanAnda.editText -> {
                    val strTujuanPengajuan = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.penjelasan_tujuan =
                        strTujuanPengajuan
                    textILJelaskanTujuanPengajuanPinjamanAnda.error =
                        strTujuanPengajuan.getError(requireContext())
                }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}