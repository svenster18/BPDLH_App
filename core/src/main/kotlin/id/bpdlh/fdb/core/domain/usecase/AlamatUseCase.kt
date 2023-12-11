package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.AlamatRepositoryContract
import id.bpdlh.fdb.core.domain.common.AlamatUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataAlamatEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single

class AlamatUseCase(private val repo: AlamatRepositoryContract) : AlamatUseCaseContract {

    override fun fetchProvince(): Single<ResultState<List<DataAlamatEntity>>> {
        return repo.fetchProvince().map {
            return@map responseBaseDataSourceApiToResultState(it) { alamat ->
                val result = mutableListOf<DataAlamatEntity>()
                alamat.forEach { data ->
                    result.add(DataAlamatEntity(
                        id = data.id,
                        name = data.name
                    ))
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataAlamatEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchRegency(provinceId: String): Single<ResultState<List<DataAlamatEntity>>> {
        return repo.fetchRegency(provinceId).map {
            return@map responseBaseDataSourceApiToResultState(it) { alamat ->
                val result = mutableListOf<DataAlamatEntity>()
                alamat.forEach { data ->
                    result.add(DataAlamatEntity(
                        id = data.id,
                        name = data.name
                    ))
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataAlamatEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchDistrict(regencyId: String): Single<ResultState<List<DataAlamatEntity>>> {
        return repo.fetchDistrict(regencyId).map {
            return@map responseBaseDataSourceApiToResultState(it) { alamat ->
                val result = mutableListOf<DataAlamatEntity>()
                alamat.forEach { data ->
                    result.add(DataAlamatEntity(
                        id = data.id,
                        name = data.name
                    ))
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataAlamatEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchVillage(districtId: String): Single<ResultState<List<DataAlamatEntity>>> {
        return repo.fetchVillage(districtId).map {
            return@map responseBaseDataSourceApiToResultState(it) { alamat ->
                val result = mutableListOf<DataAlamatEntity>()
                alamat.forEach { data ->
                    result.add(DataAlamatEntity(
                        id = data.id,
                        name = data.name
                    ))
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataAlamatEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchZipCode(villageId: String): Single<ResultState<List<DataAlamatEntity>>> {
        return repo.fetchZipCode(villageId).map {
            return@map responseBaseDataSourceApiToResultState(it) { alamat ->
                val result = mutableListOf<DataAlamatEntity>()
                alamat.forEach { data ->
                    result.add(DataAlamatEntity(
                        id = data.id,
                        name = data.name
                    ))
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataAlamatEntity>>(it)
        }.compose(singleIo())
    }
}