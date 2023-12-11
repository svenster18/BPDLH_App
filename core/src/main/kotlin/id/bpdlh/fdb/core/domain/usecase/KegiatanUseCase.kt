package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.data.common.KegiatanRepositoryContract
import id.bpdlh.fdb.core.data.sources.local.entity.Kegiatan
import id.bpdlh.fdb.core.domain.common.KegiatanUseCaseContract
import io.reactivex.Maybe

class KegiatanUseCase(private val repositoryContract: KegiatanRepositoryContract) :
    KegiatanUseCaseContract {

    override fun getAll(): Maybe<List<Kegiatan>> {
        return repositoryContract.getAll()
    }

    override fun insertAll(kegiatan: List<Kegiatan>) {
        repositoryContract.insertAll(kegiatan)
    }

    override fun update(kegiatan: Kegiatan) {
        repositoryContract.update(kegiatan)
    }

    override fun deleteAll() {
        repositoryContract.deleteAll()
    }

}