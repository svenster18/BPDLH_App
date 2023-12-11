package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.data.common.DaftarPermohonanRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.DaftarPermohonanSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.data.sources.local.room.DataDebiturDao
import id.bpdlh.fdb.core.data.sources.remote.DaftarPermohonanApi
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

/**
 * Created by hahn on 05/11/23.
 * Project: BPDLH App
 */
class DaftarPermohonanRepository(private val api: DaftarPermohonanApi, private val dao: DataDebiturDao): DaftarPermohonanRepositoryContract {

    override fun fetchGroupApplication(userId: String): Single<BaseDataSourceApi<List<DaftarPermohonanSourceApi>>> {
        return api.fetchGroupApplication(userId)
    }

    override fun fetchGroupMemberApplication(memberId: String): Single<BaseDataSourceApi<List<MemberApplicationSourceApi>>> {
        return api.fetchGroupMemberApplication(memberId)
    }

    override fun createDataDebitur(
        listData: List<DataDebiturEntity>,
        userId: String
    ): Single<BaseDataSourceApi<List<MemberApplicationSourceApi>>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        data.addFormDataPart("user_id", userId)
        listData.forEachIndexed { index, entity ->
            data.addFormDataPart("member[$index][name]", entity.nama.orEmpty())
            data.addFormDataPart("member[$index][ktp]", entity.nik.orEmpty())
            data.addFormDataPart("member[$index][date_of_birth]", entity.tanggalLahir.orEmpty())
            data.addFormDataPart("member[$index][email]", entity.email.orEmpty())
            data.addFormDataPart("member[$index][phone_number]", entity.noTelp.orEmpty())
        }
        return api.createDataDebitur(data.build())
    }

    override fun submitDataDebitur(memberApplicationId: String): Single<BaseDataSourceApi<DaftarPermohonanSourceApi>> {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("member_application_id", memberApplicationId)
            .build()
        return api.submitDataDebitur(multipartBody)
    }

    override fun fetchDebiturFromDatabase(): Flow<List<DataDebiturEntity>> {
        return dao.getData()
    }

    override fun insertData(data: DataDebiturEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(data)
        }
    }

    override fun updateDataToDb(data: DataDebiturEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.update(data)
        }
    }

    override fun deleteData(data: DataDebiturEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteData(data)
        }
    }

    override fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
        }
    }
}