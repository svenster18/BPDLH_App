package id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.common.GroupApplicationUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import javax.inject.Inject

class PengajuanDaftarPermohonanNonSosialViewModel
@Inject constructor(
    private val groupApplicationUseCase: GroupApplicationUseCaseContract
): BaseViewModel() {

    val groupApplicationResult = MutableLiveData<ResultState<List<GroupApplicationEntity>>>()
    private val groupApplicationEntity = MutableLiveData<List<GroupApplicationEntity>>()
    val createApplicationResult = MutableLiveData<ResultState<List<DataDebiturNonSosialEntity>>>()
    val memberApplicationResult = MutableLiveData<ResultState<List<DataDebiturNonSosialEntity>>>()
    fun getGroupApplicationEntity() = groupApplicationEntity

    fun setGroupApplicationEntity(data: List<GroupApplicationEntity>?) {
        data?.let {
            groupApplicationEntity.value = it
        }
    }
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
}