package id.bpdlh.fdb.features.login.update_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.airbnb.deeplinkdispatch.DeepLink
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.BuildConfig
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.*
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.core.domain.entities.VerifyForgotPasswordEntity
import id.bpdlh.fdb.databinding.ActivityUpdatePasswordBinding
import id.bpdlh.fdb.features.login.GeneralSuccessBottomSheet
import id.bpdlh.fdb.features.login.LoginActivity
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

@DeepLink(
    "https://${BuildConfig.HOST_SCHEME_URL}/set-password"
)
class UpdatePasswordActivity : BaseDaggerActivity(), ViewModelOwner<UpdatePasswordViewModel>,
    TextWatcherAfterChange,
    HasAndroidInjector {

    @Inject
    override lateinit var viewModel: UpdatePasswordViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private val binding by lazy { ActivityUpdatePasswordBinding.inflate(layoutInflater) }
    private lateinit var progressDialog: CustomProgressDialog
    private var validity = MutableLiveData<Boolean>()
    private var token = ""
    private var isReset = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initActionBar()
        initUI()
        observe(viewModel.result, ::observeVerifyForgotPassword)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.incAppbar.toolbar)
        supportActionBar?.title = resources.getString(R.string.update_password_label_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initUI() {
        progressDialog = CustomProgressDialog(this)
        binding.apply {
            CustomTextWatcher().registerEditText(edtNewPassword)
                .setCallBackAfterChange(this@UpdatePasswordActivity)
            CustomTextWatcher().registerEditText(edtConfirmNewPassword)
                .setCallBackAfterChange(this@UpdatePasswordActivity)
            intent.data?.let { uri ->
                uri.getQueryParameter("token")?.let {
                    token = it
                }
                uri.getQueryParameter("reset")?.let {
                    isReset = it.toBoolean()
                }
            }

            observe(validity) { result -> btnSend.isEnabled = result }

            btnSend.setOnClickListener {
//                updatePassword()
                showDialogSuccess()
            }
        }
    }

    private fun observeVerifyForgotPassword(state: ResultState<VerifyForgotPasswordEntity>) {
        when (state) {
            is ResultState.Success<VerifyForgotPasswordEntity> -> {
                showDialogSuccess()
            }

            is ResultState.Loading -> {
                progressDialog.show()
            }

            is ResultState.HideLoading -> {
                progressDialog.dismiss()
            }

            else -> {
                state.message?.let { showDialogFailed() }
            }
        }
    }

    private fun updatePassword() {
        binding.apply {
            val newPass = edtNewPassword.text.toString()
            val confirmPass = edtConfirmNewPassword.text.toString()
            if (isReset) {
                viewModel.verifyForgetPassword(
                    paramToken = token,
                    paramNewPassword = AES.encryptAES256(newPass),
                    paramRetypeNewPassword = AES.encryptAES256(confirmPass)
                )
            }
        }
    }

    override fun onAfterTextChanged(editText: EditText?, editable: Editable?) {
        binding.apply {
            when (editText) {
                tilInpNewPassword.editText -> {
                    val strPassword = editText?.text.toString()
                    val strConfirmPassword = tilInpConfirmNewPassword.editText?.text.toString()
                    tilInpNewPassword.error = errorPasswordValidation(
                        strPassword,
                        getString(R.string.update_password_hint_input_password),
                        strConfirmPassword
                    )
                }

                tilInpConfirmNewPassword.editText -> {
                    val strPassword = editText?.text.toString()
                    val strNewPassword = tilInpNewPassword.editText?.text.toString()
                    tilInpConfirmNewPassword.error = errorPasswordValidation(
                        strPassword,
                        getString(R.string.update_password_hint_input_password),
                        strNewPassword
                    )
                }
            }
        }
    }

    private fun errorPasswordValidation(
        input: String,
        emptyMessage: String,
        compareString: String
    ): CharSequence? {
        return when {
            input.isEmpty() -> {
                validity.value = false
                emptyMessage
            }

            input.length < 6 -> {
                validity.value = false
                getString(R.string.label_pass_less_then_six)
            }

            compareString.isNotEmpty() && compareString != input -> {
                validity.value = false
                getString(R.string.label_pass_not_same)
            }

            else -> {
                validity.value = true
                null
            }
        }
    }

    private fun backToLogin() {
        LoginActivity.start(
            this,
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        finish()
    }

    private fun showDialogSuccess() {
        val generalSuccessBottomSheet =
            GeneralSuccessBottomSheet(
                title = getString(id.bpdlh.fdb.core.R.string.update_password_title_success),
                onClose = {
                    LoginActivity.start(
                        this,
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                }
            )
        generalSuccessBottomSheet.show(
            supportFragmentManager,
            GeneralConfirmationBottomSheet.TAG
        )
    }

    private fun showDialogFailed() {
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context, intentFlag: Int) {
            val intent = Intent(context, UpdatePasswordActivity::class.java).apply {
                flags = intentFlag
            }
            context.startActivity(intent)
        }
    }
}