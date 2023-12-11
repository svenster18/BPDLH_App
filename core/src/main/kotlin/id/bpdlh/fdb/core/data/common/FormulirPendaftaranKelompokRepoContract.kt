package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.AfiliasiPendampingApi
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.FungsiKawasanSourceApi
import id.bpdlh.fdb.core.data.entities.GroupProfileDataSourceApi
import id.bpdlh.fdb.core.data.entities.KategoriKegiatanApi
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import io.reactivex.Single

/**
 * Created by hahn on 21/10/23.
 * Project: BPDLH App
 */
interface FormulirPendaftaranKelompokRepoContract {
    fun saveDraft(userId: String, body: GroupProfileEntity): Single<BaseDataSourceApi<GroupProfileDataSourceApi>>

    fun submit(userId: String, body: GroupProfileEntity): Single<BaseDataSourceApi<GroupProfileDataSourceApi>>

    fun fetchFungsiKawasan(): Single<BaseDataSourceApi<List<FungsiKawasanSourceApi>>>

    fun fetchAfiliasiPendamping(): Single<BaseDataSourceApi<List<AfiliasiPendampingApi>>>

    fun fetchKategoriKegiatan(): Single<BaseDataSourceApi<List<KategoriKegiatanApi>>>

    fun fetchKategoriUsahaDibiayai(): Single<BaseDataSourceApi<List<KategoriKegiatanApi>>>
}