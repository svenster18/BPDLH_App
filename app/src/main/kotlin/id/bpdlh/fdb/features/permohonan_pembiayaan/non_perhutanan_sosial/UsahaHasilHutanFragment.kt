package id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial

import android.app.Activity
import android.net.Uri
import android.os.Bundle
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
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.databinding.FragmentUsahaHasilHutanBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.MitraItem
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class UsahaHasilHutanFragment : BaseRegistrationFragment(), TextWatcherTextChange {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPermohonanViewModel> { factory }

    private var _binding: FragmentUsahaHasilHutanBinding? = null
    private val binding get() = _binding!!
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private val mitraList = arrayListOf<String>()
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
            val komoditasHhbkError =
                textILKomoditasHhbk.editText?.text.toString().getError(requireContext())
            textILKomoditasHhbk.error(komoditasHhbkError)
            if (komoditasHhbkError != null) return false
            val jenisKegiatanError =
                textILJenisKegiatanPengelolaan.editText?.text.toString().getError(requireContext())
            textILJenisKegiatanPengelolaan.error(jenisKegiatanError)
            if (jenisKegiatanError != null) return false
            val satuanWaktuError =
                textILLamaUsaha.editText?.text.toString().getError(requireContext())
            textILLamaUsaha.error(satuanWaktuError)
            if (satuanWaktuError != null) return false
            val lamaUsahaError =
                textILLamaUsaha2.editText?.text.toString().getError(requireContext())
            textILLamaUsaha2.error(lamaUsahaError)
            if (lamaUsahaError != null) return false
            val sumberBahanLokasiError =
                textILSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.editText?.text.toString()
                    .getError(requireContext())
            textILSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.error(sumberBahanLokasiError)
            if (sumberBahanLokasiError != null) return false
            val kapasitasUsahaError = textILKapasitasUsahaProduksi.editText?.text.toString()
                .getError(requireContext())
            textILKapasitasUsahaProduksi.error(kapasitasUsahaError)
            if (kapasitasUsahaError != null) return false
            val hargaJualError = textILHargaJualTerakhirPerKgLiter.editText?.text.toString()
                .getError(requireContext())
            textILHargaJualTerakhirPerKgLiter.error(hargaJualError)
            if (hargaJualError != null) return false
            val tujuanPemasaranError =
                textILTujuanPemasaran.editText?.text.toString().getError(requireContext())
            textILTujuanPemasaran.error(tujuanPemasaranError)
            if (tujuanPemasaranError != null) return false
            val tujuanPengajuanError =
                textILJelaskanTujuanPengajuanPinjamanAnda.editText?.text.toString()
                    .getError(requireContext())
            textILJelaskanTujuanPengajuanPinjamanAnda.error(tujuanPengajuanError)
            if (tujuanPengajuanError != null) return false
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
            val pendapatanBersihError =
                textILPerkiraanPendapatanBersih.editText?.text.toString().getError(requireContext())
            textILPerkiraanPendapatanBersih.error(pendapatanBersihError)
            if (pendapatanBersihError != null) return false
            val siklusUsahaError =
                textILSiklusUsahaProduksi.editText?.text.toString()
                    .getError(requireContext())
            textILSiklusUsahaProduksi.error(siklusUsahaError)
            if (siklusUsahaError != null) return false
            val siklusUsaha2Error =
                textILSiklusUsahaProduksi2.editText?.text.toString()
                    .getError(requireContext())
            textILSiklusUsahaProduksi2.error(siklusUsaha2Error)
            if (siklusUsaha2Error != null) return false
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
            category?.let {
                if (it == Constants.NON_KEHUTANAN) {
                    tv2.text = getString(R.string.jenis_usaha_non_kehutanan)
                    edtKomoditasHhbk.hint = getString(R.string.masukan_jenis_usaha_non_kehutanan)
                    tv3.text = getString(R.string.komoditas_usaha)
                    edtJenisKegiatanPengelolaan.hint = getString(R.string.masukan_komoditas_usaha)
                    tv5.text = getString(R.string.produktivitas_hasil_panen_terakhir)
                    edtKomoditasHhbk.hint =
                        getString(R.string.masukan_produktivitas_hasil_panen_terakhir)
                    tv7.text = getString(R.string.harga_jual_terakhir)
                }
            }
            viewModel.formulirPembiayaanNonPerhutananSosialResult.observe(
                viewLifecycleOwner
            ) { draft ->
                draft?.let {
                    edtKomoditasHhbk.setText(it.komoditas_hhbk)
                    edtJenisKegiatanPengelolaan.setText(it.jenis_kegiatan)
                    edtLamaUsaha.setText(it.satuan_lama_usaha)
                    edtLamaUsaha2.setText(it.lama_usaha.toString())
                    edtSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.setText(it.sumber_bahan_baku)
                    edtKapasitasUsahaProduksi.setText(it.kapasitas_usaha)
                    edtHargaJualTerakhirPerKgLiter.setText(currencyFormatter(it.harga, false))
                    edtPerkiraanOmzetHargaJualPendapatanKotor.setText(
                        currencyFormatter(
                            it.omzet,
                            false
                        )
                    )
                    edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(
                        currencyFormatter(
                            it.hpp,
                            false
                        )
                    )
                    edtPerkiraanPendapatanBersih.setText(
                        currencyFormatter(
                            it.pendapatan_bersih,
                            false
                        )
                    )
                    edtTujuanPemasaran.setText(it.tujuan_pemasaran)
                    edtSiklusUsahaProduksi.setText(it.satuan_siklus_usaha)
                    edtSiklusUsahaProduksi2.setText(it.siklus_usaha.toString())
                    if (it.tujuan_pengajuan == Constants.MODAL_KERJA) rbOpsi1.isChecked =
                        true
                    else if (it.tujuan_pengajuan == Constants.INVESTASI) rbOpsi2.isChecked =
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
            val siklusUsahaAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    siklusUsahaArray
                )
            edtSiklusUsahaProduksi.setAdapter(siklusUsahaAdapter)
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
        _binding = FragmentUsahaHasilHutanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            textILKomoditasHhbk.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILJenisKegiatanPengelolaan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILLamaUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILLamaUsaha2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILKapasitasUsahaProduksi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILHargaJualTerakhirPerKgLiter.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            edtHargaJualTerakhirPerKgLiter.setOnFocusChangeListener { _, isFocus ->
                val strHargaPenjualan = edtHargaJualTerakhirPerKgLiter.text.toString()
                if (strHargaPenjualan.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strHargaPenjualan.extractNumber())
                        edtHargaJualTerakhirPerKgLiter.setText(strHargaPenjualan.extractNumber())
                    } else edtHargaJualTerakhirPerKgLiter.setText(
                        currencyFormatter(
                            strHargaPenjualan.toLong(),
                            true
                        )
                    )
                }
            }
            textILTujuanPemasaran.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            rvMitraUsaha.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahMitraUsaha.setOnClickListener {
                viewModel.insertMitra("")
                showMitraList()
            }
            rbOpsi1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.formulirPembiayaanNonPerhutananSosial.tujuan_pengajuan =
                    Constants.MODAL_KERJA
            }
            rbOpsi2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) viewModel.formulirPembiayaanNonPerhutananSosial.tipe_usaha =
                    Constants.INVESTASI
            }
            textILJelaskanTujuanPengajuanPinjamanAnda.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILPerkiraanOmzetHargaJualPendapatanKotor.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            edtPerkiraanOmzetHargaJualPendapatanKotor.setOnFocusChangeListener { _, isFocus ->
                val strOmzetUsaha = edtPerkiraanOmzetHargaJualPendapatanKotor.text.toString()
                if (strOmzetUsaha.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strOmzetUsaha.extractNumber())
                        edtPerkiraanOmzetHargaJualPendapatanKotor.setText(strOmzetUsaha.extractNumber())
                    } else edtPerkiraanOmzetHargaJualPendapatanKotor.setText(
                        currencyFormatter(
                            strOmzetUsaha.toLong(),
                            true
                        )
                    )
                }
            }
            textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setOnFocusChangeListener { _, isFocus ->
                val strHppModal =
                    edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.text.toString()
                if (strHppModal.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strHppModal.extractNumber())
                        edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(strHppModal.extractNumber())
                    } else edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(
                        currencyFormatter(
                            strHppModal.toLong(),
                            true
                        )
                    )
                }
            }
            textILPerkiraanPendapatanBersih.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            edtPerkiraanPendapatanBersih.setOnFocusChangeListener { _, isFocus ->
                val strPendapatanBersih = edtPerkiraanPendapatanBersih.text.toString()
                if (strPendapatanBersih.isNotEmpty()) {
                    if (isFocus) {
                        Timber.d(strPendapatanBersih.extractNumber())
                        edtPerkiraanPendapatanBersih.setText(strPendapatanBersih.extractNumber())
                    } else edtPerkiraanPendapatanBersih.setText(
                        currencyFormatter(
                            strPendapatanBersih.toLong(),
                            true
                        )
                    )
                }
            }
            textILSiklusUsahaProduksi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
            }
            textILSiklusUsahaProduksi2.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaHasilHutanFragment)
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

    private fun showMitraList() = runBlocking {
        var position = 0
        val items =
            viewModel.mitraList.map { mitra ->
                MitraItem.toViewItem(mitra.nama, position++,
                    { newMitra, position ->
                        onChange(newMitra, position)
                    },
                    onDelete = { position ->
                        onDelete(position)
                    })
            }
        launch {
            itemAdapter.set(items)
        }
    }

    private fun onChange(mitra: String, position: Int) {
        viewModel.updateMitra(mitra, position)
    }

    private fun onDelete(position: Int) {
        viewModel.removeMitra(position)
        showMitraList()
    }

    private fun chooseLaporanLabaRugi() {
        requestCode = Constants.REQUEST_CODE_LAPORAN_LABA_RUGI
        startFileChooser(launcherIntentFileChooser)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        binding.apply {
            when (editText) {
                textILKomoditasHhbk.editText -> {
                    val strJenisUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.komoditas_hhbk =
                        strJenisUsaha
                    textILKomoditasHhbk.error = strJenisUsaha.getError(requireContext())
                }

                textILJenisKegiatanPengelolaan.editText -> {
                    val strKomoditasUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.jenis_kegiatan =
                        strKomoditasUsaha
                    textILJenisKegiatanPengelolaan.error =
                        strKomoditasUsaha.getError(requireContext())
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

                textILSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.editText -> {
                    val strProduktivitas = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.sumber_bahan_baku =
                        strProduktivitas
                    textILSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.error =
                        strProduktivitas.getError(requireContext())
                }

                textILKapasitasUsahaProduksi.editText -> {
                    val strHargaPenjualan = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.kapasitas_usaha =
                        strHargaPenjualan
                    textILKapasitasUsahaProduksi.error =
                        strHargaPenjualan.getError(requireContext())
                }

                textILHargaJualTerakhirPerKgLiter.editText -> {
                    val strLuasLahan = editText?.text.toString()
                    if (strLuasLahan.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.harga =
                        strLuasLahan.extractNumber().toLong()
                    textILHargaJualTerakhirPerKgLiter.error =
                        strLuasLahan.getError(requireContext())
                }

                textILTujuanPemasaran.editText -> {
                    val strTujuanPemasaran = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.tujuan_pemasaran =
                        strTujuanPemasaran
                    textILTujuanPemasaran.error = strTujuanPemasaran.getError(requireContext())
                }

                textILJelaskanTujuanPengajuanPinjamanAnda.editText -> {
                    val strTujuanPengajuan = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.penjelasan_tujuan =
                        strTujuanPengajuan
                    textILJelaskanTujuanPengajuanPinjamanAnda.error =
                        strTujuanPengajuan.getError(requireContext())
                }

                textILPerkiraanOmzetHargaJualPendapatanKotor.editText -> {
                    val strOmzetUsaha = editText?.text.toString()
                    if (strOmzetUsaha.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.omzet =
                        strOmzetUsaha.extractNumber().toLong()
                    textILPerkiraanOmzetHargaJualPendapatanKotor.error =
                        strOmzetUsaha.getError(requireContext())
                }

                textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText -> {
                    val strHppModal = editText?.text.toString()
                    if (strHppModal.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.hpp =
                        strHppModal.extractNumber().toLong()
                    textILPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.error =
                        strHppModal.getError(requireContext())
                }

                textILPerkiraanPendapatanBersih.editText -> {
                    val strPendapatanBersih = editText?.text.toString()
                    if (strPendapatanBersih.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.pendapatan_bersih =
                        strPendapatanBersih.extractNumber().toLong()
                    textILPerkiraanPendapatanBersih.error =
                        strPendapatanBersih.getError(requireContext())
                }

                textILSiklusUsahaProduksi.editText -> {
                    val strSiklusUsaha = editText?.text.toString()
                    viewModel.formulirPembiayaanNonPerhutananSosial.satuan_siklus_usaha =
                        strSiklusUsaha
                    textILSiklusUsahaProduksi.error = strSiklusUsaha.getError(requireContext())
                }

                textILSiklusUsahaProduksi2.editText -> {
                    val strSiklusUsaha = editText?.text.toString()
                    if (strSiklusUsaha.isNotEmpty()) viewModel.formulirPembiayaanNonPerhutananSosial.siklus_usaha =
                        strSiklusUsaha.toInt()
                    textILSiklusUsahaProduksi2.error = strSiklusUsaha.getError(requireContext())
                }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}