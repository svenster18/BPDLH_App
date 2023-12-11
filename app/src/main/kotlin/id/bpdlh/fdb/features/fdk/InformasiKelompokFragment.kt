package id.bpdlh.fdb.features.fdk

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Constants.DATE_PICKER_TAG
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.getDateError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.gone
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.entities.FungsiKawasanEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.databinding.FragmentInformasiKelompokBinding
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

/**
 * Created by hahn on 07/09/23.
 * Project: BPDLH App
 */
class InformasiKelompokFragment : BaseDaggerFragment(), TextWatcherTextChange,
    HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPendaftaranKelompokViewModel> { factory }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var binding: FragmentInformasiKelompokBinding
    private var communicator: IFormulirPendaftaranKelompokCommunicator? = null
    private lateinit var groupProfileEntity: GroupProfileEntity
    private lateinit var progressDialog: CustomProgressDialog
    private var isLoading = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformasiKelompokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        watchForm()
        progressDialog = CustomProgressDialog(requireContext())
        isLoading = true
        viewModel.fetchFungsiKawasan()
        observe(viewModel.getGroupProfileEntity()) {
            groupProfileEntity = it
            showData(it)
            viewModel.fetchProvince(groupProfileEntity.province)
        }
        observe(viewModel.saveDraftResult, ::onSaveDraftResult)
        observe(viewModel.getFungsiKawasan(), ::onFungsiKawasanFetched)
        observe(viewModel.getProvince(), ::onProvinceListDataLoaded)
        observe(viewModel.getRegency(), ::onRegencyListDataLoaded)
        observe(viewModel.getDistrict(), ::onDistrictListDataLoaded)
        observe(viewModel.getVillage(), ::onVillageLoaded)
    }

    override fun onResume() {
        super.onResume()
        resetWatcher()
    }

    private fun resetWatcher() {
        with(binding) {
            tilNamaKups.editText?.error = null
            tilDasarPersetujuan.editText?.error = null
            tilFungsiKawasan.editText?.error = null
            tilTahunBerdiri.editText?.error = null
            llAlamat.edtProvinsi.error = null
            llAlamat.edtKotaKabupaten.error = null
            llAlamat.edtKecamatan.error = null
            llAlamat.edtKelurahan.error = null
            llAlamat.edtAlamat.error = null
        }
    }

    private fun onSaveDraftResult(state: ResultState<GroupProfileEntity>) {
        progressDialog.dismiss()
        when(state) {
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.Success -> {
                state.message?.let { context?.showToast(it) }
                activity?.onBackPressed()
            }

            is ResultState.UnprocessableContent -> {
                context?.showToast(state.message.toString())
            }
            else -> Unit
        }
    }

    private fun onFungsiKawasanFetched(state: ResultState<List<FungsiKawasanEntity>>) {
        when(state) {
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.Success -> {
                progressDialog.dismiss()
                val fungsiKawasanList = state.data?.map { it.name }.orEmpty()
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, fungsiKawasanList)
                binding.edtFungsiKawasan.setAdapter(adapter)
            }

            is ResultState.UnprocessableContent -> {
                progressDialog.dismiss()
                context?.showToast(state.message.toString())
            }
            else -> progressDialog.dismiss()
        }
    }

    private fun showData(data: GroupProfileEntity) {
        with(binding) {
            val date = data.establishmentDate?.convertStringDate(
                input = Constants.ISO_DATE_FORMAT,
                output = Constants.NEW_DATE_FORMAT
            )
            etNamaKps.setText(data.name)
            etDasarPersetujuan.setText(data.sk)
            etNamaKups.setText(data.kupsName)
            etDasarPembentukanIzinKups.setText(data.kupsSk)
            etTahunBerdiri.setText(date)
            edtFungsiKawasan.setText(data.functionality)
            llAlamat.edtProvinsi.setText(data.province)
            llAlamat.edtKotaKabupaten.setText(data.city)
            llAlamat.edtKecamatan.setText(data.district)
            llAlamat.edtKelurahan.setText(data.village)
            llAlamat.edtAlamat.setText(data.address)
        }
    }

    private fun watchForm() {
        with(binding) {
            tilNamaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokFragment)
            }
            tilDasarPersetujuan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokFragment)
            }
            tilNamaKups.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokFragment)
            }
            tilDasarPembentukanIzinKups.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokFragment)
            }
            tilTahunBerdiri.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokFragment)
            }
            tilTahunBerdiri.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilTahunBerdiri.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilTahunBerdiri.setErrorIconOnClickListener {
                tilTahunBerdiri.editText?.error = null
                val datePickerSpinner = DatePickerSpinner(tilTahunBerdiri.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilTahunBerdiri.editText?.setOnClickListener {
                tilTahunBerdiri.editText?.error = null
                val datePickerSpinner = DatePickerSpinner(tilTahunBerdiri.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilFungsiKawasan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKelompokFragment)
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
                    val selectedDistrict = edtKotaKabupaten.text.toString()
                    if (!isLoading) {
                        val villageId = viewModel.getVillageIdFromDistrict(selectedDistrict)
                        if (villageId.isNotEmpty()) viewModel.fetchVillage(villageId)
                    }
                }

                textInputLayoutProvinsi.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
                textInputLayoutKotaKabupaten.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
                textInputLayoutKecamatan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
                textInputLayoutKelurahan.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
                textInputLayoutRt.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
                textInputLayoutRw.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
                textInputLayoutAlamatLengkap.editText?.let {
                    CustomTextWatcher().registerEditText(it)
                        .setCallBackOnTextChange(this@InformasiKelompokFragment)
                }
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        with(binding) {
            when(editText) {
                tilNamaKps.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    tilNamaKps.error = error
                }
                tilDasarPersetujuan.editText -> {
                    val error = editText?.text.toString().getGeneralError(requireContext())
                    tilDasarPersetujuan.error = error
                }
                tilTahunBerdiri.editText -> {
                    val error = editText?.text.toString().getDateError(requireContext())
                    tilTahunBerdiri.error = error
                }
                tilFungsiKawasan.editText -> {
                    val error = editText?.text.toString().getError(requireContext())
                    tilFungsiKawasan.error = error
                }
                llAlamat.textInputLayoutProvinsi.editText -> {
                    val strProv = editText?.text.toString()
                    if (!isLoading) {
                        val regencyId = viewModel.getRegencyIdFromProvince(strProv)
                        if (regencyId.isNotEmpty()) viewModel.fetchCity(strProv)
                    }
                    llAlamat.textInputLayoutProvinsi.error = strProv.getError(requireContext())
                }
            }
        }
    }

    private fun initUI() {
        with(binding) {
            //hide subtitle alamat and group RT/RW
            llAlamat.groupRt.gone()
            llAlamat.groupRw.gone()

            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnKembali.setOnClickListener {
                activity?.onBackPressed()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                showDraftBottomSheet()
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val error = tilNamaKps.editText?.text.toString().getGeneralError(requireContext())
            error?.let { errorMessages.add(it) }
            tilNamaKps.error = error

            val strDasarPersetujuan = tilDasarPersetujuan.editText?.text.toString().getGeneralError(requireContext())
            strDasarPersetujuan?.let { errorMessages.add(it) }
            tilDasarPersetujuan.error = strDasarPersetujuan

            val strTahunBerdiri = tilTahunBerdiri.editText?.text.toString().getDateError(requireContext())
            strTahunBerdiri?.let { errorMessages.add(it) }
            tilTahunBerdiri.error = strTahunBerdiri

            val strFungsiKawasan = tilFungsiKawasan.editText?.text.toString().getError(requireContext())
            strFungsiKawasan?.let { errorMessages.add(it) }
            tilFungsiKawasan.error = strFungsiKawasan
        }
        if (errorMessages.size == 0) {
            viewModel.setGroupProfile(saveData())
            communicator?.goToPage(1)
        }
    }

    private fun showDraftBottomSheet() {
        val generalConfirmationBottomSheet = GeneralConfirmationBottomSheet(ConfirmationType.DRAFT) {
            onSaveDraftClicked()
        }
        generalConfirmationBottomSheet.show(childFragmentManager, GeneralConfirmationBottomSheet.TAG)
    }

    private fun onSaveDraftClicked() {
        val dataTobeSaved = saveData()
        viewModel.saveDraft(dataTobeSaved)
    }

    private fun saveData(): GroupProfileEntity {
        with(binding) {
            val establishmentDate = tilTahunBerdiri.editText?.text.toString()
                .convertStringDate(output = Constants.ISO_DATE_FORMAT)
            return groupProfileEntity.copy(
                name = tilNamaKps.editText?.text.toString(),
                sk = tilDasarPersetujuan.editText?.text.toString(),
                kupsName = tilNamaKups.editText?.text.toString(),
                kupsSk = tilDasarPembentukanIzinKups.editText?.text.toString(),
                establishmentDate = establishmentDate,
                functionality = tilFungsiKawasan.editText?.text.toString(),
                province = llAlamat.textInputLayoutProvinsi.editText?.text.toString(),
                city = llAlamat.textInputLayoutKotaKabupaten.editText?.text.toString(),
                district = llAlamat.textInputLayoutKecamatan.editText?.text.toString(),
                village = llAlamat.textInputLayoutKelurahan.editText?.text.toString(),
                address = llAlamat.textInputLayoutAlamatLengkap.editText?.text.toString(),
            )
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
                    binding.llAlamat.edtProvinsi.setAdapter(provinceAdapter)
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

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
