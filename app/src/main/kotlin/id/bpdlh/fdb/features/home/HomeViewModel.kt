package id.bpdlh.fdb.features.home

import androidx.lifecycle.MutableLiveData
import id.bpdlh.fdb.core.base.BaseViewModel
import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.Quadruple
import id.bpdlh.fdb.core.domain.common.HomeUseCaseContract
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import io.reactivex.Single
import io.reactivex.functions.Function4
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCaseContract
): BaseViewModel() {
    private val _groupProfileResult = MutableLiveData<ResultState<GroupProfileEntity>>()
    private val _memberApplicationResult = MutableLiveData<ResultState<MemberApplicationEntity>>()
    private val _groupProfileEntity = MutableLiveData<GroupProfileEntity>()
    private val _memberApplicationEntity = MutableLiveData<MemberApplicationEntity>()
    private val _listActivities = mutableListOf<ActivityEntity>()
    private val _listGeneralDescription = mutableListOf<ActivityEntity>()
    private val _listMitraUsaha = mutableListOf<ActivityEntity>()

    fun getGroupProfileEntity() = _groupProfileEntity.value
    fun getMemberApplicationEntity() = _memberApplicationEntity.value

    fun setGroupProfileEntity(groupProfileEntity: GroupProfileEntity?) {
        groupProfileEntity?.let {
            val partners = mutableListOf<String>()
            _listMitraUsaha.forEach { mitra ->
                partners.add(mitra.category.orEmpty())
            }
            val group = it.copy(
                listActivityEntity = _listActivities,
                listGeneralDescription = _listGeneralDescription,
                listPartnerName = _listMitraUsaha
            )
            _groupProfileEntity.value = group
        }
    }
    fun getGroupProfile() = _groupProfileResult

    fun getMemberApplication() = _memberApplicationResult

    private fun fetchAllApis(userId: String) {
        val disposable = Single.zip(
            homeUseCase.fetchGroupProfile(userId),
            homeUseCase.getActivity(userId),
            homeUseCase.getGeneralDescription(userId),
            homeUseCase.getPartner(userId),
            Function4 { groupProfile: ResultState<GroupProfileEntity>,
                        activities: ResultState<List<ActivityEntity>>,
                        descriptions: ResultState<List<ActivityEntity>>,
                        partners: ResultState<List<ActivityEntity>> ->
                activities.data?.let {
                    _listActivities.clear()
                    _listActivities.addAll(it)
                }
                descriptions.data?.let {
                    _listGeneralDescription.clear()
                    _listGeneralDescription.addAll(it)
                }
                partners.data?.let {
                    _listMitraUsaha.clear()
                    _listMitraUsaha.addAll(it)
                }
                return@Function4 Quadruple(groupProfile, activities, descriptions, partners)
            }
        ).doOnSubscribe {
            _groupProfileResult.value = ResultState.Loading()
        }.subscribe { resultState ->
            _groupProfileResult.value = ResultState.HideLoading()
            resultState.first.let { _groupProfileResult.value = it }
        }
        addDisposable(disposable)
    }

    private fun fetchMemberApplication(userId: String) {
        addDisposable(
            homeUseCase.fetchMemberApplication(userId)
                .doOnSubscribe {
                    _memberApplicationResult.value = ResultState.Loading()
                }
                .subscribe { resultState ->
                    _memberApplicationResult.value = ResultState.HideLoading()
                    _memberApplicationResult.value = resultState
                }
        )
    }

    fun checkRole(role: String? = Constants.TYPE_KELOMPOK, userId: String?) {
        userId?.let {
            when (role) {
                Constants.TYPE_KELOMPOK -> fetchAllApis(it)
                Constants.TYPE_PERORANGAN -> fetchMemberApplication(it)
            }
        }
    }
}