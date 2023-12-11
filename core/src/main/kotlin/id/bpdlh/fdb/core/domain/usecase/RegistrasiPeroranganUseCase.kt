package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.RegistrasiPeroranganRepositoryContract
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.common.RegistrasiPeroranganUseCaseContract
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Maybe
import io.reactivex.Single

class RegistrasiPeroranganUseCase(private val repositoryContract: RegistrasiPeroranganRepositoryContract) :
    RegistrasiPeroranganUseCaseContract {
    override fun fetchDraft(): Maybe<RegistrasiPerorangan> {
        return repositoryContract.getDraft()
    }

    override fun insertDraft(registrasiPerorangan: RegistrasiPerorangan) {
        repositoryContract.insert(registrasiPerorangan)
    }

    override fun updateDraft(registrasiPerorangan: RegistrasiPerorangan) {
        repositoryContract.update(registrasiPerorangan)
    }

    override fun getMemberApplication(userId: String): Single<ResultState<MemberApplicationDataEntity>> {
        return repositoryContract.getMemberApplication(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun submitFormulirPembiayaanNonPerhutananSosial(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<ResultState<MemberApplicationEntity>> {
        return repositoryContract.updateNonSocialForestry(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userId,
                    response.email,
                    response.status,
                    "Anggota",
                    response.isSubmittable,
                    response.isSubmitted,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun saveFormulirPembiayaanNonPerhutananSosialDraft(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<ResultState<MemberApplicationEntity>> {
        return repositoryContract.updateNonSocialForestryDraft(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userId,
                    response.email,
                    response.status,
                    "Anggota",
                    response.isSubmittable,
                    response.isSubmitted,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun delayCuttingDraft(
        userId: String,
        delayCuttingPost: MemberApplicationPost
    ): Single<ResultState<MemberApplicationDataEntity>> {
        return repositoryContract.delayCuttingDraft(userId, delayCuttingPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun delayCuttingUpdate(userId: String, delayCuttingPost: MemberApplicationPost): Single<ResultState<Any>> {
        return repositoryContract.delayCuttingUpdate(userId, delayCuttingPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun nonForestyComodityDraft(
        userId: String,
        nonForestyComodityPost: MemberApplicationPost
    ) : Single<ResultState<Any>> {
        return repositoryContract.nonForestyComodityDraft(userId, nonForestyComodityPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun nonForestyComodityUpdate(
        userId: String,
        nonForestyComodityPost: MemberApplicationPost
    ): Single<ResultState<Any>> {
        return repositoryContract.nonForestyComodityDraft(userId, nonForestyComodityPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun nonWoodForestProductDraft(
        userId: String,
        nonWoodForestProductPost: MemberApplicationPost
    ): Single<ResultState<Any>> {
        return repositoryContract.nonWoodForestProductDraft(userId, nonWoodForestProductPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun nonWoodForestProductUpdate(
        userId: String,
        nonWoodForestProductPost: MemberApplicationPost
    ) : Single<ResultState<Any>> {
        return repositoryContract.nonWoodForestProductUpdate(userId, nonWoodForestProductPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

}