package id.bpdlh.fdb.features.fdk

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.base.ViewPagerAdapter
import id.bpdlh.fdb.core.common.utils.ConfirmationType
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.getDateError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.databinding.FragmentDetailKegiatanKelompokBinding
import id.bpdlh.fdb.features.fdk.step3.GambaranUmumFragment
import id.bpdlh.fdb.features.fdk.step3.KegiatanKelompokFragment
import id.bpdlh.fdb.features.fdk.step3.MitraUsahaFragment
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by hahn on 07/09/23.
 * Project: BPDLH App
 */
class DetailKegiatanKelompokFragment: BaseDaggerFragment(), TextWatcherTextChange, HasAndroidInjector {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<FormulirPendaftaranKelompokViewModel> { factory }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var binding: FragmentDetailKegiatanKelompokBinding
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
        binding = FragmentDetailKegiatanKelompokBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initTabUI()
        formWatcher()
        progressDialog = CustomProgressDialog((requireActivity()))
        viewModel.fetchKategoriKegiatan()
        viewModel.fetchKategoriUsahaDibiayai()
        observe(viewModel.getGroupProfileEntity()) {
            groupProfileEntity = it
            showData(it)
        }
        observe(viewModel.saveDraftResult, ::onSaveDraftResult)
    }

    private fun initUi(){
        with(binding) {
            incFooter.btnKembali.setOnClickListener {
                communicator?.goToPage(1)
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                showDraftBottomSheet()
            }
            incFooter.btnSelanjutnya.text = getString(R.string.btn_submit)
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
        }
    }

    private fun initTabUI() {
        val listFragment = listOf(
            KegiatanKelompokFragment(),
            MitraUsahaFragment(),
            GambaranUmumFragment()
        )
        val viewPager = binding.vpKegiatan
        val tabLayout = binding.tabLayout
        val adapter = ViewPagerAdapter(requireActivity(), listFragment)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = setTabPosition(position)
        }.attach()
    }

    private fun setTabPosition(position: Int): String {
        return when (position) {
            0 -> getString(R.string.tab_kegiatan_kelompok)
            1 -> getString(R.string.tab_mitra_usaha)
            2 -> getString(R.string.tab_gambaran_umum)
            else -> ""
        }
    }

    private fun showData(data: GroupProfileEntity) {
        with(binding) {
            val dateCreatedAt = if (data.createdAt.isNullOrEmpty()) {
                SimpleDateFormat(Constants.NEW_DATE_FORMAT, Locale.getDefault()).format(Date())
            } else {
                data.createdAt?.convertStringDate(
                    input = Constants.ISO_DATE_FORMAT,
                    output = Constants.NEW_DATE_FORMAT
                )
            }
            tieDibuatPadaTanggal.setText(dateCreatedAt)
            tieDibuatPada.setText(data.createdIn)
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilDibuatPada.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DetailKegiatanKelompokFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {

            val strTanggal = tilDibuatPadaTanggal.editText?.text.toString().getDateError(requireContext())
            strTanggal?.let { errorMessages.add(it) }
            tilDibuatPadaTanggal.error = strTanggal

            val strDibuatPada = tilDibuatPada.editText?.text.toString().getError(requireContext())
            strDibuatPada?.let { errorMessages.add(it) }
            tilDibuatPada.error = strDibuatPada

            if (!checkbox.isChecked) {
                errorMessages.add("checked")
            }
        }
        if (errorMessages.size == 0) {
            showConfirmationBottomSheet()
        }
    }

    private fun onSaveDraftClicked() {
        viewModel.saveDraft(saveData())
    }

    private fun onSubmitData() {
        viewModel.submit(saveData())
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

    private fun saveData(): GroupProfileEntity {
        with(binding) {
            val createdAt = tilDibuatPadaTanggal.editText?.text.toString()
                .convertStringDate(output = Constants.ISO_DATE_FORMAT)
            val listActivities = viewModel.getActivitiesValue()
            val listMitraUsaha = viewModel.getMitraUsahaValue()
            val listGambaranUmum = viewModel.getGambaranUmumValue()
            val listMitraUsahaString = mutableListOf<String>()
            listMitraUsaha?.forEach {
                listMitraUsahaString.add(it.category.orEmpty())
            }
            return groupProfileEntity.copy(
                createdAt = createdAt,
                createdIn = tilDibuatPada.editText?.text.toString(),
                listActivityEntity = listActivities,
                listPartnerName = listMitraUsaha,
                listGeneralDescription = listGambaranUmum
            )

        }
    }

    private fun showDraftBottomSheet() {
        val generalConfirmationBottomSheet = GeneralConfirmationBottomSheet(ConfirmationType.DRAFT) {
            onSaveDraftClicked()
        }
        generalConfirmationBottomSheet.show(childFragmentManager, GeneralConfirmationBottomSheet.TAG)
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private fun showConfirmationBottomSheet() {
        val generalConfirmationBottomSheet = GeneralConfirmationBottomSheet(ConfirmationType.SAVE) {
            onSubmitData()
        }
        generalConfirmationBottomSheet.show(childFragmentManager, GeneralConfirmationBottomSheet.TAG)
    }


    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}