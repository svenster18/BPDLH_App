package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File

interface FormKelompokNonSosialUseCaseContract {
    fun fetchDraft(): Maybe<GroupProfileNonSosial>
    fun insertDraft(formKelompokNonSosial: GroupProfileNonSosial)
    fun updateDraft(formKelompokNonSosial: GroupProfileNonSosial)
    fun deleteDraft()
    fun draft(userId: String, body: FormulirKelompokNonSosialPost) : Single<ResultState<GroupProfileNonSosial>>
    fun update(userId: String, body: FormulirKelompokNonSosialPost) : Single<ResultState<GroupProfileNonSosial>>
    fun submit(userId: String, body: FormulirKelompokNonSosialPost) : Single<ResultState<GroupProfileNonSosial>>
    fun postOtherDocumentDraft(otherDocumentPost: OtherDocumentPost): Single<ResultState<OtherDocumentEntity>>
    fun getOtherDocumentDraft(userId: String): Single<ResultState<List<OtherDocumentEntity>>>
    fun getOtherDocument(userId: String): Single<ResultState<List<OtherDocumentEntity>>>
    fun deleteOtherDocument(id: String): Single<ResultState<Any>>
    fun uploadFile(file: File): Single<ResultState<String>>
    fun getSingleFile(id: String): Single<ResultState<GetFileSourceApi>>
}