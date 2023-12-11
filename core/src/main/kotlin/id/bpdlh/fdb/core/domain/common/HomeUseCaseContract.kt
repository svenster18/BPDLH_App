package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import io.reactivex.Single

/**
 * Created by hahn on 28/09/23.
 * Project: BPDLH App
 */
interface HomeUseCaseContract {

    fun fetchHomeType(): ResultState<String>

    fun fetchGroupProfile(userId: String): Single<ResultState<GroupProfileEntity>>

    fun fetchMemberApplication(userId: String): Single<ResultState<MemberApplicationEntity>>

    fun getActivity(userId: String): Single<ResultState<List<ActivityEntity>>>

    fun getGeneralDescription(userId: String): Single<ResultState<List<ActivityEntity>>>

    //api partner responsenya belum muncul. asumsikan isinya sama dengan 2 api di atas
    fun getPartner(userId: String): Single<ResultState<List<ActivityEntity>>>
}