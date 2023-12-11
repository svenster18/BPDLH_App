package id.bpdlh.fdb.core.data.sources.remote

import id.bpdlh.fdb.core.data.entities.AfiliasiPendampingApi
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.FungsiKawasanSourceApi
import id.bpdlh.fdb.core.data.entities.KategoriKegiatanApi
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by hahn on 20/11/23.
 * Project: BPDLH App
 */
interface MasterDataApi {

    @GET("v1/master-data/social-area-functions")
    fun fetchFungsiKawasan(): Single<BaseDataSourceApi<List<FungsiKawasanSourceApi>>>

    @GET("v1/master-data/companion-affiliate")
    fun fetchAfiliasiPendamping(): Single<BaseDataSourceApi<List<AfiliasiPendampingApi>>>

    @GET("v1/master-data/activities-category")
    fun fetchKategoriKegiatan(): Single<BaseDataSourceApi<List<KategoriKegiatanApi>>>

    @GET("v1/master-data/financed-business-category")
    fun fetchKategoriUsahaDibiayai(): Single<BaseDataSourceApi<List<KategoriKegiatanApi>>>
}