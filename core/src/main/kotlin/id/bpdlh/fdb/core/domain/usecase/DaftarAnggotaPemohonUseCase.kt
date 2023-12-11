package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.getDataType
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.DaftarPermohonanRepositoryContract
import id.bpdlh.fdb.core.domain.common.DaftarAnggotaPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

/**
 * Created by hahn on 25/09/23.
 * Project: BPDLH App
 */
class DaftarAnggotaPemohonUseCase(private val repo: DaftarPermohonanRepositoryContract): DaftarAnggotaPemohonUseCaseContract {

    override fun createDataDebitur(
        dataList: List<DataDebiturEntity>,
        userId: String
    ): Single<ResultState<List<DataDebiturEntity>>> {
        return repo.createDataDebitur(dataList, userId).map { data ->
            return@map responseBaseDataSourceApiToResultState(data) { response ->
                val result = mutableListOf<DataDebiturEntity>()
                response.forEach {
                    val item = DataDebiturEntity(
                        id = it.memberApplicationDetail?.userId.orEmpty(),
                        memberApplicationId = it.memberApplicationDetail.memberApplicationId.orEmpty(),
                        nama = it.name,
                        email = it.email,
                        tanggalLahir = it.dateOfBirth,
                        noTelp = it.phoneNumber,
                    )
                    result.add(item)
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataDebiturEntity>>(it)
        }.compose(singleIo())
    }

    override fun submitDataDebitur(memberApplicationId: String): Single<ResultState<DaftarPemohonEntity>> {
        return repo.submitDataDebitur(memberApplicationId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                DaftarPemohonEntity(
                    response.id.orEmpty(),
                    response.requestDate.orEmpty(),
                    dataType = getDataType(response.status.orEmpty()),
                    status = response.status.orEmpty(),
                    totalAnggota = response.totalMember ?: 0,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<DaftarPemohonEntity>(it)
        }.compose(singleIo())
    }

    override fun insertDataToDb(data: DataDebiturEntity) {
        repo.insertData(data)
    }

    override fun updateDataToDb(data: DataDebiturEntity) {
        repo.updateDataToDb(data)
    }

    override fun deleteData(data: DataDebiturEntity) {
        repo.deleteData(data)
    }

    override fun deleteAllData() {
        repo.deleteAll()
    }

    override fun fetchDataFromDb(): Flow<List<DataDebiturEntity>> {
        return repo.fetchDebiturFromDatabase()
    }

    override fun fetchDaftarAnggotaPemohonNonSosial(): ResultState<List<DataDebiturNonSosialEntity>> {
        val dataDebiturs = listOf(
            DataDebiturNonSosialEntity(
                nik = "1234567890123456",
                nama = "John Doe",
                tanggalLahir = "12 April 1998",
                email = "mail@gmail.com",
                noTelp = "029282726727",
                jenisLayanan = "Tunda terbang",
                nilaiPermohonan = 50000000
            ),
            DataDebiturNonSosialEntity(
                nik = "1234567890123456",
                nama = "John Doe",
                tanggalLahir = "12 April 1998",
                email = "mail@gmail.com",
                noTelp = "029282726727",
                jenisLayanan = "Hasil Hutan Bukan Kayu",
                nilaiPermohonan = 50000000
            ),
        )

        return ResultState.Success(dataDebiturs)
    }
}