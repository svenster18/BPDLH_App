package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

/**
 * Created by hahn on 23/09/23.
 * Project: BPDLH App
 */
interface DaftarAnggotaPemohonUseCaseContract {
    fun createDataDebitur(
        dataList: List<DataDebiturEntity>,
        userId: String
    ): Single<ResultState<List<DataDebiturEntity>>>

    fun submitDataDebitur(memberApplicationId: String): Single<ResultState<DaftarPemohonEntity>>

    fun fetchDaftarAnggotaPemohonNonSosial(): ResultState<List<DataDebiturNonSosialEntity>>

    fun insertDataToDb(data: DataDebiturEntity)

    fun updateDataToDb(data: DataDebiturEntity)

    fun deleteData(data: DataDebiturEntity)

    fun deleteAllData()

    fun fetchDataFromDb(): Flow<List<DataDebiturEntity>>
}