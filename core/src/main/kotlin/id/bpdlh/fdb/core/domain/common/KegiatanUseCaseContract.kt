package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.data.sources.local.entity.Kegiatan
import io.reactivex.Maybe

interface KegiatanUseCaseContract {
    fun getAll(): Maybe<List<Kegiatan>>
    fun insertAll(kegiatan: List<Kegiatan>)
    fun update(kegiatan: Kegiatan)
    fun deleteAll()
}