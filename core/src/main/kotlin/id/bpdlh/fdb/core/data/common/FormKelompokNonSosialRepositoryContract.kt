package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.FileServiceSourceApi
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File

interface FormKelompokNonSosialRepositoryContract {
    fun getDraft(): Maybe<GroupProfileNonSosial>
    fun insert(groupProfileNonSosial: GroupProfileNonSosial)
    fun update(groupProfileNonSosial: GroupProfileNonSosial)
    fun deleteDraft()
    fun postOtherDocument(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>
    fun draft(userId: String, body: FormulirKelompokNonSosialPost): Single<BaseDataSourceApi<GroupProfileNonSosial>>
    fun update(userId: String, body: FormulirKelompokNonSosialPost): Single<BaseDataSourceApi<GroupProfileNonSosial>>
    fun submit(userId: String, body: FormulirKelompokNonSosialPost): Single<BaseDataSourceApi<GroupProfileNonSosial>>
    fun postOtherDocumentDraft(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>
    fun getOtherDocumentDraft(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>
    fun getOtherDocument(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>
    fun deleteOtherDocument(userId: String): Single<BaseDataSourceApi<Any>>
    fun uploadFile(file: File): Single<BaseDataSourceApi<FileServiceSourceApi>>
    fun getSingleFile(id: String): Single<BaseDataSourceApi<GetFileSourceApi>>
}