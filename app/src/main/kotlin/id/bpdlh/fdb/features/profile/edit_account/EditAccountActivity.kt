package id.bpdlh.fdb.features.profile.edit_account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.error
import id.bpdlh.fdb.core.common.utils.getAlamatError
import id.bpdlh.fdb.core.common.utils.getEmailError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.parcelable
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.utils.startImageChooser
import id.bpdlh.fdb.core.common.utils.toast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.databinding.ActivityEditAccountBinding
import id.bpdlh.fdb.features.registration.BaseRegistrationFragment
import javax.inject.Inject

class EditAccountActivity : BaseDaggerActivity(), ViewModelOwner<EditAccountViewModel>,
    TextWatcherTextChange, HasAndroidInjector {

    @Inject
    override lateinit var viewModel: EditAccountViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private val binding by lazy { ActivityEditAccountBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var data: GroupProfileEntity

    private val launcherIntentImageChooser = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                binding.ivPhoto.setImageURI(uri)
            }
        }
    }

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initActionBar()
        initUI()
        initData()
        viewModel.fetchProvince(data.province)
        observe(viewModel.getProvince(), ::onProvinceListDataLoaded)
        observe(viewModel.getRegency(), ::onRegencyListDataLoaded)
        observe(viewModel.getDistrict(), ::onDistrictListDataLoaded)
        observe(viewModel.getVillage(), ::onVillageLoaded)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.incAppbar.toolbar)
        supportActionBar?.title = resources.getString(R.string.ubah_akun_saya)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initUI() {
        progressDialog = CustomProgressDialog(this)
        observe(viewModel.getUpdateGroupDataResult(), ::onDataUpdated)
        binding.apply {
            //hide rt rw
            alamatKtp.groupRt.gone()
            alamatKtp.groupRw.gone()

            btnEdit.setOnClickListener { startImageChooser(launcherIntentImageChooser) }
            textILNamaLembaga.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@EditAccountActivity)
            }
            textILEmail.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@EditAccountActivity)
            }
            textILSk.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@EditAccountActivity)
            }
            alamatKtp.apply {
                textInputLayoutProvinsi.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                textInputLayoutKotaKabupaten.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                textInputLayoutKecamatan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                textInputLayoutKelurahan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                textInputLayoutRt.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                textInputLayoutRw.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                textInputLayoutAlamatLengkap.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@EditAccountActivity)
                }
                edtProvinsi.setOnItemClickListener { _, _, _, _ ->
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
            textILNamaNarahubung.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@EditAccountActivity)
            }
            textILJabatan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@EditAccountActivity)
            }
            textILTelepon.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@EditAccountActivity)
            }
            btnUbah.setOnClickListener {
                validateInput()
            }
        }
    }

    private fun initData() {
         intent.parcelable<GroupProfileEntity>(DATA)?.let { data = it }
        data.let {
            with(binding) {
                edtNamaLembaga.setText(it.name)
                edtEmail.setText(it.userEntity?.email)
                edtSk.setText(it.sk)
                alamatKtp.edtProvinsi.setText(it.province)
                alamatKtp.edtKotaKabupaten.setText(it.city)
                alamatKtp.edtKecamatan.setText(it.district)
                alamatKtp.edtKelurahan.setText(it.village)
                alamatKtp.edtAlamat.setText(it.address)
                edtNamaNarahubung.setText(it.contactPersonName)
                edtJabatan.setText(it.contactPersonPosition)
                edtTelepon.setText(it.contactPersonPhoneNumber)

            }
        }
    }

    private fun updateData() {
        with(binding) {
            val dataToUpdate = data.copy(
                name = textILNamaLembaga.editText?.text.toString(),
                email = textILEmail.editText?.text.toString(),
                sk = textILSk.editText?.text.toString(),
                province = alamatKtp.textInputLayoutProvinsi.editText?.text.toString(),
                city = alamatKtp.textInputLayoutKotaKabupaten.editText?.text.toString(),
                district = alamatKtp.textInputLayoutKecamatan.editText?.text.toString(),
                village = alamatKtp.textInputLayoutKelurahan.editText?.text.toString(),
                address = alamatKtp.textInputLayoutAlamatLengkap.editText?.text.toString(),
                contactPersonName = textILNamaNarahubung.editText?.text.toString(),
                contactPersonPosition = textILJabatan.editText?.text.toString(),
                contactPersonPhoneNumber = textILTelepon.editText?.text.toString()
            )
            viewModel.updateGroupData(dataToUpdate)
        }
    }

    private fun onDataUpdated(state: ResultState<GroupProfileEntity>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.hide()
            is ResultState.Success -> {
                toast(R.string.berhasil_edit)
                finish()
            }
            is ResultState.UnprocessableContent -> showToast(state.message.orEmpty())
            else -> Unit
        }
    }

    private fun onProvinceListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading ->  progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val provinceAdapter = ArrayAdapter(
                        this@EditAccountActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.alamatKtp.edtProvinsi.setAdapter(provinceAdapter)
                }
            }
            else -> Unit
        }
    }

    private fun onRegencyListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading ->  progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val regencyAdapter = ArrayAdapter(
                        this@EditAccountActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.alamatKtp.edtKotaKabupaten.setAdapter(regencyAdapter)
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
                        this@EditAccountActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.alamatKtp.edtKecamatan.setAdapter(adapter)
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
                        this@EditAccountActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    binding.alamatKtp.edtKelurahan.setAdapter(adapter)
                }
            }
            else -> Unit
        }
    }

    private fun validateInput() {
        binding.apply {
            val namaLembagaError =
                textILNamaLembaga.editText?.text.toString().getError(this@EditAccountActivity)
            binding.textILNamaLembaga.error(namaLembagaError)
            if (namaLembagaError != null) return
            val strEmail = textILEmail.editText?.text.toString()
            textILEmail.error = strEmail.getEmailError(this@EditAccountActivity)
            if (textILEmail.error != null) return
            val nomorAktaError =
                textILSk.editText?.text.toString().getError(this@EditAccountActivity)
            binding.textILSk.error(nomorAktaError)
            if (nomorAktaError != null) return
            alamatKtp.apply {
                val provinsiError =
                    textInputLayoutProvinsi.editText?.text.toString()
                        .getError(this@EditAccountActivity)
                textInputLayoutProvinsi.error(provinsiError)
                if (provinsiError != null) return
                val kotaError = textInputLayoutKotaKabupaten.editText?.text.toString()
                    .getError(this@EditAccountActivity)
                textInputLayoutKotaKabupaten.error(kotaError)
                if (kotaError != null) return
                val kecamatanError =
                    textInputLayoutKecamatan.editText?.text.toString()
                        .getError(this@EditAccountActivity)
                textInputLayoutKecamatan.error(kecamatanError)
                if (kecamatanError != null) return
                val kelurahanError =
                    textInputLayoutKelurahan.editText?.text.toString()
                        .getError(this@EditAccountActivity)
                textInputLayoutKelurahan.error(kelurahanError)
                if (kelurahanError != null) return
                val alamatError = textInputLayoutAlamatLengkap.editText?.text.toString()
                    .getAlamatError(this@EditAccountActivity)
                textInputLayoutAlamatLengkap.error(alamatError)
                if (alamatError != null) return
            }
            val namaNarahubungError =
                textILNamaNarahubung.editText?.text.toString().getError(this@EditAccountActivity)
            binding.textILNamaNarahubung.error(namaNarahubungError)
            if (namaNarahubungError != null) return
            val jabatanError =
                textILJabatan.editText?.text.toString().getError(this@EditAccountActivity)
            binding.textILJabatan.error(jabatanError)
            if (jabatanError != null) return
            val teleponError =
                textILTelepon.editText?.text.toString().getError(this@EditAccountActivity)
            binding.textILTelepon.error(teleponError)
            if (teleponError != null) return
            updateData()
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.textILNamaLembaga.editText -> {
                val strNamaLembaga = editText?.text.toString()
                binding.textILNamaLembaga.error = strNamaLembaga.getError(this@EditAccountActivity)
            }

            binding.textILEmail.editText -> {
                val strEmail = editText?.text.toString()
                binding.textILEmail.error = strEmail.getEmailError(this@EditAccountActivity)
            }

            binding.textILSk.editText -> {
                val strNomorAkta = editText?.text.toString()
                binding.textILSk.error = strNomorAkta.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutProvinsi.editText -> {
                val strProvinsi = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.provinsi = strProvinsi
                binding.alamatKtp.textInputLayoutProvinsi.error =
                    strProvinsi.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutKotaKabupaten.editText -> {
                val strKotaKabupaten = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.kota = strKotaKabupaten
                binding.alamatKtp.textInputLayoutKotaKabupaten.error =
                    strKotaKabupaten.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutKecamatan.editText -> {
                val strKecamatan = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.kecamatan = strKecamatan
                binding.alamatKtp.textInputLayoutKecamatan.error =
                    strKecamatan.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutKelurahan.editText -> {
                val strKelurahan = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.kelurahan = strKelurahan
                binding.alamatKtp.textInputLayoutKelurahan.error =
                    strKelurahan.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutRt.editText -> {
                val strRt = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.rt = strRt
                binding.alamatKtp.textInputLayoutRt.error = strRt.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutRw.editText -> {
                val strRw = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.rw = strRw
                binding.alamatKtp.textInputLayoutRw.error = strRw.getError(this@EditAccountActivity)
            }

            binding.alamatKtp.textInputLayoutAlamatLengkap.editText -> {
                val strAlamat = editText?.text.toString()
                BaseRegistrationFragment.registrasiPerorangan.alamat = strAlamat
                binding.alamatKtp.textInputLayoutAlamatLengkap.error =
                    strAlamat.getAlamatError(this@EditAccountActivity)
            }

            binding.textILNamaNarahubung.editText -> {
                val strNamaNarahubung = editText?.text.toString()
                binding.textILNamaNarahubung.error =
                    strNamaNarahubung.getError(this@EditAccountActivity)
            }

            binding.textILJabatan.editText -> {
                val strJabatan = editText?.text.toString()
                binding.textILJabatan.error = strJabatan.getError(this@EditAccountActivity)
            }

            binding.textILTelepon.editText -> {
                val strTelepon = editText?.text.toString()
                binding.textILTelepon.error = strTelepon.getError(this@EditAccountActivity)
            }
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        private const val DATA = "data"
        fun start(context: Context, data: GroupProfileEntity) {
            val intent = Intent(context, EditAccountActivity::class.java)
            intent.putExtra(DATA, data)
            context.startActivity(intent)
        }
    }
}