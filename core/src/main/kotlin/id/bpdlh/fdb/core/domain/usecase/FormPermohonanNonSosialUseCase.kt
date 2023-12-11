package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.FormPermohonanNonSosialRepositoryContract
import id.bpdlh.fdb.core.data.common.GroupApplicationRepositoryContract
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.domain.common.FormPermohonanNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.common.GroupApplicationUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MitraEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single
import okhttp3.internal.toImmutableList
import java.io.File

class FormPermohonanNonSosialUseCase(private val repositoryContract: FormPermohonanNonSosialRepositoryContract) :
    FormPermohonanNonSosialUseCaseContract {

    override fun getMemberApplication(userId: String): Single<ResultState<MemberApplicationDataEntity>> {
        return repositoryContract.getMemberApplication(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun delayCuttingDraft(userId: String, delayCuttingPost: MemberApplicationPost) : Single<ResultState<MemberApplicationDataEntity>> {
        return repositoryContract.delayCuttingDraft(userId, delayCuttingPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun delayCuttingUpdate(userId: String, delayCuttingPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>> {
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
    ) : Single<ResultState<MemberApplicationDataEntity>> {
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
    ): Single<ResultState<MemberApplicationDataEntity>> {
        return repositoryContract.nonForestyComodityUpdate(userId, nonForestyComodityPost).map {
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
    ): Single<ResultState<MemberApplicationDataEntity>> {
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
    ) : Single<ResultState<MemberApplicationDataEntity>> {
        return repositoryContract.nonWoodForestProductUpdate(userId, nonWoodForestProductPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun postOtherDocumentDraft(otherDocumentPost: OtherDocumentPost): Single<ResultState<OtherDocumentEntity>> {
        return repositoryContract.postOtherDocumentDraft(otherDocumentPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                OtherDocumentEntity(
                    response.name,
                    response.description,
                    response.file,
                    response.createdAt,
                    response.fileUrl
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<OtherDocumentEntity>(it)
        }.compose(singleIo())
    }

    override fun getOtherDocumentDraft(userId: String): Single<ResultState<List<OtherDocumentEntity>>> {
        return repositoryContract.getOtherDocumentDraft(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listDocument = mutableListOf<OtherDocumentEntity>()
                response.forEach { otherDocument ->
                    listDocument.add(
                        OtherDocumentEntity(
                            id = otherDocument.id,
                            name = otherDocument.name,
                            file = otherDocument.file,
                            fileUrl = otherDocument.fileUrl,
                            description = otherDocument.description
                        )
                    )
                }
                listDocument.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<OtherDocumentEntity>>(it)
        }.compose(singleIo())
    }

    override fun getOtherDocument(userId: String): Single<ResultState<List<OtherDocumentEntity>>> {
        return repositoryContract.getOtherDocumentDraft(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listDocument = mutableListOf<OtherDocumentEntity>()
                response.forEach { otherDocument ->
                    listDocument.add(
                        OtherDocumentEntity(
                            id = otherDocument.id,
                            name = otherDocument.name,
                            file = otherDocument.file,
                            fileUrl = otherDocument.fileUrl,
                            description = otherDocument.description
                        )
                    )
                }
                listDocument.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<OtherDocumentEntity>>(it)
        }.compose(singleIo())
    }

    override fun deleteOtherDocument(id: String): Single<ResultState<Any>> {
        return repositoryContract.deleteOtherDocument(id).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun getBusinessPartner(id: String): Single<ResultState<List<MitraEntity>>> {
        return repositoryContract.getBusinessPartner(id).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listMitra = mutableListOf<MitraEntity>()
                response.forEach { otherDocument ->
                    listMitra.add(
                        MitraEntity(
                            id = otherDocument.id,
                            name = otherDocument.name
                        )
                    )
                }
                listMitra.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError(it)
        }.compose(singleIo())
    }

    override fun uploadFile(file: File): Single<ResultState<String>> {
        return repositoryContract.uploadFile(file).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response.id
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<String>(it)
        }.compose(singleIo())
    }

    override fun getSingleFile(id: String): Single<ResultState<GetFileSourceApi>> {
        return repositoryContract.getSingleFile(id).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GetFileSourceApi>(it)
        }.compose(singleIo())
    }

}