package id.bpdlh.fdb.features.profile

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.AuthUseCaseContract
import id.bpdlh.fdb.core.domain.entities.LogoutEntity
import id.bpdlh.fdb.core.domain.entities.ProfileEntity
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val authUseCase: AuthUseCaseContract) :
    BaseViewModel() {

    val resultLogout = MutableLiveData<ResultState<LogoutEntity>>()
    val resultProfile = MutableLiveData<ResultState<ProfileEntity>>()

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

    fun doLogout() {
        //TODO: uncomment after logout API ready
//        val disposable = authUseCase.logout()
//            .doOnSubscribe {
//                resultLogout.value = ResultState.Loading()
//            }
//            .subscribe { resultState ->
//                resultLogout.value = ResultState.HideLoading()
//                resultLogout.value = resultState
//            }
//        addDisposable(disposable)
        resultLogout.value = ResultState.Success(LogoutEntity(true))
    }
}