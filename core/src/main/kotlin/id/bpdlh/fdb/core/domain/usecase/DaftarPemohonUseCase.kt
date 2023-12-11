package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertStringDate
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.common.DaftarPermohonanRepositoryContract
import id.bpdlh.fdb.core.domain.common.DaftarPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single

/**
 * Created by hahn on 20/09/23.
 * Project: BPDLH App
 */
class DaftarPemohonUseCase(private val repo: DaftarPermohonanRepositoryContract): DaftarPemohonUseCaseContract {

    override fun fetchGroupApplication(userId: String): Single<ResultState<List<DaftarPemohonEntity>>> {
        return repo.fetchGroupApplication(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val result = mutableListOf<DaftarPemohonEntity>()
                response.forEach { data ->
                    val date = data.requestDate?.convertStringDate(input = Constants.ISO_DATE_FORMAT,
                        output = Constants.NEW_DATE_FORMAT)
                    val item = DaftarPemohonEntity(
                        id = data.id.orEmpty(),
                        date = date ?: "-",
                        dataType = getDataType(data.status.orEmpty()),
                        status = data.status.orEmpty(),
                        totalAnggota = data.totalMember ?: 0
                    )
                    result.add(item)
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DaftarPemohonEntity>>(it)
        }.compose(singleIo())
    }

    private fun getDataType(input: String) : Int {
        return when(input) {
            "Draft" -> FdbBadge.DRAFT
            "On Progress" -> FdbBadge.ON_PROGRESS
            "Menunggu Verifikasi" -> FdbBadge.ON_VERIFY
            "Terverifikasi" -> FdbBadge.SUCCESS
            else -> FdbBadge.DRAFT
        }
    }
}