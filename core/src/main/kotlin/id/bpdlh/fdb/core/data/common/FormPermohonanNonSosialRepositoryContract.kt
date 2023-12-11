package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.BusinessPartnerSourceApi
import id.bpdlh.fdb.core.data.entities.FileServiceSourceApi
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File

interface FormPermohonanNonSosialRepositoryContract {
    fun getMemberApplication(userId: String): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun delayCuttingDraft(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun delayCuttingUpdate(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun nonForestyComodityDraft(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun nonForestyComodityUpdate(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun nonWoodForestProductDraft(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun nonWoodForestProductUpdate(userId: String, body: MemberApplicationPost): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun postOtherDocumentDraft(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>
    fun getOtherDocumentDraft(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>
    fun getOtherDocument(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>>
    fun deleteOtherDocument(userId: String): Single<BaseDataSourceApi<Any>>
    fun getBusinessPartner(userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>>
    fun uploadFile(file: File): Single<BaseDataSourceApi<FileServiceSourceApi>>
    fun getSingleFile(id: String): Single<BaseDataSourceApi<GetFileSourceApi>>
}