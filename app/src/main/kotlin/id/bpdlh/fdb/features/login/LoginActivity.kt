package id.bpdlh.fdb.features.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.airbnb.deeplinkdispatch.DeepLink
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.*
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.data.sources.local.LocalPreferences
import id.bpdlh.fdb.core.domain.entities.LoginEntity
import id.bpdlh.fdb.core.domain.entities.ProfileEntity
import id.bpdlh.fdb.databinding.ActivityLoginBinding
import id.bpdlh.fdb.features.home.HomeActivity
import id.bpdlh.fdb.features.login.request_forget_password.RequestForgetPasswordActivity
import id.bpdlh.fdb.features.registration_kelompok.RegistrasiKelompokActivity
import javax.inject.Inject

@DeepLink(AppLink.Auth.LOGIN)
class LoginActivity : BaseDaggerActivity(), ViewModelOwner<LoginViewModel>, TextWatcherTextChange,
    HasAndroidInjector {

    @Inject
    override lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var preferences: LocalPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe(viewModel.resultLogin, ::observerLogin)
        observe(viewModel.resultProfile, ::observerProfile)
        progressDialog = CustomProgressDialog(this)
        preferences = LocalPreferences(this)
        binding.apply {
            tilEmail.editText?.let {
                CustomTextWatcher().registerEditText(it).setCallBackOnTextChange(this@LoginActivity)
            }
            tilPassword.editText?.let {
                CustomTextWatcher().registerEditText(it).setCallBackOnTextChange(this@LoginActivity)
            }
            btnLogin.setOnClickListener {
                validateInput()
            }
            txtForgetPassword.setOnClickListener {
                goToForgetPassword()
            }
            btnRegister.setOnClickListener {
                goToRegister()
            }
            edtPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    validateInput()
                }
                false
            }
        }
    }

    private fun validateInput() {
        binding.apply {
            val strEmail = tilEmail.editText?.text.toString()
            tilEmail.error = when {
                strEmail.isEmpty() -> getString(R.string.label_email_empty)
                !Patterns.EMAIL_ADDRESS.matcher(strEmail)
                    .matches() -> getString(R.string.label_email_wrong_format)

                else -> null
            }

            val strPassword = tilPassword.editText?.text.toString()
            tilPassword.error = when {
                strPassword.isEmpty() -> getString(R.string.label_pass_empty)
                strPassword.length < 8 -> getString(R.string.label_pass_less_then_eight)
                else -> null
            }

            if (tilEmail.error == null && tilPassword.error == null) {
                viewModel.doLogin(
                    email = strEmail,
                    password = strPassword
                )
            }
        }
    }

    override fun onTextChanged(editText: EditText?, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (editText) {
            binding.tilEmail.editText -> {
                val strEmail = editText?.text.toString()
                binding.tilEmail.error = when {
                    strEmail.isEmpty() -> getString(R.string.label_email_empty)
                    !Patterns.EMAIL_ADDRESS.matcher(strEmail)
                        .matches() -> getString(R.string.label_email_wrong_format)

                    else -> null
                }
            }

            binding.tilPassword.editText -> {
                val strPassword = editText?.text.toString()
                binding.tilPassword.error = when {
                    strPassword.isEmpty() -> getString(R.string.label_pass_empty)
                    strPassword.length < 6 -> getString(R.string.label_pass_less_then_six)
                    else -> null
                }
            }
        }
    }

    private fun observerLogin(state: ResultState<LoginEntity>) {
        when (state) {
            is ResultState.Success<LoginEntity> -> {
                state.data?.let {
                    //TODO: api login belum kasih access token. sementara dikomen dulu
//                    preferences.save(Constants.BEARER_TOKEN, it.accessToken)
                    preferences.save(Constants.LOGGED_IN, true)
                    preferences.save(Constants.EMAIL_ID, it.email.lowercase())
                    preferences.save(Constants.USER_ID, it.userID)
                    preferences.save(Constants.ROLE, it.role)
                }
                goToHome()
//                viewModel.fetchProfile()
            }

            is ResultState.Loading -> {
                progressDialog.show()
            }

            is ResultState.BadRequest -> {
                binding.tilPassword.error = getString(R.string.label_login_failed)
            }

            is ResultState.Forbidden -> {
                binding.tilPassword.error = getString(R.string.label_access_not_allowed)
            }

            is ResultState.Unauthorized -> {
                binding.tilPassword.error = getString(R.string.label_access_not_allowed)
            }

            is ResultState.NotFound -> {
                binding.tilEmail.error = getString(R.string.label_email_not_registered)
            }

            is ResultState.HideLoading -> {
                progressDialog.dismiss()
            }

            else -> {
                //TODO: sebaiknya kesalahan detail login tidak perlu dikasih tahu ke user
                showToast(state.message.toString())
            }
        }
    }

    private fun observerProfile(state: ResultState<ProfileEntity>) {
        when (state) {
            is ResultState.Success<ProfileEntity> -> {
                goToHome()
            }

            is ResultState.Loading -> {
                progressDialog.show()
            }

            is ResultState.HideLoading -> {
                progressDialog.dismiss()
            }

            else -> {
                toast(state.message.toString())
            }
        }
    }

    private fun goToForgetPassword() {
        RequestForgetPasswordActivity.start(this)
    }

    private fun goToRegister() {
        RegistrasiKelompokActivity.start(this)
    }

    private fun goToHome() {
        HomeActivity.start(this, Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context, intentFlag: Int) {
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = intentFlag
            }
            context.startActivity(intent)
        }
    }
}