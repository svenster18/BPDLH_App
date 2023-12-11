package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.RegistrasiKelompokSourceApi
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import io.reactivex.Single
import retrofit2.http.Body

/**
 * Created by hahn on 08/10/23.
 * Project: BPDLH App
 */
interface RegistrasiKelompokRepositoryContract {

    fun submitData(@Body body: RegistrasiKelompokPost): Single<BaseDataSourceApi<RegistrasiKelompokSourceApi>>

}