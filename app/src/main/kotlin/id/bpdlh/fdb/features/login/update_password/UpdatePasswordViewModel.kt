package id.bpdlh.fdb.features.login.update_password

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.VerifyForgotPasswordPost
import id.bpdlh.fdb.core.domain.common.AuthUseCaseContract
import id.bpdlh.fdb.core.domain.entities.VerifyForgotPasswordEntity
import javax.inject.Inject

class UpdatePasswordViewModel @Inject constructor(private val authUseCase: AuthUseCaseContract) :
    BaseViewModel() {
    val result = MutableLiveData<ResultState<VerifyForgotPasswordEntity>>()

    fun verifyForgetPassword(
        paramToken: String,
        paramNewPassword: String,
        paramRetypeNewPassword: String
    ) {
        val body = VerifyForgotPasswordPost(
            token = paramToken,
            newPassword = paramNewPassword,
            retypeNewPassword = paramRetypeNewPassword
        )

        val disposable = authUseCase.verifyForgotPassword(body)
            .doOnSubscribe {
                result.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                result.value = ResultState.HideLoading()
                result.value = resultState
            }
        addDisposable(disposable)
    }
}