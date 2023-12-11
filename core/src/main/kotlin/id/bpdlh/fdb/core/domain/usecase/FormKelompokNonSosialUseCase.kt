package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.FormKelompokNonSosialRepositoryContract
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import id.bpdlh.fdb.core.domain.common.FormKelompokNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.internal.toImmutableList
import java.io.File

class FormKelompokNonSosialUseCase(private val repositoryContract: FormKelompokNonSosialRepositoryContract) :
    FormKelompokNonSosialUseCaseContract {
    override fun fetchDraft(): Maybe<GroupProfileNonSosial> {
        return repositoryContract.getDraft()
    }

    override fun insertDraft(groupProfileNonSosial: GroupProfileNonSosial) {
        repositoryContract.insert(groupProfileNonSosial)
    }

    override fun updateDraft(groupProfileNonSosial: GroupProfileNonSosial) {
        repositoryContract.update(groupProfileNonSosial)
    }

    override fun deleteDraft() {
        repositoryContract.deleteDraft()
    }

    override fun draft(
        userId: String,
        body: FormulirKelompokNonSosialPost
    ): Single<ResultState<GroupProfileNonSosial>> {
        return repositoryContract.draft(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GroupProfileNonSosial>(it)
        }.compose(singleIo())
    }

    override fun update(
        userId: String,
        body: FormulirKelompokNonSosialPost
    ): Single<ResultState<GroupProfileNonSosial>> {
        return repositoryContract.update(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GroupProfileNonSosial>(it)
        }.compose(singleIo())
    }

    override fun submit(
        userId: String,
        body: FormulirKelompokNonSosialPost
    ): Single<ResultState<GroupProfileNonSosial>> {
        return repositoryContract.submit(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GroupProfileNonSosial>(it)
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