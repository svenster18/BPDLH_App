package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import io.reactivex.Single

interface GroupApplicationUseCaseContract {
    fun getGroupApplication(userId: String) : Single<ResultState<List<GroupApplicationEntity>>>
    fun createApplication(userId: String, groupApplicationMemberPost: List<DataDebiturNonSosialEntity>): Single<ResultState<List<DataDebiturNonSosialEntity>>>
    fun submitApplication(memberApplicationId: String): Single<ResultState<Any>>
    fun deleteApplication(userId: String): Single<ResultState<Any>>
    fun getMemberApplication(memberApplicationId: String): Single<ResultState<List<DataDebiturNonSosialEntity>>>
    fun delayCutting(userId: String, body: MemberApplicationPost): Single<ResultState<Any>>
    fun nonForestyComodity(userId: String, body: MemberApplicationPost): Single<ResultState<Any>>
    fun nonWoodForestProduct(userId: String, body: MemberApplicationPost): Single<ResultState<Any>>

}