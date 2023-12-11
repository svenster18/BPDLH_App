package id.bpdlh.fdb.features.registration.alamat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.disableInput
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.getAlamatError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getYearError
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.databinding.FragmentAlamatBinding
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import id.bpdlh.fdb.features.registration.MapsActivity
import javax.inject.Inject

class AlamatFragment : BaseRegistrationFragment(), ViewModelOwner<AlamatViewModel>,
    TextWatcherTextChange, HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    override lateinit var viewModel: AlamatViewModel

    @Inject
    lateinit var factory: ViewModelFactory
    private val formulirViewModel by activityViewModels<FormulirPermohonanViewModel> { factory }

    private lateinit var binding: FragmentAlamatBinding
    private lateinit var progressDialog: CustomProgressDialog
    private var isLoading = false
    private var alamatCode = 0
    private val launcherMaps = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Constants.RESULT_CODE_LOCATION_CHOSEN && result.data != null) {
            val latitude = result.data!!.getDoubleExtra(Constants.EXTRA_LATITUDE, 0.0)
            val longitude = result.data!!.getDoubleExtra(Constants.EXTRA_LONGITUDE, 0.0)
            if (latitude != 0.0 && longitude != 0.0) {
                binding.alamatDomisili.apply {
                    edtLatitude.setText(latitude.toString())
                    edtLongitude.setText(longitude.toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlamatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun isValid(): Boolean {
        binding.apply {
            alamatKtp.apply {
                val provinsiError =
                    textInputLayoutProvinsi.editText?.text.toString().getError(requireContext())
                textInputLayoutProvinsi.error(provinsiError)
                if (provinsiError != null) return false
                val kotaError = textInputLayoutKotaKabupaten.editText?.text.toString()
                    .getError(requireContext())
                textInputLayoutKotaKabupaten.error(kotaError)
                if (kotaError != null) return false
                val kecamatanError =
                    textInputLayoutKecamatan.editText?.text.toString().getError(requireContext())
                textInputLayoutKecamatan.error(kecamatanError)
                if (kecamatanError != null) return false
                val kelurahanError =
                    textInputLayoutKelurahan.editText?.text.toString().getError(requireContext())
                textInputLayoutKelurahan.error(kelurahanError)
                if (kelurahanError != null) return false
                val rtError = textInputLayoutRt.editText?.text.toString().getError(requireContext())
                textInputLayoutRt.error(rtError)
                if (rtError != null) return false
                val rwError = textInputLayoutRw.editText?.text.toString().getError(requireContext())
                textInputLayoutRw.error(rwError)
                if (rwError != null) return false
                val alamatError = textInputLayoutAlamatLengkap.editText?.text.toString()
                    .getAlamatError(requireContext())
                textInputLayoutAlamatLengkap.error(alamatError)
                if (alamatError != null) return false
            }
            alamatDomisili.apply {
                alamatKtp.apply {
                    val provinsiError =
                        textInputLayoutProvinsi.editText?.text.toString().getError(requireContext())
                    textInputLayoutProvinsi.error(provinsiError)
                    if (provinsiError != null) return false
                    val kotaError = textInputLayoutKotaKabupaten.editText?.text.toString()
                        .getError(requireContext())
                    textInputLayoutKotaKabupaten.error(kotaError)
                    if (kotaError != null) return false
                    val kecamatanError = textInputLayoutKecamatan.editText?.text.toString()
                        .getError(requireContext())
                    textInputLayoutKecamatan.error(kecamatanError)
                    if (kecamatanError != null) return false
                    val kelurahanError = textInputLayoutKelurahan.editText?.text.toString()
                        .getError(requireContext())
                    textInputLayoutKelurahan.error(kelurahanError)
                    if (kelurahanError != null) return false
                    val rtError =
                        textInputLayoutRt.editText?.text.toString().getError(requireContext())
                    textInputLayoutRt.error(rtError)
                    if (rtError != null) return false
                    val rwError =
                        textInputLayoutRw.editText?.text.toString().getError(requireContext())
                    textInputLayoutRw.error(rwError)
                    if (rwError != null) return false
                    val alamatError = textInputLayoutAlamatLengkap.editText?.text.toString()
                        .getAlamatError(requireContext())
                    textInputLayoutAlamatLengkap.error(alamatError)
                    if (alamatError != null) return false
                }
                val latitudeError =
                    textILLatitude.editText?.text.toString().getError(requireContext())
                textILLatitude.error(latitudeError)
                if (latitudeError != null) return false
                val longitudeError =
                    textILLongitude.editText?.text.toString().getError(requireContext())
                textILLongitude.error(longitudeError)
                if (longitudeError != null) return false
                val tahunDomisili =
                    textILBerdomisiliSejak.editText?.text.toString().getYearError(requireContext())
                textILBerdomisiliSejak.error(tahunDomisili)
                if (tahunDomisili != null) return false
            }
        }

        return true
    }

    override fun initUI() {
        binding.apply {
            when (category) {
                Constants.PERHUTANAN_SOSIAL -> {
                    val formPembiayaan = formulirViewModel.formulirPembiayaanPerhutananSosial
                    alamatKtp.apply {
                        edtProvinsi.setText(formPembiayaan.ktpProvince)
                        edtKotaKabupaten.setText(formPembiayaan.ktpCity)
                        edtKecamatan.setText(formPembiayaan.ktpDistrict)
                        edtKelurahan.setText(formPembiayaan.ktpVillage)
                        edtRt.setText(formPembiayaan.ktpRt)
                        edtRw.setText(formPembiayaan.ktpRw)
                        edtAlamat.setText(formPembiayaan.ktpAddress)
                    }
                    alamatDomisili.apply {
//                        cbAlamatSama.isChecked = formPembiayaan.samaDenganKtp
                        alamatKtp.apply {
                            edtProvinsi.setText(formPembiayaan.domicileProvince)
                            edtKotaKabupaten.setText(formPembiayaan.domicileCity)
                            edtKecamatan.setText(formPembiayaan.domicileDistrict)
                            edtKelurahan.setText(formPembiayaan.domicileVillage)
                            edtRt.setText(formPembiayaan.domicileRt)
                            edtRw.setText(formPembiayaan.domicileRw)
                            edtAlamat.setText(formPembiayaan.domicileAddress)
                        }
                        edtLatitude.setText(formPembiayaan.domicileLatitude)
                        edtLongitude.setText(formPembiayaan.domicileLongitude)
                        edtBerdomisiliSejak.setText(formPembiayaan.domicileSinceYear)
                    }
                }

                else -> {
                    alamatKtp.apply {
                        edtProvinsi.setText(registrasiPerorangan.provinsi)
                        edtKotaKabupaten.setText(registrasiPerorangan.kota)
                        edtKecamatan.setText(registrasiPerorangan.kecamatan)
                        edtKelurahan.setText(registrasiPerorangan.kelurahan)
                        edtRt.setText(registrasiPerorangan.rt)
                        edtRw.setText(registrasiPerorangan.rw)
                        edtAlamat.setText(registrasiPerorangan.alamat)
                    }
                    alamatDomisili.apply {
                        cbAlamatSama.isChecked = registrasiPerorangan.samaDenganKtp
                        alamatKtp.apply {
                            edtProvinsi.setText(registrasiPerorangan.provinsiDomisili)
                            edtKotaKabupaten.setText(registrasiPerorangan.kotaDomisili)
                            edtKecamatan.setText(registrasiPerorangan.kecamatanDomisili)
                            edtKelurahan.setText(registrasiPerorangan.kelurahanDomisili)
                            edtRt.setText(registrasiPerorangan.rtDomisili)
                            edtRw.setText(registrasiPerorangan.rwDomisili)
                            edtAlamat.setText(registrasiPerorangan.alamatDomisili)
                        }
                        edtLatitude.setText(registrasiPerorangan.latitude.toString())
                        edtLongitude.setText(registrasiPerorangan.longitude.toString())
                        edtBerdomisiliSejak.setText(registrasiPerorangan.tahunDomisili)
                    }
                }
            }
        }
    }

    override fun disableEditText() {
        binding.root.disableInput()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        viewModel.fetchProvince(registrasiPerorangan.provinsi)
        observe(viewModel.getProvince(), ::onProvinceListDataLoaded)
        observe(viewModel.getRegency(), ::onRegencyListDataLoaded)
        observe(viewModel.getDistrict(), ::onDistrictListDataLoaded)
        observe(viewModel.getVillage(), ::onVillageLoaded)
        binding.apply {
            alamatKtp.apply {
                edtProvinsi.setOnItemClickListener { _, _, _, _ ->
                    alamatCode = Constants.ALAMAT_CODE_KTP
                    val selectedProvince = edtProvinsi.text.toString()
                    if (!isLoading) {
                        val regencyId = viewModel.getRegencyIdFromProvince(selectedProvince)
                        if (regencyId.isNotEmpty()) viewModel.fetchCity(regencyId)
                    }
                }
                edtKotaKabupaten.setOnItemClickListener { _, _, _, _ ->
                    alamatCode = Constants.ALAMAT_CODE_KTP
                    val selectedRegency = edtKotaKabupaten.text.toString()
                    if (!isLoading) {
                        val districtId = viewModel.getDistrictIdFromRegency(selectedRegency)
                        if (districtId.isNotEmpty()) viewModel.fetchDistrict(districtId)
                    }
                }
                edtKecamatan.setOnItemClickListener { _, _, _, _ ->
                    alamatCode = Constants.ALAMAT_CODE_KTP
                    val selectedDistrict = edtKecamatan.text.toString()
                    if (!isLoading) {
                        val villageId = viewModel.getVillageIdFromDistrict(selectedDistrict)
                        if (villageId.isNotEmpty()) viewModel.fetchVillage(villageId)
                    }
                }
                textInputLayoutProvinsi.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textInputLayoutKotaKabupaten.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textInputLayoutKecamatan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textInputLayoutKelurahan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textInputLayoutRt.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textInputLayoutRw.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textInputLayoutAlamatLengkap.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
            }
            alamatDomisili.apply {
                cbAlamatSama.setOnCheckedChangeListener { _, isChecked ->
                    registrasiPerorangan.samaDenganKtp = isChecked
                    if (isChecked) fillAddress()
                }
                alamatKtp.apply {
                    edtProvinsi.setOnItemClickListener { _, _, _, _ ->
                        alamatCode = Constants.ALAMAT_CODE_DOMISILI
                        val selectedProvince = edtProvinsi.text.toString()
                        if (!isLoading) {
                            val regencyId = viewModel.getRegencyIdFromProvince(selectedProvince)
                            if (regencyId.isNotEmpty()) viewModel.fetchCity(regencyId)
                        }
                    }
                    edtKotaKabupaten.setOnItemClickListener { _, _, _, _ ->
                        alamatCode = Constants.ALAMAT_CODE_DOMISILI
                        val selectedRegency = edtKotaKabupaten.text.toString()
                        if (!isLoading) {
                            val districtId = viewModel.getDistrictIdFromRegency(selectedRegency)
                            if (districtId.isNotEmpty()) viewModel.fetchDistrict(districtId)
                        }
                    }
                    edtKecamatan.setOnItemClickListener { _, _, _, _ ->
                        alamatCode = Constants.ALAMAT_CODE_DOMISILI
                        val selectedDistrict = edtKecamatan.text.toString()
                        if (!isLoading) {
                            val villageId = viewModel.getVillageIdFromDistrict(selectedDistrict)
                            if (villageId.isNotEmpty()) viewModel.fetchVillage(villageId)
                        }
                    }
                    textInputLayoutProvinsi.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                    textInputLayoutKotaKabupaten.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                    textInputLayoutKecamatan.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                    textInputLayoutKelurahan.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                    textInputLayoutRt.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                    textInputLayoutRw.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                    textInputLayoutAlamatLengkap.editText?.let {
                        CustomTextWatcher().registerEditText(it)
                            .setCallBackOnTextChange(this@AlamatFragment)
                    }
                }
                btnPilihPeta.setOnClickListener {
                    launcherMaps.launch(Intent(requireContext(), MapsActivity::class.java))
                }
                textILLatitude.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textILLongitude.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
                textILBerdomisiliSejak.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@AlamatFragment)
                }
            }
        }
    }

    private fun onProvinceListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val provinceAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.alamatKtp.edtProvinsi.setAdapter(provinceAdapter)
                    binding.alamatDomisili.alamatKtp.edtProvinsi.setAdapter(provinceAdapter)
                }
            }

            else -> Unit
        }
    }

    private fun onRegencyListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val cityAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    if (alamatCode == Constants.ALAMAT_CODE_KTP) binding.alamatKtp.edtKotaKabupaten.setAdapter(
                        cityAdapter
                    )
                    else if (alamatCode == Constants.ALAMAT_CODE_DOMISILI) binding.alamatDomisili.alamatKtp.edtKotaKabupaten.setAdapter(
                        cityAdapter
                    )
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
                    if (alamatCode == Constants.ALAMAT_CODE_KTP) binding.alamatKtp.edtKecamatan.setAdapter(
                        adapter
                    )
                    else if (alamatCode == Constants.ALAMAT_CODE_DOMISILI) binding.alamatDomisili.alamatKtp.edtKecamatan.setAdapter(
                        adapter
                    )
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
                    if (alamatCode == Constants.ALAMAT_CODE_KTP) binding.alamatKtp.edtKelurahan.setAdapter(
                        adapter
                    )
                    else if (alamatCode == Constants.ALAMAT_CODE_DOMISILI) binding.alamatDomisili.alamatKtp.edtKelurahan.setAdapter(
                        adapter
                    )
                }
            }

            else -> Unit
        }
    }

    private fun fillAddress() {
        binding.apply {
            alamatDomisili.alamatKtp.apply {
                edtProvinsi.setText(alamatKtp.edtProvinsi.text.toString())
                edtKotaKabupaten.setText(alamatKtp.edtKotaKabupaten.text.toString())
                edtKecamatan.setText(alamatKtp.edtKecamatan.text.toString())
                edtKelurahan.setText(alamatKtp.edtKelurahan.text.toString())
                edtRt.setText(alamatKtp.edtRt.text.toString())
                edtRw.setText(alamatKtp.edtRw.text.toString())
                edtAlamat.setText(alamatKtp.edtAlamat.text.toString())
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.alamatKtp.textInputLayoutProvinsi.editText -> {
                alamatCode = Constants.ALAMAT_CODE_KTP
                val strProvinsi = editText?.text.toString()
                registrasiPerorangan.provinsi = strProvinsi
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpProvince = strProvinsi
                binding.alamatKtp.textInputLayoutProvinsi.error =
                    strProvinsi.getError(requireContext())
            }

            binding.alamatKtp.textInputLayoutKotaKabupaten.editText -> {
                val strKotaKabupaten = editText?.text.toString()
                registrasiPerorangan.kota = strKotaKabupaten
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpCity = strKotaKabupaten
                binding.alamatKtp.textInputLayoutKotaKabupaten.error =
                    strKotaKabupaten.getError(requireContext())
            }

            binding.alamatKtp.textInputLayoutKecamatan.editText -> {
                val strKecamatan = editText?.text.toString()
                registrasiPerorangan.kecamatan = strKecamatan
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpDistrict = strKecamatan
                binding.alamatKtp.textInputLayoutKecamatan.error =
                    strKecamatan.getError(requireContext())
            }

            binding.alamatKtp.textInputLayoutKelurahan.editText -> {
                val strKelurahan = editText?.text.toString()
                registrasiPerorangan.kelurahan = strKelurahan
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpVillage = strKelurahan
                binding.alamatKtp.textInputLayoutKelurahan.error =
                    strKelurahan.getError(requireContext())
            }

            binding.alamatKtp.textInputLayoutRt.editText -> {
                val strRt = editText?.text.toString()
                registrasiPerorangan.rt = strRt
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpRt = strRt
                binding.alamatKtp.textInputLayoutRt.error = strRt.getError(requireContext())
            }

            binding.alamatKtp.textInputLayoutRw.editText -> {
                val strRw = editText?.text.toString()
                registrasiPerorangan.rw = strRw
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpRw = strRw
                binding.alamatKtp.textInputLayoutRw.error = strRw.getError(requireContext())
            }

            binding.alamatKtp.textInputLayoutAlamatLengkap.editText -> {
                val strAlamat = editText?.text.toString()
                registrasiPerorangan.alamat = strAlamat
                formulirViewModel.formulirPembiayaanPerhutananSosial.ktpAddress = strAlamat
                binding.alamatKtp.textInputLayoutAlamatLengkap.error =
                    strAlamat.getAlamatError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutProvinsi.editText -> {
                alamatCode = Constants.ALAMAT_CODE_DOMISILI
                val selectedProvince = editText?.text.toString()
                registrasiPerorangan.provinsiDomisili = selectedProvince
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileProvince =
                    selectedProvince
                binding.alamatDomisili.alamatKtp.textInputLayoutProvinsi.error =
                    selectedProvince.getError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutKotaKabupaten.editText -> {
                val selectedCity = editText?.text.toString()
                registrasiPerorangan.kotaDomisili = selectedCity
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileCity = selectedCity
                binding.alamatDomisili.alamatKtp.textInputLayoutKotaKabupaten.error =
                    selectedCity.getError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutKecamatan.editText -> {
                val strKecamatan = editText?.text.toString()
                registrasiPerorangan.kecamatanDomisili = strKecamatan
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileDistrict = strKecamatan
                binding.alamatDomisili.alamatKtp.textInputLayoutKecamatan.error =
                    strKecamatan.getError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutKelurahan.editText -> {
                val strKelurahan = editText?.text.toString()
                registrasiPerorangan.kelurahanDomisili = strKelurahan
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileVillage = strKelurahan
                binding.alamatDomisili.alamatKtp.textInputLayoutKelurahan.error =
                    strKelurahan.getError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutRt.editText -> {
                val strRt = editText?.text.toString()
                registrasiPerorangan.rtDomisili = strRt
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileRt = strRt
                binding.alamatDomisili.alamatKtp.textInputLayoutRt.error =
                    strRt.getError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutRw.editText -> {
                val strRw = editText?.text.toString()
                registrasiPerorangan.rwDomisili = strRw
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileRw = strRw
                binding.alamatDomisili.alamatKtp.textInputLayoutRw.error =
                    strRw.getError(requireContext())
            }

            binding.alamatDomisili.alamatKtp.textInputLayoutAlamatLengkap.editText -> {
                val strAlamat = editText?.text.toString()
                registrasiPerorangan.alamatDomisili = strAlamat
                formulirViewModel.formulirPembiayaanPerhutananSosial.domicileAddress = strAlamat
                binding.alamatDomisili.alamatKtp.textInputLayoutAlamatLengkap.error =
                    strAlamat.getAlamatError(requireContext())
            }

            binding.alamatDomisili.textILLatitude.editText -> {
                val strLatitude = editText?.text.toString()
                if (strLatitude.isNotEmpty()) {
                    registrasiPerorangan.latitude = strLatitude.toDouble()
                    formulirViewModel.formulirPembiayaanPerhutananSosial.domicileLatitude =
                        strLatitude
                }
                binding.alamatDomisili.textILLatitude.error = strLatitude.getError(requireContext())
            }

            binding.alamatDomisili.textILLongitude.editText -> {
                val strLongitude = editText?.text.toString()
                if (strLongitude.isNotEmpty()) {
                    registrasiPerorangan.longitude =
                        strLongitude.toDouble()
                    formulirViewModel.formulirPembiayaanPerhutananSosial.domicileLongitude =
                        strLongitude
                }
                binding.alamatDomisili.textILLongitude.error =
                    strLongitude.getError(requireContext())
            }

            binding.alamatDomisili.textILBerdomisiliSejak.editText -> {
                val strBerdomisiliSejak = editText?.text.toString()
                registrasiPerorangan.tahunDomisili = strBerdomisiliSejak
                if (strBerdomisiliSejak.isNotEmpty()) formulirViewModel.formulirPembiayaanPerhutananSosial.domicileSinceYear =
                    strBerdomisiliSejak
                binding.alamatDomisili.textILBerdomisiliSejak.error =
                    strBerdomisiliSejak.getYearError(requireContext())
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }
}