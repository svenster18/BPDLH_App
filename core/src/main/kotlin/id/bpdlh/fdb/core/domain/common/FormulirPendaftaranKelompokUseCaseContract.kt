package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.domain.entities.AfiliasiPendampingEntity
import id.bpdlh.fdb.core.domain.entities.FungsiKawasanEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.KategoriKegiatanEntity
import io.reactivex.Single

/**
 * Created by hahn on 20/10/23.
 * Project: BPDLH App
 */
interface FormulirPendaftaranKelompokUseCaseContract {

    fun submitData(userId: String, data: GroupProfileEntity): Single<ResultState<GroupProfileEntity>>

    fun saveToDraft(userId: String, data: GroupProfileEntity): Single<ResultState<GroupProfileEntity>>

    fun fetchFungsiKawasan(): Single<ResultState<List<FungsiKawasanEntity>>>

    fun fetchAfiliasiPendamping(): Single<ResultState<List<AfiliasiPendampingEntity>>>

    fun fetchKategoriKegiatan(): Single<ResultState<List<KategoriKegiatanEntity>>>

    fun fetchKategoriUsahaDibiayai(): Single<ResultState<List<KategoriKegiatanEntity>>>
}