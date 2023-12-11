package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.GroupApplicationRepositoryContract
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.domain.common.GroupApplicationUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single
import okhttp3.internal.toImmutableList

class GroupApplicationUseCase(private val repositoryContract: GroupApplicationRepositoryContract) :
    GroupApplicationUseCaseContract {
    override fun getGroupApplication(userId: String): Single<ResultState<List<GroupApplicationEntity>>> {
        return repositoryContract.getGroupApplication(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun submitApplication(memberApplicationId: String): Single<ResultState<Any>> {
        return repositoryContract.submitApplication(memberApplicationId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun deleteApplication(userId: String): Single<ResultState<Any>> {
        TODO("Not yet implemented")
    }

    override fun getMemberApplication(memberApplicationId: String): Single<ResultState<List<DataDebiturNonSosialEntity>>> {
        return repositoryContract.getMemberApplication(memberApplicationId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val tempList = mutableListOf<DataDebiturNonSosialEntity>()
                response.forEach {
                    tempList.add(DataDebiturNonSosialEntity(
                        id = it.id,
                        nama = it.name,
                        nik = it.ktp,
                        tanggalLahir = it.dateOfBirth,
                        email = it.email,
                        noTelp = it.phoneNumber,
                        jenisLayanan = it.serviceType,
                        nilaiPermohonan = it.amountOfLoan,
                        userId = it.memberApplicationDetail?.userId,
                        memberApplicationId = it.memberApplicationDetail?.memberApplicationId,
                        isFinancingSubmittable = it.isFinancingSubmittable,
                        isSend = true
                    ))
                }
                tempList.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataDebiturNonSosialEntity>>(it)
        }.compose(singleIo())

    }

    override fun createApplication(
        userId: String,
        groupApplicationMemberPost: List<DataDebiturNonSosialEntity>
    ): Single<ResultState<List<DataDebiturNonSosialEntity>>> {
        return repositoryContract.createApplication(userId, groupApplicationMemberPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val tempList = mutableListOf<DataDebiturNonSosialEntity>()
                response.forEach {
                    tempList.add(DataDebiturNonSosialEntity(
                        id = it.id,
                        nama = it.name,
                        nik = it.ktp,
                        tanggalLahir = it.dateOfBirth,
                        email = it.email,
                        noTelp = it.phoneNumber,
                        jenisLayanan = it.serviceType,
                        nilaiPermohonan = it.amountOfInstallment,
                        memberApplicationId = it.memberApplicationDetail?.memberApplicationId,
                        isFinancingSubmittable = it.isFinancingSubmittable,
                    ))
                }
                tempList.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<DataDebiturNonSosialEntity>>(it)
        }.compose(singleIo())
    }

    override fun delayCutting(
        userId: String,
        body: MemberApplicationPost
    ): Single<ResultState<Any>> {
        return repositoryContract.delayCutting(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun nonForestyComodity(
        userId: String,
        body: MemberApplicationPost
    ): Single<ResultState<Any>> {
        return repositoryContract.nonForestyComodity(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun nonWoodForestProduct(
        userId: String,
        body: MemberApplicationPost
    ): Single<ResultState<Any>> {
        return repositoryContract.nonWoodForestProduct(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

}