package id.bpdlh.fdb.features.login.request_forget_password

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.RequestForgotPasswordPost
import id.bpdlh.fdb.core.domain.common.AuthUseCaseContract
import javax.inject.Inject

class RequestForgetPasswordViewModel @Inject constructor(private val authUseCase: AuthUseCaseContract) :
    BaseViewModel() {
    val result = MutableLiveData<ResultState<Any>>()

    fun requestForgetPassword(emailUser: String) {
        val body = RequestForgotPasswordPost(email = emailUser, service = "BPDLH")

        val disposable = authUseCase.requestForgotPassword(body)
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