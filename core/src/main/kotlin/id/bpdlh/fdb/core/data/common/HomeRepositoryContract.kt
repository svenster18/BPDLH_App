package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.ActivityDataSourceApi
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.GroupProfileDataSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import io.reactivex.Single

/**
 * Created by hahn on 12/10/23.
 * Project: BPDLH App
 */
interface HomeRepositoryContract {

    fun fetchGroupProfileByUserId(userId: String): Single<BaseDataSourceApi<GroupProfileDataSourceApi>>

    fun fetchMemberApplicationByUserId(userId: String): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun getActivity(userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>>

    fun getGeneralDescription(userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>>

    fun getPartner(userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>>
}