package id.bpdlh.fdb.features.registration_kelompok

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
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getAlamatError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.databinding.FragmentRegistrasiKelompokStep1Binding
import javax.inject.Inject

/**
 * Created by hahn on 04/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokStep1Fragment: BaseDaggerFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<RegistrasiKelompokViewModel> { factory }

    private lateinit var binding: FragmentRegistrasiKelompokStep1Binding
    private var communicator: IRegistrasiCommunicator? = null
    private var mRegistrasiKelompokPost: RegistrasiKelompokPost = RegistrasiKelompokPost()
    private lateinit var progressDialog: CustomProgressDialog
    private var isLoading = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IRegistrasiCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrasiKelompokStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = CustomProgressDialog(requireContext())
        isLoading = true
        viewModel.fetchProvince()
        initUI()
        formWatcher()
        observe(viewModel.getRegistrasiKelompok(), ::loadRegistrasiKelompok)
        observe(viewModel.getProvince(), ::onProvinceListDataLoaded)
        observe(viewModel.getRegency(), ::onRegencyListDataLoaded)
        observe(viewModel.getDistrict(), ::onDistrictListDataLoaded)
        observe(viewModel.getVillage(), ::onVillageLoaded)
    }

    private fun initUI() {
        val tipeKelompok = arrayListOf(
            getString(R.string.text_perhutanan_sosial),
            getString(R.string.text_non_perhutanan_sosial)
        )
        with(binding) {
            btnNext.setOnClickListener {
                validateInput()
            }
            val tipeKelompokAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                tipeKelompok
            )
            edtJenisKelompok.setAdapter(tipeKelompokAdapter)
            llAlamat.groupRw.gone()
            llAlamat.groupRt.gone()
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilNamaKelompok.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
            }
            tilJenisKelompok.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
            }
            tilDasarPersetujuan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
            }
            with(llAlamat) {
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
                    val selectedDistrict = edtKecamatan.text.toString()
                    if (!isLoading) {
                        val villageId = viewModel.getVillageIdFromDistrict(selectedDistrict)
                        if (villageId.isNotEmpty()) viewModel.fetchVillage(villageId)
                    }
                }
                textInputLayoutProvinsi.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
                }
                textInputLayoutKotaKabupaten.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
                }
                textInputLayoutKecamatan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
                }
                textInputLayoutKelurahan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
                }
                textInputLayoutAlamatLengkap.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@RegistrasiKelompokStep1Fragment)
                }
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        with(binding) {
            when(editText) {
                tilNamaKelompok.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    tilNamaKelompok.error = error
                }
                tilJenisKelompok.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    tilJenisKelompok.error = error
                }
                tilDasarPersetujuan.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    tilDasarPersetujuan.error = error
                }
                llAlamat.textInputLayoutProvinsi.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    llAlamat.textInputLayoutProvinsi.error = error
                }
                llAlamat.textInputLayoutKotaKabupaten.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    llAlamat.textInputLayoutKotaKabupaten.error = error
                }
                llAlamat.textInputLayoutKecamatan.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    llAlamat.textInputLayoutKecamatan.error = error
                }
                llAlamat.textInputLayoutKelurahan.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    llAlamat.textInputLayoutKelurahan.error = error
                }
                llAlamat.textInputLayoutAlamatLengkap.editText -> {
                    val error = editText?.text.toString().getAlamatError(requireContext())
                    llAlamat.textInputLayoutAlamatLengkap.error = error
                }
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val error = tilNamaKelompok.editText?.text.toString().getGeneralError(requireContext())
            error?.let { errorMessages.add(it) }
            tilNamaKelompok.error = error

            val errorJenisKelompok = tilJenisKelompok.editText?.text.toString().getGeneralError(requireContext())
            errorJenisKelompok?.let { errorMessages.add(it) }
            tilJenisKelompok.error = errorJenisKelompok

            val errorDasarPersetujuan = tilDasarPersetujuan.editText?.text.toString().getGeneralError(requireContext())
            errorDasarPersetujuan?.let { errorMessages.add(it) }
            tilDasarPersetujuan.error = errorDasarPersetujuan

            with(llAlamat) {
                val errProv = textInputLayoutProvinsi.editText?.text.toString().getGeneralError(requireContext())
                errProv?.let { errorMessages.add(it) }
                textInputLayoutProvinsi.error = errProv
                val errKabKot = textInputLayoutKotaKabupaten.editText?.text.toString().getGeneralError(requireContext())
                errKabKot?.let { errorMessages.add(it) }
                textInputLayoutKotaKabupaten.error = errKabKot
                val errKec = textInputLayoutKecamatan.editText?.text.toString().getGeneralError(requireContext())
                errKec?.let { errorMessages.add(it) }
                textInputLayoutKecamatan.error = errKec
                val errKel = textInputLayoutKelurahan.editText?.text.toString().getGeneralError(requireContext())
                errKel?.let { errorMessages.add(it) }
                textInputLayoutKelurahan.error = errKel
                val errAlamat = textInputLayoutAlamatLengkap.editText?.text.toString().getAlamatError(requireContext())
                errAlamat?.let { errorMessages.add(it) }
                textInputLayoutAlamatLengkap.error = errAlamat
            }

            if (errorMessages.size == 0) {
                viewModel.saveData(RegistrasiKelompokPost(
                    name = tilNamaKelompok.editText?.text.toString(),
                    type = tilJenisKelompok.editText?.text.toString(),
                    sk = tilDasarPersetujuan.editText?.text.toString(),
                    province = llAlamat.textInputLayoutProvinsi.editText?.text.toString(),
                    city = llAlamat.textInputLayoutKotaKabupaten.editText?.text.toString(),
                    district = llAlamat.textInputLayoutKecamatan.editText?.text.toString(),
                    village = llAlamat.textInputLayoutKelurahan.editText?.text.toString(),
                    address = llAlamat.textInputLayoutAlamatLengkap.editText?.text.toString(),
                    contactPersonName = mRegistrasiKelompokPost.contactPersonName,
                    contactPersonKtp = mRegistrasiKelompokPost.contactPersonKtp,
                    contactPersonPosition = mRegistrasiKelompokPost.contactPersonPosition,
                    contactPersonPhoneNumber = mRegistrasiKelompokPost.contactPersonPhoneNumber,
                    contactPersonEmail = mRegistrasiKelompokPost.contactPersonEmail,
                    email = mRegistrasiKelompokPost.email,
                    password = mRegistrasiKelompokPost.password,
                    passwordConfirmation = mRegistrasiKelompokPost.passwordConfirmation
                ))
                communicator?.gotoStep(2)
            }
        }

    }

    private fun loadRegistrasiKelompok(registrasiKelompokPost: RegistrasiKelompokPost) {
        mRegistrasiKelompokPost = RegistrasiKelompokPost(
            name = registrasiKelompokPost.name,
            type = registrasiKelompokPost.type,
            sk = registrasiKelompokPost.sk,
            province = registrasiKelompokPost.province,
            city = registrasiKelompokPost.city,
            district = registrasiKelompokPost.district,
            village = registrasiKelompokPost.village,
            address = registrasiKelompokPost.address,
            contactPersonName = registrasiKelompokPost.contactPersonName,
            contactPersonKtp = registrasiKelompokPost.contactPersonKtp,
            contactPersonPosition = registrasiKelompokPost.contactPersonPosition,
            contactPersonPhoneNumber = registrasiKelompokPost.contactPersonPhoneNumber,
            contactPersonEmail = registrasiKelompokPost.contactPersonEmail,
            email = registrasiKelompokPost.email,
            password = registrasiKelompokPost.password,
            passwordConfirmation = registrasiKelompokPost.passwordConfirmation
        )
        with(binding) {
            tilNamaKelompok.editText?.setText(registrasiKelompokPost.name)
            tilJenisKelompok.editText?.setText(registrasiKelompokPost.type)
            tilDasarPersetujuan.editText?.setText(registrasiKelompokPost.sk)

            with(llAlamat) {
                textInputLayoutProvinsi.editText?.setText(registrasiKelompokPost.province)
                textInputLayoutKotaKabupaten.editText?.setText(registrasiKelompokPost.city)
                textInputLayoutKecamatan.editText?.setText(registrasiKelompokPost.district)
                textInputLayoutKelurahan.editText?.setText(registrasiKelompokPost.village)
                textInputLayoutAlamatLengkap.editText?.setText(registrasiKelompokPost.address)
            }
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
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    with(binding.llAlamat) {
                        edtProvinsi.setAdapter(provinceAdapter)
                        edtKotaKabupaten.setText("")
                        edtKecamatan.setText("")
                        edtKelurahan.setText("")
                        edtAlamat.setText("")

                    }
                }
            }

            else -> {
//                showErrorBottomSheet(state.message)
            }
        }
    }

    private fun onRegencyListDataLoaded(state: ResultState<List<DataAlamatEntity>>) {
        when (state) {
            is ResultState.Loading -> progressDialog.show()
            is ResultState.HideLoading -> progressDialog.dismiss()
            is ResultState.Success -> {
                isLoading = false
                state.data?.let {
                    val regencyAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                    with(binding.llAlamat) {
                        edtKotaKabupaten.setAdapter(regencyAdapter)
                        edtKotaKabupaten.setText("")
                        edtKecamatan.setText("")
                        edtKelurahan.setText("")
                        edtAlamat.setText("")
                    }
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
                    with(binding.llAlamat) {
                        edtKecamatan.setAdapter(adapter)
                        edtKecamatan.setText("")
                        edtKelurahan.setText("")
                        edtAlamat.setText("")
                    }
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
                    with(binding.llAlamat) {
                        edtKelurahan.setAdapter(adapter)
                        edtKelurahan.setText("")
                        edtAlamat.setText("")
                    }
                }
            }
            else -> Unit
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    companion object {

        fun newInstance() = RegistrasiKelompokStep1Fragment()
    }
}