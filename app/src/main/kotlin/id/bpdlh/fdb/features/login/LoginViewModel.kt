package id.bpdlh.fdb.features.login

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.AuthUseCaseContract
import id.bpdlh.fdb.core.domain.entities.LoginEntity
import id.bpdlh.fdb.core.domain.entities.ProfileEntity
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCaseContract) :
    BaseViewModel() {
    val resultLogin = MutableLiveData<ResultState<LoginEntity>>()
    val resultProfile = MutableLiveData<ResultState<ProfileEntity>>()

    fun doLogin(email: String, password: String) {
        val disposable = authUseCase.login(email, password)
            .doOnSubscribe {
                resultLogin.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                resultLogin.value = ResultState.HideLoading()
                resultLogin.value = resultState
            }
        addDisposable(disposable)
    }

    fun fetchProfile() {
        val disposable = authUseCase.fetchProfile()
            .doOnSubscribe {
                resultProfile.value = ResultState.Loading()
            }
            .subscribe { resultState ->
                resultProfile.value = ResultState.HideLoading()
                resultProfile.value = resultState
            }
        addDisposable(disposable)
    }

}