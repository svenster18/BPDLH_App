package id.bpdlh.fdb.features.registration_kelompok

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import dagger.android.support.AndroidSupportInjection
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerFragment
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherTextChange
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.getEmailError
import id.bpdlh.fdb.core.common.utils.getPasswordError
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.utils.showToast
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.core.domain.entities.RegistrasiKelompokEntity
import id.bpdlh.fdb.databinding.FragmentRegistrasiKelompokStep3Binding
import id.bpdlh.fdb.features.login.LoginActivity
import javax.inject.Inject

/**
 * Created by hahn on 04/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokStep3Fragment: BaseDaggerFragment(), TextWatcherTextChange {

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel by activityViewModels<RegistrasiKelompokViewModel> { factory }
    private lateinit var binding: FragmentRegistrasiKelompokStep3Binding
    private var communicator: IRegistrasiCommunicator? = null
    private var mRegistrasiKelompokPost: RegistrasiKelompokPost = RegistrasiKelompokPost()
    private lateinit var progressDialog: CustomProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as? IRegistrasiCommunicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrasiKelompokStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        progressDialog = CustomProgressDialog(requireContext())
        observe(viewModel.getRegistrasiKelompok(), ::loadRegistrasiKelompok)
        observe(viewModel.submitResult, ::onDataSubmitted)
    }

    override fun onResume() {
        super.onResume()
        formWatcher()
    }

    private fun initUI() {
        with(binding) {
            btnRegister.setOnClickListener {
                validateInput()
            }
            btnBack.setOnClickListener {
                saveDraft(
                    tilEmail.editText?.text.toString().lowercase(),
                    tilKataSandi.editText?.text.toString(),
                    tilKonfirmasiKataSandi.editText?.text.toString()
                )
                communicator?.gotoStep(2)
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        with(binding) {
            when(editText) {
                tilEmail.editText -> {
                    tilEmail.error = editText?.text.toString().getEmailError(requireContext())
                }
                tilKataSandi.editText -> {
                    tilKataSandi.error = editText?.text.toString().getPasswordError(requireContext())
                }
                tilKonfirmasiKataSandi.editText -> {
                    val passwordConfirmation = editText?.text.toString()
                    tilKonfirmasiKataSandi.error = editText?.text.toString().getPasswordError(requireContext(), passwordConfirmation)
                }
            }
        }
    }

    private fun formWatcher() {
        with(binding) {
            tilEmail.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep3Fragment)
            }
            tilKataSandi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep3Fragment)
            }
            tilKonfirmasiKataSandi.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackOnTextChange(this@RegistrasiKelompokStep3Fragment)
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
            contactPersonEmail = registrasiKelompokPost.contactPersonEmail?.lowercase(),
            email = registrasiKelompokPost.email?.lowercase(),
            password = registrasiKelompokPost.password,
            passwordConfirmation = registrasiKelompokPost.passwordConfirmation
        )
        with(binding) {
            tilEmail.editText?.setText(registrasiKelompokPost.email)
            tilKataSandi.editText?.setText(registrasiKelompokPost.password)
            tilKonfirmasiKataSandi.editText?.setText(registrasiKelompokPost.passwordConfirmation)
        }
    }

    private fun validateInput() {
        val errorMesages = mutableListOf<String>()
        with(binding) {
            val errEmail = tilEmail.editText?.text.toString().getEmailError(requireContext())
            errEmail?.let { errorMesages.add(it) }
            tilEmail.error = errEmail

            val errKataSandi = tilKataSandi.editText?.text.toString().getPasswordError(requireContext())
            errKataSandi?.let { errorMesages.add(it) }
            tilKataSandi.error = errKataSandi

            val errKonfirmasi = tilKonfirmasiKataSandi.editText?.text.toString().getPasswordError(requireContext(), tilKataSandi.editText?.text.toString())
            errKonfirmasi?.let { errorMesages.add(it) }
            tilKonfirmasiKataSandi.error = errKonfirmasi

            val errNotSame = tilKataSandi.editText?.text.toString() != tilKonfirmasiKataSandi.editText?.text.toString()
            if (errNotSame) {
                errorMesages.add("error not same")
                tilKonfirmasiKataSandi.error = getString(R.string.label_pass_not_same)
            }

            if (errorMesages.size == 0) {
                saveDraft(
                    tilEmail.editText?.text.toString().lowercase(),
                    tilKataSandi.editText?.text.toString(),
                    tilKonfirmasiKataSandi.editText?.text.toString()
                )
                viewModel.submitData()
            }
        }
    }

    private fun saveDraft(email: String, password: String, passwordConfirmation: String) {
        mRegistrasiKelompokPost.apply {
            this.email = email.lowercase()
            this.password = password
            this.passwordConfirmation = passwordConfirmation
        }
        viewModel.saveData(mRegistrasiKelompokPost)
    }

    private fun onDataSubmitted(state: ResultState<RegistrasiKelompokEntity>) {
        progressDialog.dismiss()
        when(state) {
            is ResultState.Success -> {
                //TODO: apakah harus dikasih toast di sini?
                context?.showToast("Registrasi berhasil")
                LoginActivity.start(
                    requireContext(),
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                activity?.finish()

            }
            is ResultState.Loading -> {
                progressDialog.show()
            }
            is ResultState.UnprocessableContent -> context?.showToast(state.message.toString())
            else -> Unit
        }
    }

    override fun injectComponent() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        fun newInstance() = RegistrasiKelompokStep3Fragment()
    }
}