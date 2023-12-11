package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDetailEntity
import io.reactivex.Single

interface GroupApplicationRepositoryContract {
    fun getGroupApplication(userId: String): Single<BaseDataSourceApi<List<GroupApplicationEntity>>>
    fun createApplication(userId: String, groupApplicationMemberPost: List<DataDebiturNonSosialEntity>): Single<BaseDataSourceApi<List<MemberApplicationDetailEntity>>>
    fun submitApplication(memberApplicationId: String): Single<BaseDataSourceApi<Any>>
    fun deleteApplication(userId: String): Single<ResultState<Any>>
    fun getMemberApplication(memberApplicationId: String): Single<BaseDataSourceApi<List<MemberApplicationDetailEntity>>>
    fun delayCutting(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<Any>>
    fun nonForestyComodity(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<Any>>
    fun nonWoodForestProduct(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<Any>>
}