package id.bpdlh.fdb.features.fdk

import android.R
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
import id.bpdlh.fdb.core.common.utils.getNikError
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.common.widget.DatePickerSpinner
import id.bpdlh.fdb.core.domain.entities.AfiliasiPendampingEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.databinding.FragmentInformasiKepengurusanKelompokBinding
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

/**
 * Created by hahn on 07/09/23.
 * Project: BPDLH App
 */
class InformasiKepengurusanKelompokFragment: BaseDaggerFragment(), TextWatcherTextChange, HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPendaftaranKelompokViewModel> { factory }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var binding: FragmentInformasiKepengurusanKelompokBinding
    private var communicator: IFormulirPendaftaranKelompokCommunicator? = null
    private lateinit var groupProfileEntity: GroupProfileEntity
    private lateinit var progressDialog: CustomProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformasiKepengurusanKelompokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        formWatcher()
        progressDialog = CustomProgressDialog(requireContext())
        viewModel.fetchAfiliasiPendamping()
        observe(viewModel.saveDraftResult, ::onSaveDraftResult)
        observe(viewModel.getAfiliasiPendamping(), ::onAfiliasiPendampingFetched)
    }

    override fun onResume() {
        super.onResume()
        observe(viewModel.getGroupProfileEntity()) {
            groupProfileEntity = it
            showData(it)
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

    private fun showData(data: GroupProfileEntity) {
        with(binding) {
            val date = data.administrationDeadlineDate?.convertStringDate(
                input = Constants.ISO_DATE_FORMAT,
                output = Constants.NEW_DATE_FORMAT
            )
            tieNamaKetuaKps.setText(data.leaderName)
            tieNikKetuaKps.setText(data.leaderKtp)
            tieNoTelpKetuaKps.setText(data.leaderPhoneNumber)
            tieNamaSekretarisKps.setText(data.secretaryName)
            tieNoTelpSekretarisKps.setText(data.secretaryPhoneNumber)
            tieNamaBendaharaKps.setText(data.treasurerName)
            tieNoTelpBendaharaKps.setText(data.treasurerPhoneNumber)
            tieNamaPendampingKps.setText(data.companionName)
            tieNoTelpPendampingKps.setText(data.companionPhoneNumber)
            tieAfiliasiPendampingKps.setText(data.companionAffiliate)
            tieBatasAkhirKepengurusan.setText(date)
            tieJumlahAnggotaKps.setText(data.amountOfMember.toString())
            tieJumlahAnggotaKpsPermohonan.setText(data.amountOfMemberSubmit.toString())
            tieJumlahAndilGarapan.setText(data.amountOfMemberSubmitLand.toString())
            tieTotalLuasLahan.setText(data.amountOfMemberSubmitLandArea.toString())
        }
    }

    private fun initUI() {
        with(binding) {
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }

            incFooter.btnKembali.setOnClickListener {
                viewModel.setGroupProfile(saveData())
                communicator?.goToPage(0)
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                showDraftBottomSheet()
            }
        }
    }

    private fun showDraftBottomSheet() {
        val generalConfirmationBottomSheet = GeneralConfirmationBottomSheet(ConfirmationType.DRAFT) {
            onSaveDraftClicked()
        }
        generalConfirmationBottomSheet.show(childFragmentManager, GeneralConfirmationBottomSheet.TAG)
    }

    private fun saveData(): GroupProfileEntity {
        with(binding) {
            val deadline = tilBatasAkhirKepengurusan.editText?.text.toString()
                .convertStringDate(output = Constants.ISO_DATE_FORMAT)
            return groupProfileEntity.copy(
                leaderName = tilNamaKetuaKps.editText?.text.toString(),
                leaderKtp = tilNikKetuaKps.editText?.text.toString(),
                leaderPhoneNumber = tilNoTelpKetuaKps.editText?.text.toString(),
                secretaryName = tilNamaSekretarisKps.editText?.text.toString(),
                secretaryPhoneNumber = tilNoTelpSekretarisKps.editText?.text.toString(),
                treasurerName = tilNamaBendaharaKps.editText?.text.toString(),
                treasurerPhoneNumber = tilNoTelpBendaharaKps.editText?.text.toString(),
                companionName = tilNamaPendampingKps.editText?.text.toString(),
                companionPhoneNumber = tilNoTelpPendampingKps.editText?.text.toString(),
                companionAffiliate = tilAfiliasiPendampingKps.editText?.text.toString(),
                administrationDeadlineDate = deadline,
                amountOfMember = getAmountInt(tilJumlahAnggotaKps.editText?.text.toString()),
                amountOfMemberSubmit = getAmountInt(tilJumlahAnggotaKpsPermohonan.editText?.text.toString()),
                amountOfMemberSubmitLand = getAmountInt(tilJumlahAndilGarapan.editText?.text.toString()),
                amountOfMemberSubmitLandArea = getAmountInt(tilTotalLuasLahan.editText?.text.toString()),
            )
        }
    }

    private fun getAmountInt(input: String) : Int {
        if (input.isEmpty()) return 0
        return input.toInt()
    }

    private fun onSaveDraftClicked() {
        viewModel.saveDraft(saveData())
    }

    private fun onAfiliasiPendampingFetched(state: ResultState<List<AfiliasiPendampingEntity>>) {
        when(state) {
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.Success -> {
                progressDialog.dismiss()
                val afiliasiPendampingList = state.data?.map { it.name }.orEmpty()
                val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, afiliasiPendampingList)
                binding.tieAfiliasiPendampingKps.setAdapter(adapter)
            }

            is ResultState.UnprocessableContent -> {
                progressDialog.dismiss()
                context?.showToast(state.message.toString())
            }
            else -> progressDialog.dismiss()
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilNamaKetuaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNoTelpKetuaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNikKetuaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNamaSekretarisKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNamaBendaharaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNoTelpBendaharaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNamaPendampingKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilNoTelpPendampingKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilAfiliasiPendampingKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilBatasAkhirKepengurusan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilJumlahAnggotaKps.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilJumlahAnggotaKpsPermohonan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilJumlahAndilGarapan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilTotalLuasLahan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@InformasiKepengurusanKelompokFragment)
            }
            tilBatasAkhirKepengurusan.setEndIconOnClickListener {
                val datePickerSpinner = DatePickerSpinner(tilBatasAkhirKepengurusan.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilBatasAkhirKepengurusan.setErrorIconOnClickListener {
                tilBatasAkhirKepengurusan.error = null
                val datePickerSpinner = DatePickerSpinner(tilBatasAkhirKepengurusan.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
            tilBatasAkhirKepengurusan.editText?.setOnClickListener {
                tilBatasAkhirKepengurusan.error = null
                val datePickerSpinner = DatePickerSpinner(tilBatasAkhirKepengurusan.editText as TextView)
                datePickerSpinner.show(childFragmentManager, DATE_PICKER_TAG)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val strNamaKetuaKps = tilNamaKetuaKps.editText?.text.toString().getGeneralError(requireContext())
            strNamaKetuaKps?.let { errorMessages.add(it) }
            tilNamaKetuaKps.error = strNamaKetuaKps

            val strNoTelpKetuaKps = tilNoTelpKetuaKps.editText?.text.toString().getError(requireContext())
            strNoTelpKetuaKps?.let { errorMessages.add(it) }
            tilNoTelpKetuaKps.error = strNoTelpKetuaKps

            val strNikKetuaKps = tilNikKetuaKps.editText?.text.toString().getNikError(requireContext())
            strNikKetuaKps?.let { errorMessages.add(it) }
            tilNikKetuaKps.error = strNikKetuaKps

            val strNamaSekretaris = tilNamaSekretarisKps.editText?.text.toString().getGeneralError(requireContext())
            strNamaSekretaris?.let { errorMessages.add(it) }
            tilNamaSekretarisKps.error = strNamaSekretaris

            val strNamaBendahara = tilNamaBendaharaKps.editText?.text.toString().getGeneralError(requireContext())
            strNamaBendahara?.let { errorMessages.add(it) }
            tilNamaBendaharaKps.error = strNamaBendahara

            val strNoTelpBendahara = tilNoTelpBendaharaKps.editText?.text.toString().getError(requireContext())
            strNoTelpBendahara?.let { errorMessages.add(it) }
            tilNoTelpBendaharaKps.error = strNoTelpBendahara

            val strnamaPendamping = tilNamaPendampingKps.editText?.text.toString().getGeneralError(requireContext())
            strnamaPendamping?.let { errorMessages.add(it) }
            tilNamaPendampingKps.error = strnamaPendamping

            val strNoTelpPendamping = tilNoTelpPendampingKps.editText?.text.toString().getError(requireContext())
            strNoTelpPendamping?.let { errorMessages.add(it) }
            tilNoTelpPendampingKps.error = strNoTelpPendamping

            val strAfiliasi = tilAfiliasiPendampingKps.editText?.text.toString().getError(requireContext())
            strAfiliasi?.let { errorMessages.add(it) }
            tilAfiliasiPendampingKps.error = strAfiliasi

            val strBatasAkhirPengurus = tilBatasAkhirKepengurusan.editText?.text.toString().getDateError(requireContext())
            strBatasAkhirPengurus?.let { errorMessages.add(it) }
            tilBatasAkhirKepengurusan.error = strBatasAkhirPengurus

            val strJumlahAnggota = tilJumlahAnggotaKps.editText?.text.toString().getError(requireContext())
            strJumlahAnggota?.let { errorMessages.add(it) }
            tilJumlahAnggotaKps.error = strJumlahAnggota

            val strJumlahAnggotaPemohon = tilJumlahAnggotaKpsPermohonan.editText?.text.toString().getError(requireContext())
            strJumlahAnggotaPemohon?.let { errorMessages.add(it) }
            tilJumlahAnggotaKpsPermohonan.error = strJumlahAnggotaPemohon

            val strJumlahAndilGarapan = tilJumlahAndilGarapan.editText?.text.toString().getError(requireContext())
            strJumlahAndilGarapan?.let { errorMessages.add(it) }
            tilJumlahAndilGarapan.error = strJumlahAndilGarapan

            val strTotalLahan = tilTotalLuasLahan.editText?.text.toString().getError(requireContext())
            strTotalLahan?.let { errorMessages.add(it) }
            tilTotalLuasLahan.error = strTotalLahan
        }
        if (errorMessages.size == 0) {
            viewModel.setGroupProfile(saveData())
            communicator?.goToPage(2)
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        with(binding) {
            when(editText) {
                tilNamaKetuaKps.editText -> {
                    tilNamaKetuaKps.error = editText?.text.toString().getGeneralError(requireContext())
                }
                tilNoTelpKetuaKps.editText -> {
                    tilNoTelpKetuaKps.error = editText?.text.toString().getError(requireContext())
                }
                tilNikKetuaKps.editText -> {
                    tilNikKetuaKps.error = editText?.text.toString().getNikError(requireContext())
                }
                tilNamaSekretarisKps.editText -> {
                    tilNamaSekretarisKps.error = editText?.text.toString().getGeneralError(requireContext())
                }
                tilNoTelpSekretarisKps.editText -> {
                    tilNoTelpSekretarisKps.error = editText?.text.toString().getError(requireContext())
                }
                tilNamaBendaharaKps.editText -> {
                    tilNamaBendaharaKps.error = editText?.text.toString().getError(requireContext())
                }
                tilNoTelpBendaharaKps.editText -> {
                    tilNoTelpBendaharaKps.error = editText?.text.toString().getError(requireContext())
                }
                tilNamaPendampingKps.editText -> {
                    tilNamaPendampingKps.error = editText?.text.toString().getError(requireContext())
                }
                tilNoTelpPendampingKps.editText -> {
                    tilNoTelpPendampingKps.error = editText?.text.toString().getError(requireContext())
                }
                tilAfiliasiPendampingKps.editText -> {
                    tilAfiliasiPendampingKps.error = editText?.text.toString().getError(requireContext())
                }
                tilBatasAkhirKepengurusan.editText -> {
                    tilBatasAkhirKepengurusan.error = editText?.text.toString().getDateError(requireContext())
                }
                tilJumlahAnggotaKps.editText -> {
                    tilJumlahAnggotaKps.error = editText?.text.toString().getError(requireContext())
                }
                tilJumlahAnggotaKpsPermohonan.editText -> {
                    tilJumlahAnggotaKpsPermohonan.error = editText?.text.toString().getError(requireContext())
                }
                tilJumlahAndilGarapan.editText -> {
                    tilJumlahAndilGarapan.error = editText?.text.toString().getError(requireContext())
                }
                tilTotalLuasLahan.editText -> {
                    tilTotalLuasLahan.error = editText?.text.toString().getError(requireContext())
                }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}