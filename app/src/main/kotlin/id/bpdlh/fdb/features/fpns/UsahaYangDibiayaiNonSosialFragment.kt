package id.bpdlh.fdb.features.fpns

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewItem
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getMimeType
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.removePreviewImage
import id.bpdlh.fdb.core.common.utils.showPreviewImage
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startFileChooser
import id.bpdlh.fdb.core.common.utils.uriToFile
import id.bpdlh.fdb.core.common.utils.visible
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.MitraEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.databinding.FragmentUsahaYangDibiayaiNonSosialBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.MitraItem
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

class UsahaYangDibiayaiNonSosialFragment : BaseFormPermohonanNonSosialFragment(), TextWatcherTextChange {

    private lateinit var binding: FragmentUsahaYangDibiayaiNonSosialBinding
    private var communicator: IFormPermohonanKelompokNonSosialCommunicator? = null

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormPermohonanNonSosialViewModel> { factory }
    private lateinit var progressDialog: CustomProgressDialog
    private var itemAdapter = ItemAdapter<ViewItem<*>>()
    private var labaRugiFile : File? = null
    private var requestCode = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormPermohonanKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsahaYangDibiayaiNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.resultBusinessPartner, ::observeBusinessPartner)
        observe(viewModel.labaRugiFileUploadResult, ::observeLabaRugiUpload)
        with(binding) {
            tvDescription.text = getString(R.string.form_permohonan_non_sosial_usaha_yang_dibiayai)
            tvStep.text = requireContext().getString(R.string.form_permohonan_non_sosial_text_stepper, "2")
            incFooter.btnKembali.setOnClickListener {
                communicator?.goToPage(0)
            }
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
            rvMitraUsaha.apply {
                adapter = FastAdapter.with(itemAdapter)
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnTambahMitraUsaha.setOnClickListener {
                viewModel.insertMitra("")
                showMitraList()
            }
        }
        formWatcher()
    }

    override fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        viewModel.getBusinessPartner(memberApplicationId)
        with(binding){
            val lamaUsahaArray = resources.getStringArray(R.array.lama_usaha)
            val lamaUsahaAdapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    lamaUsahaArray
                )
            edtLamaUsahaSatuan.setAdapter(lamaUsahaAdapter)
            edtSiklusUsahaProduksiUnit.setAdapter(lamaUsahaAdapter)
            radioTipeUsaha.setOnCheckedChangeListener { radioGroup, i ->
                val id = radioGroup.checkedRadioButtonId
                val radio: RadioButton = radioGroup.findViewById(id)
                memberApplicationPost.businessManagementType = radio.text.toString()
            }
            radioTujuanPengajuanPinjaman.setOnCheckedChangeListener { radioGroup, i ->
                val id = radioGroup.checkedRadioButtonId
                val radio: RadioButton = radioGroup.findViewById(id)
                memberApplicationPost.submissionPurpose = radio.text.toString()
            }
            cvLaporanLabaRugi.apply {
                root.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_LAPORAN_LABA_RUGI)
                }
                tvGantiData.setOnClickListener {
                    choseDocument(Constants.REQUEST_CODE_LAPORAN_LABA_RUGI)
                }
                btnTrash.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landTenureFile = null
                    root.setOnClickListener {
                        choseDocument(Constants.REQUEST_CODE_LAPORAN_LABA_RUGI)
                    }
                }
                btnTrashFile.setOnClickListener {
                    removePreviewImage()
                    memberApplicationPost.landTenureFile = null
                }
            }
            memberApplicationPost.businessManagementType?.let {
                when(it){
                    getString(R.string.dikelola_sendiri) -> radioTipeUsaha.check(radioDikelolaSendiri.id)
                    getString(R.string.dikelola_orang_lain) -> radioTipeUsaha.check(radioDikelolaOrangLain.id)
                }
            }
            labaRugiFileUrl?.let {
                cvLaporanLabaRugi.showPreviewImage(it, requireContext())
            }
            memberApplicationPost.businessType?.let {
                edtJenisUsaha.setText(it)
                edtJenisUsahaNonKehutanan.setText(it)
            }
            memberApplicationPost.businessCommodity?.let {
                edtKomoditasUsaha.setText(it)
                edtKomoditasHhbk.setText(it)
            }
            memberApplicationPost.sourceOfProduction?.let {
                edtSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.setText(it)

            }
            memberApplicationPost.businessDuration?.let {
                if(it != 0) edtLamaUsaha.setText(it.toString())
            }
            memberApplicationPost.businessDurationUnit?.let {
                edtLamaUsahaSatuan.setText(it)
            }
            memberApplicationPost.productivity?.let {
                edtProduktivitasHasilPanenTerakhir.setText(it)
            }
            memberApplicationPost.recentSale?.let {
                if(it != 0) edtHargaPenjualanTerakhir.setText(it.toString())
            }
            memberApplicationPost.landAreaCultivated?.let {
                if(it != 0) edtLuasLahanYangDiGarap.setText(it.toString())
            }
            memberApplicationPost.estimatedTurnover?.let {
                if(it != 0) edtOmzetUsaha.setText(it.toString())
            }
            memberApplicationPost.estimatedProductionCost?.let {
                if(it != 0) edtHppModal.setText(it.toString())
            }
            memberApplicationPost.marketingObjective?.let {
                edtTujuanPemasaran.setText(it)
            }
            memberApplicationPost.businessCommodity?.let {
               edtKuantitasKomoditasYangSudahAda.setText(it)
            }
            memberApplicationPost.businessCycle?.let {
                if(it != 0) {
                    edtSiklusUsaha.setText(it.toString())
                    edtSiklusUsahaProduksi.setText(it.toString())
                }
            }
            memberApplicationPost.businessCycleUnit?.let {
                edtSiklusUsahaProduksiUnit.setText(it)
            }
            memberApplicationPost.marketingObjective?.let {
                edtRencanaPenggunaanDanaPinjaman.setText(it)
            }
            memberApplicationPost.submissionPurpose?.let {
                when(it){
                    getString(R.string.untuk_modal_kerja) -> radioTujuanPengajuanPinjaman.check(radioUntukModalKerja.id)
                    getString(R.string.untuk_investasi) -> radioTujuanPengajuanPinjaman.check(radioUntukInventasi.id)
                }
            }
            memberApplicationPost.businessCapacity?.let {
                if(it != 0) edtKapasitasUsaha.setText(it.toString())
            }
            memberApplicationPost.detailSubmissionPurpose?.let {
                edtJelaskanTujuanPengajuanPinjamanAnda.setText(it)
                edtTujuanPengajuanPinjaman.setText(it)
            }
            memberApplicationPost.estimatedTurnover?.let {
                if(it != 0) edtPerkiraanOmzetHargaJualPendapatanKotor.setText(it.toString())
            }
            memberApplicationPost.estimatedProductionCost?.let {
                if(it != 0) edtPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.setText(it.toString())
            }
            memberApplicationPost.estimatedNetIncome?.let {
                if(it != 0) {
                    edtPerkiraanPendapatanBersih.setText(it.toString())
                    edtPendapatanBersih.setText(it.toString())
                }
            }
            memberApplicationPost.managementActivityType?.let {
                edtJenisKegiatanPengelolaan.setText(it)
            }
            memberApplicationPost.recentSale?.let {
                if(it != 0) edtHargaJualKg.setText(it.toString())
            }
            memberApplicationPost.businessCycle
            when(memberApplicationPost.serviceType) {
                Constants.TUNDA_TEBANG -> {
                    jenisUsaha.visible()
                    tilJenisUsaha.visible()
                    komoditasUsaha.visible()
                    tilKomiditasUsaha.visible()
                    lamaUsaha.visible()
                    tilLamaUsaha.visible()
                    tilLamaUsahaSatuan.visible()
                    produktivitasHasilPanenTerakhir.visible()
                    tilProduktivitasHasilPanenTerakhir.visible()
                    hargaPenjualanTerakhir.visible()
                    tilHargaPenjualanTerakhir.visible()
                    luasLahanYangDiGarap.visible()
                    tilLuasLahanYangDiGarap.visible()
                    omzetUsaha.visible()
                    tilOmzetUsaha.visible()
                    hppModal.visible()
                    tilHppModal.visible()
                    pendapatanBersih.visible()
                    tilPendapatanBersih.visible()
                    tujuanPemasaran.visible()
                    tilTujuanPemasaran.visible()
                    tipeUsaha.visible()
                    radioTipeUsaha.visible()
                    siklusUsaha.visible()
                    tilSiklusUsaha.visible()
                    kuantitasKomoditasYangSudahAda.visible()
                    tilKuantitasKomoditasYangSudahAda.visible()
                    rencanaPenggunaanDanaPinjaman.visible()
                    tilRencanaPenggunaanDanaPinjaman.visible()
                    jelaskanTujuanPengajuanPinjamanAnda.visible()
                    tilJelaskanTujuanPengajuanPinjamanAnda.visible()
                }
                Constants.HASIL_HUTAN_BUKAN_KAYU -> {
                    kondisiUsahaSaatIni.visible()
                    komoditasHhbk.visible()
                    tilKomoditasHhbk.visible()
                    jenisKegiatanPengelolaan.visible()
                    tilJenisKegiatanPengelolaan.visible()
                    lamaUsaha.visible()
                    tilLamaUsaha.visible()
                    tilLamaUsahaSatuan.visible()
                    sumberBahanBakuHhbkDanLokasiOnFarmOffFarm.visible()
                    tilSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.visible()
                    kapasitasUsaha.visible()
                    tilKapasitasUsaha.visible()
                    hargaJualKg.visible()
                    tilHargaJualKg.visible()
                    tujuanPemasaran.visible()
                    tilTujuanPemasaran.visible()

                    jelaskanTujuanPengajuanPinjamanAnda.visible()
                    tilJelaskanTujuanPengajuanPinjamanAnda.visible()
                    perkiraanOmzetHargaJualPendapatanKotor.visible()
                    tilPerkiraanOmzetHargaJualPendapatanKotor.visible()
                    perkiraanBiayaProduksiHppModalYangTelahDikeluarkan.visible()
                    tilPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.visible()
                    perkiraanPendapatanBersih.visible()
                    tilPerkiraanPendapatanBersih.visible()
                    siklusUsahaProduksi.visible()
                    tilSiklusUsahaProduksi.visible()
                    tilSiklusUsahaProduksiUnit.visible()
                    laporanLabaRugi.visible()
                    cvLaporanLabaRugi.apply {
                        root.visible()
                    }
                }
                Constants.KOMODITAS_NON_KEHUTANAN -> {
                    kondisiUsahaSaatIni.visible()
                    jenisUsahaNonKehutanan.visible()
                    tilJenisUsahaNonKehutanan.visible()
                    komoditasUsaha.visible()
                    tilKomiditasUsaha.visible()
                    lamaUsaha.visible()
                    tilLamaUsaha.visible()
                    tilLamaUsahaSatuan.visible()
                    produktivitasHasilPanenTerakhir.visible()
                    tilProduktivitasHasilPanenTerakhir.visible()
                    kapasitasUsaha.visible()
                    tilKapasitasUsaha.visible()
                    hargaPenjualanTerakhir.visible()
                    tilHargaPenjualanTerakhir.visible()
                    tujuanPemasaran.visible()
                    tilTujuanPemasaran.visible()
                    perkiraanOmzetHargaJualPendapatanKotor.visible()
                    tilPerkiraanOmzetHargaJualPendapatanKotor.visible()
                    perkiraanBiayaProduksiHppModalYangTelahDikeluarkan.visible()
                    tilPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.visible()
                    perkiraanPendapatanBersih.visible()
                    tilPerkiraanPendapatanBersih.visible()
                    siklusUsahaProduksi.visible()
                    tilSiklusUsahaProduksi.visible()
                    tilSiklusUsahaProduksiUnit.visible()
                    laporanLabaRugi.visible()
                    cvLaporanLabaRugi.apply {
                        root.visible()
                    }
                }
                else -> {}
            }
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilJenisUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilKomiditasUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilKomoditasHhbk.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilJenisKegiatanPengelolaan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilHargaJualKg.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilLamaUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilLamaUsahaSatuan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilProduktivitasHasilPanenTerakhir.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilHargaPenjualanTerakhir.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilLuasLahanYangDiGarap.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilOmzetUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilHppModal.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilPendapatanBersih.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilTujuanPemasaran.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilSiklusUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilKuantitasKomoditasYangSudahAda.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilRencanaPenggunaanDanaPinjaman.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilJelaskanTujuanPengajuanPinjamanAnda.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilJenisUsahaNonKehutanan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilKapasitasUsaha.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilPerkiraanOmzetHargaJualPendapatanKotor.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilPerkiraanPendapatanBersih.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilSiklusUsahaProduksi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
            tilSiklusUsahaProduksiUnit.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@UsahaYangDibiayaiNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val jenisUsaha = tilJenisUsaha.editText?.text.toString().getError(requireContext())
            jenisUsaha?.let { errorMessages.add(it) }
            tilJenisUsaha.error = jenisUsaha

            val komoditasUsaha = tilKomiditasUsaha.editText?.text.toString().getError(requireContext())
            komoditasUsaha?.let { errorMessages.add(it) }
            tilKomiditasUsaha.error = komoditasUsaha

            val komoditasUsahaHHBK = tilKomoditasHhbk.editText?.text.toString().getError(requireContext())
            komoditasUsahaHHBK?.let { errorMessages.add(it) }
            tilKomoditasHhbk.error = komoditasUsahaHHBK

            val jenisKegiatanPengelolaan = tilJenisKegiatanPengelolaan.editText?.text.toString().getError(requireContext())
            jenisKegiatanPengelolaan?.let { errorMessages.add(it) }
            tilJenisKegiatanPengelolaan.error = jenisKegiatanPengelolaan

            val sumberBahanBakuHhbkDanLokasiOnFarmOffFarm = tilSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.editText?.text.toString().getError(requireContext())
            sumberBahanBakuHhbkDanLokasiOnFarmOffFarm?.let { errorMessages.add(it) }
            tilSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.error = sumberBahanBakuHhbkDanLokasiOnFarmOffFarm

            val hargaJualKg = tilHargaJualKg.editText?.text.toString().getError(requireContext())
            hargaJualKg?.let { errorMessages.add(it) }
            tilHargaJualKg.error = hargaJualKg

            val lamaUsaha = tilLamaUsaha.editText?.text.toString().getError(requireContext())
            lamaUsaha?.let { errorMessages.add(it) }
            tilLamaUsaha.error = lamaUsaha

            val lamaUsahaSatuan = tilLamaUsahaSatuan.editText?.text.toString().getError(requireContext())
            lamaUsahaSatuan?.let { errorMessages.add(it) }
            tilLamaUsahaSatuan.error = lamaUsahaSatuan

            val produktivitasHasilPanenTerakhir = tilProduktivitasHasilPanenTerakhir.editText?.text.toString().getError(requireContext())
            produktivitasHasilPanenTerakhir?.let { errorMessages.add(it) }
            tilProduktivitasHasilPanenTerakhir.error = produktivitasHasilPanenTerakhir

            val hargaPenjualanTerakhir = tilHargaPenjualanTerakhir.editText?.text.toString().getError(requireContext())
            hargaPenjualanTerakhir?.let { errorMessages.add(it) }
            tilHargaPenjualanTerakhir.error = hargaPenjualanTerakhir

            val luasLahanYangDiGarap = tilLuasLahanYangDiGarap.editText?.text.toString().getError(requireContext())
            luasLahanYangDiGarap?.let { errorMessages.add(it) }
            tilLuasLahanYangDiGarap.error = luasLahanYangDiGarap

            val omzetUsaha = tilOmzetUsaha.editText?.text.toString().getError(requireContext())
            omzetUsaha?.let { errorMessages.add(it) }
            tilOmzetUsaha.error = omzetUsaha

            val hppModal = tilHppModal.editText?.text.toString().getError(requireContext())
            hppModal?.let { errorMessages.add(it) }
            tilHppModal.error = hppModal

            val pendapatanBersih = tilPendapatanBersih.editText?.text.toString().getError(requireContext())
            pendapatanBersih?.let { errorMessages.add(it) }
            tilPendapatanBersih.error = pendapatanBersih

            val tujuanPemasaran = tilTujuanPemasaran.editText?.text.toString().getError(requireContext())
            tujuanPemasaran?.let { errorMessages.add(it) }
            tilTujuanPemasaran.error = tujuanPemasaran

            val siklusUsaha = tilSiklusUsaha.editText?.text.toString().getError(requireContext())
            siklusUsaha?.let { errorMessages.add(it) }
            tilSiklusUsaha.error = siklusUsaha

            val kuantitasKomoditasYangSudahAda = tilKuantitasKomoditasYangSudahAda.editText?.text.toString().getError(requireContext())
            kuantitasKomoditasYangSudahAda?.let { errorMessages.add(it) }
            tilKuantitasKomoditasYangSudahAda.error = kuantitasKomoditasYangSudahAda

            val rencanaPenggunaanDanaPinjaman = tilRencanaPenggunaanDanaPinjaman.editText?.text.toString().getError(requireContext())
            rencanaPenggunaanDanaPinjaman?.let { errorMessages.add(it) }
            tilRencanaPenggunaanDanaPinjaman.error = rencanaPenggunaanDanaPinjaman

            val jelaskanTujuanPengajuanPinjamanAnda = tilJelaskanTujuanPengajuanPinjamanAnda.editText?.text.toString().getError(requireContext())
            jelaskanTujuanPengajuanPinjamanAnda?.let { errorMessages.add(it) }
            tilJelaskanTujuanPengajuanPinjamanAnda.error = jelaskanTujuanPengajuanPinjamanAnda

            if(radioTipeUsaha.checkedRadioButtonId <= 0 ) {
                radioDikelolaOrangLain.error = resources.getString(R.string.wajib_di_isi)
            }

            if(radioTujuanPengajuanPinjaman.checkedRadioButtonId <= 0 ) {
                radioUntukInventasi.error = resources.getString(R.string.wajib_di_isi)
            }

            val jenisUsahaNonKehutanan = tilJenisUsahaNonKehutanan.editText?.text.toString().getError(requireContext())
            jenisUsahaNonKehutanan?.let { errorMessages.add(it) }
            tilJenisUsahaNonKehutanan.error = jenisUsahaNonKehutanan

            val kapasitasUsaha = tilKapasitasUsaha.editText?.text.toString().getError(requireContext())
            kapasitasUsaha?.let { errorMessages.add(it) }
            tilKapasitasUsaha.error = kapasitasUsaha

            val perkiraanOmzetHargaJualPendapatanKotor = tilPerkiraanOmzetHargaJualPendapatanKotor.editText?.text.toString().getError(requireContext())
            perkiraanOmzetHargaJualPendapatanKotor?.let { errorMessages.add(it) }
            tilPerkiraanOmzetHargaJualPendapatanKotor.error = perkiraanOmzetHargaJualPendapatanKotor

            val perkiraanBiayaProduksiHppModalYangTelahDikeluarkan = tilPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText?.text.toString().getError(requireContext())
            perkiraanBiayaProduksiHppModalYangTelahDikeluarkan?.let { errorMessages.add(it) }
            tilPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.error = perkiraanBiayaProduksiHppModalYangTelahDikeluarkan

            val perkiraanPendapatanBersih = tilPerkiraanPendapatanBersih.editText?.text.toString().getError(requireContext())
            perkiraanPendapatanBersih?.let { errorMessages.add(it) }
            tilPerkiraanPendapatanBersih.error = perkiraanPendapatanBersih

            val siklusUsahaProduksi = tilSiklusUsahaProduksi.editText?.text.toString().getError(requireContext())
            siklusUsahaProduksi?.let { errorMessages.add(it) }
            tilSiklusUsahaProduksi.error = siklusUsahaProduksi

            val siklusUsahaProduksiUnit = tilSiklusUsahaProduksiUnit.editText?.text.toString().getError(requireContext())
            siklusUsahaProduksiUnit?.let { errorMessages.add(it) }
            tilSiklusUsahaProduksiUnit.error = siklusUsahaProduksiUnit
        }
//        if (errorMessages.size == 0) {
            communicator?.goToPage(2)
//        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText) {
            binding.tilJenisUsaha.editText -> {
                memberApplicationPost.businessType = editText?.text.toString()
            }
            binding.tilKomiditasUsaha.editText -> {
                memberApplicationPost.businessCommodity = editText?.text.toString()
            }
            binding.tilKomoditasHhbk.editText -> {
                memberApplicationPost.businessCommodity = editText?.text.toString()
            }
            binding.tilJenisKegiatanPengelolaan.editText -> {
                memberApplicationPost.managementActivityType = editText?.text.toString()
            }
            binding.tilSumberBahanBakuHhbkDanLokasiOnFarmOffFarm.editText -> {
                memberApplicationPost.sourceOfProduction = editText?.text.toString()
            }
            binding.tilLamaUsaha.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.businessDuration = it }
            }
            binding.tilLamaUsahaSatuan.editText -> {
                memberApplicationPost.businessDurationUnit = editText?.text.toString()
            }
            binding.tilProduktivitasHasilPanenTerakhir.editText -> {
                memberApplicationPost.productivity = editText?.text.toString()
            }
            binding.tilHargaPenjualanTerakhir.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.recentSale = it }
            }
            binding.tilHargaJualKg.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.recentSale = it }
            }
            binding.tilLuasLahanYangDiGarap.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.landAreaCultivated = it }
            }
            binding.tilOmzetUsaha.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.estimatedTurnover = it }
            }
            binding.tilHppModal.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.estimatedProductionCost = it }
            }
            binding.tilPendapatanBersih.editText -> {
                editText?.text.toString().toIntOrNull()?.let { memberApplicationPost.estimatedNetIncome = it }
            }
            binding.tilTujuanPemasaran.editText -> {
                memberApplicationPost.marketingObjective = editText?.text.toString()
            }
            binding.tilSiklusUsaha.editText -> {
                memberApplicationPost.businessCycleUnit = editText?.text.toString()
            }
            binding.tilKuantitasKomoditasYangSudahAda.editText -> {
                editText?.text.toString().toIntOrNull()?.let {  memberApplicationPost.qtyBusinessCommodity = it }
            }
//            binding.tilRencanaPenggunaanDanaPinjaman.editText -> {
//                memberApplicationPost.marketingObjective = editText?.text.toString()
//            }
            binding.tilJelaskanTujuanPengajuanPinjamanAnda.editText -> {
                memberApplicationPost.detailSubmissionPurpose = editText?.text.toString()
            }
            binding.tilJenisUsahaNonKehutanan.editText -> {
                memberApplicationPost.businessType = editText?.text.toString()
            }
            binding.tilKapasitasUsaha.editText -> {
                editText?.text.toString().toIntOrNull()?.let {  memberApplicationPost.businessCapacity = it }
            }
            binding.tilPerkiraanOmzetHargaJualPendapatanKotor.editText -> {
                editText?.text.toString().toIntOrNull()?.let {  memberApplicationPost.estimatedTurnover = it }
            }
            binding.tilPerkiraanBiayaProduksiHppModalYangTelahDikeluarkan.editText -> {
                editText?.text.toString().toIntOrNull()?.let {  memberApplicationPost.estimatedProductionCost = it }
            }
            binding.tilPerkiraanPendapatanBersih.editText -> {
                editText?.text.toString().toIntOrNull()?.let {  memberApplicationPost.estimatedNetIncome = it }
            }
            binding.tilSiklusUsahaProduksi.editText -> {
                editText?.text.toString().toIntOrNull()?.let {  memberApplicationPost.businessCycle = it }
            }
            binding.tilSiklusUsahaProduksiUnit.editText -> {
                memberApplicationPost.businessCycleUnit = editText?.text.toString()
            }
        }
    }

    private val launcherIntentFileChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val mimeType = uri.getMimeType(requireContext())
                if (mimeType != null) {
                    when (requestCode) {
                        Constants.REQUEST_CODE_LAPORAN_LABA_RUGI -> {
                            labaRugiFile = uriToFile(uri, requireContext())
                            binding.cvLaporanLabaRugi.showPreviewImage(uri, requireContext())
                            labaRugiFile?.let {
                                viewModel.uploadFile(it, Constants.REQUEST_CODE_LAPORAN_LABA_RUGI)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showMitraList() {
        //? Add To body Request
        val partnerList = mutableListOf<String>()
        viewModel.mitraList.map {
            partnerList.add(it.nama)
        }
        memberApplicationPost.businessPartner = partnerList
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var position = 0
        executor.execute {
            val items = viewModel.mitraList.map { mitra ->
                MitraItem.toViewItem(mitra.nama, position++,
                    { newMitra, position ->
                        onUpdate(newMitra, position)
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

    private fun onUpdate(mitra: String, position: Int) {
        viewModel.updateMitra(mitra, position)
    }

    private fun onDelete(position: Int) {
        viewModel.removeMitra(position)
        showMitraList()
    }

    private fun choseDocument(request: Int) {
        requestCode = request
        startFileChooser(launcherIntentFileChooser)
    }

    private fun observeBusinessPartner(state: ResultState<List<MitraEntity>>) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Success<List<MitraEntity>> -> {
                state.data?.let {
                    it.map { it1 ->
                        it1.name?.let { it2 -> viewModel.insertMitra(it2) }
                    }
                }
                showMitraList()
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    private fun observeLabaRugiUpload(state: ResultState<String>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.UnknownError,
            is ResultState.Success -> {
                memberApplicationPost.profitLossFile = state.data
            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.orEmpty())
            else -> {
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}
