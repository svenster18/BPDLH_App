package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.core.domain.entities.RegistrasiKelompokEntity
import io.reactivex.Single

/**
 * Created by hahn on 05/10/23.
 * Project: BPDLH App
 */
interface RegistrasiKelompokUseCaseContract {
    fun submitData(registerKelompokPost: RegistrasiKelompokPost): Single<ResultState<RegistrasiKelompokEntity>>

}