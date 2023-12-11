package id.bpdlh.fdb.features.registration_kelompok

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getEmailError
import id.bpdlh.fdb.core.common.utils.getError
import id.bpdlh.fdb.core.common.utils.getGeneralError
import id.bpdlh.fdb.core.common.utils.getNikError
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.databinding.FragmentRegistrasiKelompokStep2Binding
import java.util.Locale
import javax.inject.Inject

/**
 * Created by hahn on 04/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokStep2Fragment: BaseDaggerFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<RegistrasiKelompokViewModel> { factory }
    private lateinit var binding: FragmentRegistrasiKelompokStep2Binding
    private var communicator: IRegistrasiCommunicator? = null
    private var mRegistrasiKelompokPost: RegistrasiKelompokPost = RegistrasiKelompokPost()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IRegistrasiCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrasiKelompokStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observe(viewModel.getRegistrasiKelompok(), ::loadRegistrasiKelompok)
    }

    override fun onResume() {
        super.onResume()
        formWatcher()
    }

    private fun initUI() {
        with(binding) {
            btnNext.setOnClickListener {
                validateInput()
            }
            btnBack.setOnClickListener {
                saveDraft(
                    namaNarahubung = tilNamaNarahubung.editText?.text.toString(),
                    nikNarahubung = tilNikNarahubung.editText?.text.toString(),
                    email = tilEmail.editText?.text.toString().lowercase(Locale.getDefault()),
                    jabatan = tilJabatan.editText?.text.toString(),
                    telepon = tilTelepon.editText?.text.toString()
                )
                communicator?.gotoStep(1)
            }
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilNamaNarahubung.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep2Fragment)
            }
            tilNikNarahubung.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep2Fragment)
            }
            tilEmail.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep2Fragment)
            }
            tilJabatan.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep2Fragment)
            }
            tilTelepon.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep2Fragment)
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        with(binding) {
            when(editText) {
                tilNamaNarahubung.editText -> {
                    tilNamaNarahubung.error = editText?.text.toString().getGeneralError(requireContext())
                }
                tilNikNarahubung.editText -> {
                    tilNikNarahubung.error = editText?.text.toString().getNikError(requireContext())
                }
                tilEmail.editText -> {
                    tilEmail.error = editText?.text.toString().getEmailError(requireContext())
                }
                tilJabatan.editText -> {
                    tilJabatan.error = editText?.text.toString().getGeneralError(requireContext())
                }
                tilTelepon.editText -> {
                    tilTelepon.error = editText?.text.toString().getError(requireContext())
                }
            }
        }
    }

    private fun validateInput() {
        val errorMessages = mutableListOf<String>()
        with(binding) {
            val errNama = tilNamaNarahubung.editText?.text.toString().getGeneralError(requireContext())
            errNama?.let { errorMessages.add(it) }
            tilNamaNarahubung.error = errNama

            val errNik = tilNikNarahubung.editText?.text.toString().getNikError(requireContext())
            errNik?.let { errorMessages.add(it) }
            tilNikNarahubung.error = errNik

            val errEmail = tilEmail.editText?.text.toString().getEmailError(requireContext())
            errEmail?.let { errorMessages.add(it) }
            tilEmail.error = errEmail

            val errJabatan = tilJabatan.editText?.text.toString().getGeneralError(requireContext())
            errJabatan?.let { errorMessages.add(it) }
            tilJabatan.error = errJabatan

            val errTelepon = tilTelepon.editText?.text.toString().getError(requireContext())
            errTelepon?.let { errorMessages.add(it) }
            tilTelepon.error = errTelepon

            if (errorMessages.size == 0) {
                saveDraft(
                    namaNarahubung = tilNamaNarahubung.editText?.text.toString(),
                    nikNarahubung = tilNikNarahubung.editText?.text.toString(),
                    email = tilEmail.editText?.text.toString().lowercase(),
                    jabatan = tilJabatan.editText?.text.toString(),
                    telepon = tilTelepon.editText?.text.toString()
                )
                communicator?.gotoStep(3)
            }
        }
    }

    private fun saveDraft(namaNarahubung: String?, nikNarahubung: String?, email: String?, jabatan: String?, telepon: String?) {
        mRegistrasiKelompokPost.apply {
            this.contactPersonName = namaNarahubung
            this.contactPersonKtp = nikNarahubung
            this.contactPersonPosition = jabatan
            this.contactPersonPhoneNumber = telepon
            this.contactPersonEmail = email
        }
        viewModel.saveData(mRegistrasiKelompokPost)
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
            email = registrasiKelompokPost.email,
            contactPersonEmail = registrasiKelompokPost.contactPersonEmail,
            password = registrasiKelompokPost.password,
            passwordConfirmation = registrasiKelompokPost.passwordConfirmation
        )
        with(binding) {
            tilNamaNarahubung.editText?.setText(registrasiKelompokPost.contactPersonName)
            tilNikNarahubung.editText?.setText(registrasiKelompokPost.contactPersonKtp)
            tilEmail.editText?.setText(registrasiKelompokPost.contactPersonEmail)
            tilJabatan.editText?.setText(registrasiKelompokPost.contactPersonPosition)
            tilTelepon.editText?.setText(registrasiKelompokPost.contactPersonPhoneNumber)
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        fun newInstance() = RegistrasiKelompokStep2Fragment()
    }
}