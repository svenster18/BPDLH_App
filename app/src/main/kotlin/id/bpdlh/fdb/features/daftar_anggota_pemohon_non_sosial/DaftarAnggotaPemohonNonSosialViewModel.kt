package id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.GroupApplicationUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import javax.inject.Inject

class DaftarAnggotaPemohonNonSosialViewModel
@Inject constructor(
    private val groupApplicationUseCase: GroupApplicationUseCaseContract
): BaseViewModel() {

    val groupApplicationResult = MutableLiveData<ResultState<List<GroupApplicationEntity>>>()
    val createApplicationResult = MutableLiveData<ResultState<List<DataDebiturNonSosialEntity>>>()
    val memberApplicationResult = MutableLiveData<ResultState<List<DataDebiturNonSosialEntity>>>()
    val submitApplicationResult = MutableLiveData<ResultState<Any>>()
    val deleteApplicationResult = MutableLiveData<ResultState<Any>>()

    fun getGroupApplication(userId: String?) {
        userId?.let {
            addDisposable(
                groupApplicationUseCase.getGroupApplication(userId)
                    .doOnSubscribe {
                        groupApplicationResult.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        groupApplicationResult.value = ResultState.HideLoading()
                        groupApplicationResult.value = resultState
                    }
            )
        }
    }

    fun createApplication(userId: String?, groupApplicationResult : List<DataDebiturNonSosialEntity>) {
        userId?.let {
            addDisposable(
                groupApplicationUseCase.createApplication(userId, groupApplicationResult)
                    .doOnSubscribe {
                        createApplicationResult.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        createApplicationResult.value = ResultState.HideLoading()
                        createApplicationResult.value = resultState
                    }
            )
        }
    }

    fun getMemberApplication(userId: String?) {
        userId?.let {
            addDisposable(
                groupApplicationUseCase.getMemberApplication(userId)
                    .doOnSubscribe {
                        memberApplicationResult.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        memberApplicationResult.value = ResultState.HideLoading()
                        memberApplicationResult.value = resultState
                    }
            )
        }
    }

    fun submitApplication(userId: String?) {
        userId?.let {
            addDisposable(
                groupApplicationUseCase.submitApplication(userId)
                    .doOnSubscribe {
                        submitApplicationResult.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        submitApplicationResult.value = ResultState.HideLoading()
                        submitApplicationResult.value = resultState
                    }
            )
        }
    }

    fun deleteApplication(memberApplicationId: String?) {
        memberApplicationId?.let {
            addDisposable(
                groupApplicationUseCase.deleteApplication(memberApplicationId)
                    .doOnSubscribe {
                        deleteApplicationResult.value = ResultState.Loading()
                    }
                    .subscribe { resultState ->
                        deleteApplicationResult.value = ResultState.HideLoading()
                        deleteApplicationResult.value = resultState
                    }
            )
        }
    }
}