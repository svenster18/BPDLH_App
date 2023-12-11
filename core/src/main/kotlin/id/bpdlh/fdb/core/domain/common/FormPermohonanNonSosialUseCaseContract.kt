package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MitraEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import io.reactivex.Single
import java.io.File

interface FormPermohonanNonSosialUseCaseContract {
    fun getMemberApplication(userId : String): Single<ResultState<MemberApplicationDataEntity>>
    fun delayCuttingDraft(userId : String, memberApplicationPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>>
    fun delayCuttingUpdate(userId : String, memberApplicationPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>>
    fun nonForestyComodityDraft(userId: String, memberApplicationPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>>
    fun nonForestyComodityUpdate(userId: String, memberApplicationPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>>
    fun nonWoodForestProductDraft(userId: String, memberApplicationPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>>
    fun nonWoodForestProductUpdate(userId: String, memberApplicationPost: MemberApplicationPost): Single<ResultState<MemberApplicationDataEntity>>
    fun postOtherDocumentDraft(otherDocumentPost: OtherDocumentPost): Single<ResultState<OtherDocumentEntity>>
    fun getOtherDocumentDraft(userId: String): Single<ResultState<List<OtherDocumentEntity>>>
    fun getOtherDocument(userId: String): Single<ResultState<List<OtherDocumentEntity>>>
    fun deleteOtherDocument(id: String): Single<ResultState<Any>>
    fun getBusinessPartner(id: String): Single<ResultState<List<MitraEntity>>>
    fun uploadFile(file: File): Single<ResultState<String>>
    fun getSingleFile(id: String): Single<ResultState<GetFileSourceApi>>
}