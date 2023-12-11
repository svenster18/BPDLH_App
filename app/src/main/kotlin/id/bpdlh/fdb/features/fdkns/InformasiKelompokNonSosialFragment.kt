package id.bpdlh.fdb.features.fdkns

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.databinding.FragmentInformasiKelompokNonSosialBinding
import javax.inject.Inject

class InformasiKelompokNonSosialFragment : BaseFormDaftarNonSosialFragment(), TextWatcherTextChange {

    private lateinit var binding: FragmentInformasiKelompokNonSosialBinding
    private var communicator: IFormulirPendaftaranKelompokNonSosialCommunicator? = null

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormKelompokNonSosialViewModel> { factory }
    private lateinit var progressDialog: CustomProgressDialog

    private var isLoading = false
    private var currentPage = 1
    private var isRefresh = false
    private var hasMoreData = false

    private var alamatCode = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformasiKelompokNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        isLoading = true
        viewModel.fetchProvince(formulirKelompokNonSosialPost.province)
        with(binding) {
            tvDescription.text = getString(R.string.formulir_pendaftaran_kelompok_non_sosial_informasi_kelompok)
            tvStep.text = requireContext().getString(R.string.formulir_pendaftaran_kelompok_non_sosial_text_stepper, "1")
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
        }
        formWatcher()
        observe(viewModel.getProvince(), ::onProvinceListDataLoaded)
        observe(viewModel.getRegency(), ::onRegencyListDataLoaded)
        observe(viewModel.getDistrict(), ::onDistrictListDataLoaded)
        observe(viewModel.getVillage(), ::onVillageLoaded)
    }

    override fun initUI() {
        progressDialog = CustomProgressDialog(requireContext())
        with(binding){
            //hide subtitle alamat and group RT/RW
            llAlamat.groupRt.gone()
            llAlamat.groupRw.gone()
            etNamaKps.setText(formulirKelompokNonSosialPost.name)
            etSkPembentukan.setText(formulirKelompokNonSosialPost.sk)
            llAlamat.edtProvinsi.setText(formulirKelompokNonSosialPost.province)
            llAlamat.edtKotaKabupaten.setText(formulirKelompokNonSosialPost.city)
            llAlamat.edtKecamatan.setText(formulirKelompokNonSosialPost.district)
            llAlamat.edtKelurahan.setText(formulirKelompokNonSosialPost.village)
            //? Di Body Request tidak ada rt & rw
//            llAlamat.edtRw.setText(formulirKelompokNonSosialPost.rw)
//            llAlamat.edtRt.setText(formulirKelompokNonSosialPost.rt)
            llAlamat.edtAlamat.setText(formulirKelompokNonSosialPost.address)
        }
    }

    private fun formWatcher() {
        with(binding) {
            with(llAlamat) {
                edtProvinsi.setOnItemClickListener { _, _, _, _ ->
                    alamatCode = Constants.ALAMAT_CODE_DOMISILI
                    val selectedProvince = edtProvinsi.text.toString()
                    if (!isLoading) {
                        val regencyId = viewModel.getRegencyIdFromProvince(selectedProvince)
                        if (regencyId.isNotEmpty()) viewModel.fetchCity(regencyId)
                    }
                }
                edtKotaKabupaten.setOnItemClickListener { _, _, _, _ ->
                    val selectedRegency = edtKotaKabupaten.text.toString()
                    if (!isLoading) {
                        val districtId = viewModel.getDistrictIdFromRegency(selectedRegency)
                        if (districtId.isNotEmpty()) viewModel.fetchDistrict(districtId)
                    }
                }
                edtKecamatan.setOnItemClickListener { _, _, _, _ ->
                    val selectedDistrict = edtKotaKabupaten.text.toString()
                    if (!isLoading) {
                        val villageId = viewModel.getVillageIdFromDistrict(selectedDistrict)
                        if (villageId.isNotEmpty()) viewModel.fetchVillage(villageId)
                    }
                }
            }
            tilNamaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            tilSkPembentukan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutProvinsi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutKotaKabupaten.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutKecamatan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutKelurahan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutRw.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutRt.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
            llAlamat.textInputLayoutAlamatLengkap.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val namaKps = tilNamaKps.editText?.text.toString().getError(requireContext())
            namaKps?.let { errorMessages.add(it) }
            tilNamaKps.error = namaKps

            val skPembentukan = tilSkPembentukan.editText?.text.toString().getGeneralError(requireContext())
            skPembentukan?.let { errorMessages.add(it) }
            tilSkPembentukan.error = skPembentukan

            val provinsi = llAlamat.textInputLayoutProvinsi.editText?.text.toString().getGeneralError(requireContext())
            provinsi?.let { errorMessages.add(it) }
            llAlamat.textInputLayoutProvinsi.error = provinsi

            val kota = llAlamat.textInputLayoutKotaKabupaten.editText?.text.toString().getGeneralError(requireContext())
            kota?.let { errorMessages.add(it) }
            llAlamat.textInputLayoutKotaKabupaten.error = kota

            val kecamatan = llAlamat.textInputLayoutKecamatan.editText?.text.toString().getGeneralError(requireContext())
            kecamatan?.let { errorMessages.add(it) }
            llAlamat.textInputLayoutKecamatan.error = kecamatan

            val kelurahan = llAlamat.textInputLayoutKelurahan.editText?.text.toString().getError(requireContext())
            kelurahan?.let { errorMessages.add(it) }
            llAlamat.textInputLayoutKelurahan.error = kelurahan

            //? Belum di pake
//            val rw = llAlamat.textInputLayoutRw.editText?.text.toString().getError(requireContext())
//            rw?.let { errorMessages.add(it) }
//            llAlamat.textInputLayoutRw.error = rw
//
//            val rt = llAlamat.textInputLayoutRt.editText?.text.toString().getError(requireContext())
//            rt?.let { errorMessages.add(it) }
//            llAlamat.textInputLayoutRt.error = rt

            val alamatLengkap = llAlamat.textInputLayoutAlamatLengkap.editText?.text.toString().getError(requireContext())
            alamatLengkap?.let { errorMessages.add(it) }
            llAlamat.textInputLayoutAlamatLengkap.error = alamatLengkap

        }
        if (errorMessages.size == 0) {
            communicator?.goToPage(1)
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText) {
            binding.tilNamaKps.editText -> {
                formulirKelompokNonSosialPost.kpsName = editText?.text.toString()
            }
            binding.tilSkPembentukan.editText -> {
                formulirKelompokNonSosialPost.sk = editText?.text.toString()
            }
            binding.llAlamat.textInputLayoutProvinsi.editText -> {
                formulirKelompokNonSosialPost.province = editText?.text.toString()
            }
            binding.llAlamat.textInputLayoutKotaKabupaten.editText -> {
                formulirKelompokNonSosialPost.city = editText?.text.toString()
            }
            binding.llAlamat.textInputLayoutKecamatan.editText -> {
                formulirKelompokNonSosialPost.district = editText?.text.toString()
            }
            binding.llAlamat.textInputLayoutKelurahan.editText -> {
                formulirKelompokNonSosialPost.village = editText?.text.toString()
            }
            //? Di Body Request tidak ada rt & rw
//            binding.llAlamat.textInputLayoutRw.editText -> {
//                formKelompokNonSosial.rw = editText?.text.toString()
//            }
//            binding.llAlamat.textInputLayoutRt.editText -> {
//                formKelompokNonSosial.rt = editText?.text.toString()
//            }
            binding.llAlamat.textInputLayoutAlamatLengkap.editText -> {
                formulirKelompokNonSosialPost.address = editText?.text.toString()
            }
        }
    }

    private fun onProvinceListDataLoaded(state: ResultState<List<DataAlamatEntity>>?) {
        progressDialog.dismiss()
        when (state) {
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.Success -> {
                isLoading = false
                state.data?.let { items ->
                    val provinceAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        items
                    )
                    binding.llAlamat.edtProvinsi.setAdapter(provinceAdapter)
                }
            }
            else -> {
            }
        }
    }

    private fun onRegencyListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading ->  progressDialog.show()

            is ResultState.HideLoading -> progressDialog.hide()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val regencyAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.llAlamat.edtKotaKabupaten.setAdapter(regencyAdapter)
                }
            }

            else -> Unit
        }
    }

    private fun onDistrictListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.llAlamat.edtKecamatan.setAdapter(adapter)
                }
            }
            else -> Unit
        }
    }

    private fun onVillageLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.llAlamat.edtKelurahan.setAdapter(adapter)
                }
            }
            else -> Unit
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}
