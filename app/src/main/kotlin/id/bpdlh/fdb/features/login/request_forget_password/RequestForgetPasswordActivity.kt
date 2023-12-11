package id.bpdlh.fdb.features.login.request_forget_password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import id.bpdlh.fdb.R
import id.bpdlh.fdb.core.base.BaseDaggerActivity
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.CustomTextWatcher
import id.bpdlh.fdb.core.common.utils.TextWatcherAfterChange
import id.bpdlh.fdb.core.common.utils.ViewModelOwner
import id.bpdlh.fdb.core.common.utils.observe
import id.bpdlh.fdb.core.common.widget.CustomProgressDialog
import id.bpdlh.fdb.databinding.ActivityRequestForgetPasswordBinding
import id.bpdlh.fdb.features.login.GeneralSuccessBottomSheet
import id.bpdlh.fdb.features.login.LoginActivity
import id.bpdlh.fdb.features.login.update_password.UpdatePasswordActivity
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import javax.inject.Inject

class RequestForgetPasswordActivity : BaseDaggerActivity(),
    ViewModelOwner<RequestForgetPasswordViewModel>,
    TextWatcherAfterChange,
    HasAndroidInjector {

    @Inject
    override lateinit var viewModel: RequestForgetPasswordViewModel

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private val binding by lazy { ActivityRequestForgetPasswordBinding.inflate(layoutInflater) }
    private var validity = MutableLiveData<Boolean>()
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe(viewModel.result, ::observeRequestForgotPassword)
        initActionBar()
        initiUI()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.incAppbar.toolbar)
        supportActionBar?.title = resources.getString(R.string.forget_password_label_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initiUI() {
        progressDialog = CustomProgressDialog(this)
        binding.apply {
            tilEmail.editText?.let {
                CustomTextWatcher().registerEditText(it)
                    .setCallBackAfterChange(this@RequestForgetPasswordActivity)
            }

            observe(validity) { result -> btnSend.isEnabled = result }

            btnSend.setOnClickListener {
                sendRequestForgotPassword()
            }
        }
    }

    private fun sendRequestForgotPassword() {
        val generalSuccessBottomSheet =
            GeneralSuccessBottomSheet(
                title = getString(id.bpdlh.fdb.core.R.string.request_password_title_success),
                description = getString(id.bpdlh.fdb.core.R.string.request_password_description_success),
                onClose = {
                    UpdatePasswordActivity.start(
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

    override fun onAfterTextChanged(editText: EditText?, editable: Editable?) {
        when (editText) {
            binding.tilEmail.editText -> {
                val strEmail = editText?.text.toString()
                binding.tilEmail.error = when {
                    strEmail.isEmpty() -> {
                        validity.value = false
                        getString(R.string.label_email_empty)

                    }

                    !Patterns.EMAIL_ADDRESS.matcher(strEmail)
                        .matches() -> {
                        validity.value = false
                        getString(R.string.label_email_wrong_format)
                    }

                    else -> {
                        validity.value = true
                        null
                    }
                }
            }

        }
    }

    private fun observeRequestForgotPassword(state: ResultState<Any>) {
        when (state) {
            is ResultState.Success<Any> -> {
                showDialogSuccess()
            }

            is ResultState.Loading -> {
                progressDialog.show()
            }

            is ResultState.NotFound -> {
//                binding.tilEmail.error = getString(R.string.label_email_not_registered)
            }

            is ResultState.HideLoading -> {
                progressDialog.dismiss()
            }

            else -> {
                state.message?.let { showDialogFailed(it) }
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
    }

    private fun showDialogFailed(error: String) {
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun injectComponent() {
        AndroidInjection.inject(this)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RequestForgetPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }
}