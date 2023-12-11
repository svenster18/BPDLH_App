package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.DaftarPermohonanSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

/**
 * Created by hahn on 05/11/23.
 * Project: BPDLH App
 */
interface DaftarPermohonanRepositoryContract {
    fun fetchGroupApplication(userId: String): Single<BaseDataSourceApi<List<DaftarPermohonanSourceApi>>>

    fun fetchGroupMemberApplication(memberId: String): Single<BaseDataSourceApi<List<MemberApplicationSourceApi>>>

    fun createDataDebitur(
        listData: List<DataDebiturEntity>,
        userId: String
    ): Single<BaseDataSourceApi<List<MemberApplicationSourceApi>>>

    fun submitDataDebitur(memberApplicationId: String): Single<BaseDataSourceApi<DaftarPermohonanSourceApi>>

    //db
    fun fetchDebiturFromDatabase(): Flow<List<DataDebiturEntity>>

    fun insertData(data: DataDebiturEntity)

    fun updateDataToDb(data: DataDebiturEntity)

    fun deleteData(data: DataDebiturEntity)

    fun deleteAll()
}