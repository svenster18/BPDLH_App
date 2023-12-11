package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.common.utils.AppExecutors
import id.bpdlh.fdb.core.data.common.KegiatanRepositoryContract
import id.bpdlh.fdb.core.data.sources.local.entity.Kegiatan
import id.bpdlh.fdb.core.data.sources.local.room.KegiatanDao
import io.reactivex.Maybe

class KegiatanRepository(
    private val kegiatanDao: KegiatanDao,
    private val appExecutors: AppExecutors
) : KegiatanRepositoryContract {

    override fun getAll(): Maybe<List<Kegiatan>> = kegiatanDao.getKegiatan()
    override fun insertAll(kegiatan: List<Kegiatan>) {
        appExecutors.diskIO().execute { kegiatanDao.insertAll(kegiatan) }
    }

    override fun update(kegiatan: Kegiatan) {
        appExecutors.diskIO().execute { kegiatanDao.update(kegiatan) }
    }

    override fun deleteAll() {
        appExecutors.diskIO().execute{kegiatanDao.deleteAll()}
    }
}