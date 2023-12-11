package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.data.common.HomeRepositoryContract
import id.bpdlh.fdb.core.data.entities.ActivityDataSourceApi
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.GroupProfileDataSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.data.sources.remote.UserApi
import io.reactivex.Single

/**
 * Created by hahn on 12/10/23.
 * Project: BPDLH App
 */
class HomeRepository(private val userApi: UserApi): HomeRepositoryContract {
    override fun fetchGroupProfileByUserId(userId: String): Single<BaseDataSourceApi<GroupProfileDataSourceApi>> =
        userApi.fetchGroupProfileByUserid(userId)

    override fun fetchMemberApplicationByUserId(userId: String): Single<BaseDataSourceApi<MemberApplicationSourceApi>> =
        userApi.fetchMemberApplicationByUserId(userId)

    override fun getActivity(userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>> =
        userApi.getActivity(userId)

    override fun getGeneralDescription(userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>> =
        userApi.getGeneralDescription(userId)

    override fun getPartner(userId: String): Single<BaseDataSourceApi<List<ActivityDataSourceApi>>> =
        userApi.getPartner(userId)
}