package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.RegistrasiKelompokRepositoryContract
import id.bpdlh.fdb.core.data.post.RegistrasiKelompokPost
import id.bpdlh.fdb.core.domain.common.RegistrasiKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.entities.RegistrasiKelompokEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single

/**
 * Created by hahn on 05/10/23.
 * Project: BPDLH App
 */
class RegistrasiKelompokUseCase(private val repoContract: RegistrasiKelompokRepositoryContract): RegistrasiKelompokUseCaseContract {

    override fun submitData(registerKelompokPost: RegistrasiKelompokPost): Single<ResultState<RegistrasiKelompokEntity>> {
        return repoContract.submitData(registerKelompokPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                RegistrasiKelompokEntity(
                    userID = response.groupProfile?.userId,
                    email = response.groupProfile?.user?.email,
                    status = response.status,
                    role = response.groupProfile?.user?.role
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<RegistrasiKelompokEntity>(it)
        }.compose(singleIo())
    }
}