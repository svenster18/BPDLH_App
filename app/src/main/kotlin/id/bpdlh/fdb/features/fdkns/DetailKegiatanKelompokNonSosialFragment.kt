package id.bpdlh.fdb.features.fdkns

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.ViewPagerAdapter
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.databinding.FragmentFormulirKegiatanKelompokNonSosialBinding

class DetailKegiatanKelompokNonSosialFragment: BaseFormDaftarNonSosialFragment(), TextWatcherTextChange {

    private lateinit var binding: FragmentFormulirKegiatanKelompokNonSosialBinding
    private var communicator: IFormulirPendaftaranKelompokNonSosialCommunicator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IFormulirPendaftaranKelompokNonSosialCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormulirKegiatanKelompokNonSosialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUI() {
        with(binding){
            formulirKelompokNonSosialPost.amountOfMemberSubmit?.let {
                if( formulirKelompokNonSosialPost.amountOfMemberSubmit != 0)  {
                    tieJumlahAnggotaPermohonan.setText(formulirKelompokNonSosialPost.amountOfMemberSubmit.toString())
                }
            }
            formulirKelompokNonSosialPost.amountOfMemberSubmitLand?.let {
                if (formulirKelompokNonSosialPost.amountOfMemberSubmitLand != 0) {
                    tieJumlahAndilGarapan.setText(formulirKelompokNonSosialPost.amountOfMemberSubmitLand.toString())
                }
            }
            formulirKelompokNonSosialPost.amountOfMemberSubmitLandArea?.let {
                if (formulirKelompokNonSosialPost.amountOfMemberSubmitLandArea != 0) {
                    tieTotalLuasLahan.setText(formulirKelompokNonSosialPost.amountOfMemberSubmitLandArea.toString())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvDescription.text = getString(R.string.formulir_pendaftaran_kelompok_non_sosial_detail_kegiatan_kelompok)
            tvStep.text = requireContext().getString(R.string.formulir_pendaftaran_kelompok_non_sosial_text_stepper, "3")
            incFooter.btnKembali.setOnClickListener {
                communicator?.goToPage(1)
            }
            incFooter.btnSelanjutnya.setOnClickListener {
                validateInput()
            }
            incFooter.btnSimpanDraft.setOnClickListener {
                communicator?.onSavDraft()
            }
        }
        formWatcher()
        setTabUI()
    }


    private fun setTabUI() {
        val listFragment = listOf(
            KegiatanListFragment.newInstance(0),
            KegiatanListFragment.newInstance(1),
            KegiatanListFragment.newInstance(2),
        )
        val viewPager = binding.vpKegiatan
        val tabLayout = binding.tabKegiatan
        val adapter = ViewPagerAdapter(requireActivity(), listFragment)
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = setTabTitle(position)
        }.attach()
    }

    private fun setTabTitle(position: Int) : String {
        return when(position) {
            0 -> getString(R.string.tab_kegiatan_kelompok)
            1 -> getString(R.string.tab_mitra_usaha)
            2 -> getString(R.string.tab_gambaran_umum)
            else -> ""
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilJumlahjAnggotaPermohonan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DetailKegiatanKelompokNonSosialFragment)
            }
            tilJumlahAndilGarapan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DetailKegiatanKelompokNonSosialFragment)
            }
            tilTotalLuasLahan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@DetailKegiatanKelompokNonSosialFragment)
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {

            val jumlahAnggotaPermohonan = tilJumlahjAnggotaPermohonan.editText?.text.toString().getError(requireContext())
            jumlahAnggotaPermohonan?.let { errorMessages.add(it) }
            tilJumlahjAnggotaPermohonan.error = jumlahAnggotaPermohonan

            val jumlahAndilGarapan = tilJumlahAndilGarapan.editText?.text.toString().getError(requireContext())
            jumlahAndilGarapan?.let { errorMessages.add(it) }
            tilJumlahAndilGarapan.error = jumlahAndilGarapan

            val totalLuasLahan = tilTotalLuasLahan.editText?.text.toString().getError(requireContext())
            totalLuasLahan?.let { errorMessages.add(it) }
            tilTotalLuasLahan.error = totalLuasLahan
        }
        if (errorMessages.size == 0) {
            communicator?.goToPage(3)
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when(editText) {
            binding.tilJumlahjAnggotaPermohonan.editText -> {
                editText?.text.toString().toIntOrNull()?.let { formulirKelompokNonSosialPost.amountOfMemberSubmit = it }
            }
            binding.tilJumlahAndilGarapan.editText -> {
                editText?.text.toString().toIntOrNull()?.let { formulirKelompokNonSosialPost.amountOfMemberSubmitLand = it }
            }
            binding.tilTotalLuasLahan.editText -> {
                editText?.text.toString().toIntOrNull()?.let { formulirKelompokNonSosialPost.amountOfMemberSubmitLandArea = it }
            }
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

}