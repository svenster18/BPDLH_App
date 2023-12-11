package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.BusinessPartnerSourceApi
import id.bpdlh.fdb.core.data.entities.DaftarPermohonanSourceApi
import id.bpdlh.fdb.core.data.entities.FileServiceSourceApi
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.post.PermohonanPembiayaanPerhutananSosialPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanNonPerhutananSosial
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File

interface PermohonanPembiayaanRepositoryContract {
    fun fetchMemberApplicationByUserId(userId: String): Single<BaseDataSourceApi<MemberApplicationSourceApi>>
    fun fetchBusinessPartnerDraftByUserId(userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>>
    fun fetchBusinessPartnerByUserId(userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>>

    fun updateSocialForestry(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun updateSocialForestryDraft(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun updateNonSocialForestry(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun updateNonSocialForestryDraft(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun submitApplication(
        memberApplicationId: String
    ): Single<BaseDataSourceApi<DaftarPermohonanSourceApi>>

    fun uploadFile(file: File): Single<BaseDataSourceApi<FileServiceSourceApi>>
    fun getSingleFile(id: String): Single<BaseDataSourceApi<GetFileSourceApi>>
    fun getFormulirPembiayaanNonPerhutananSosial(status: Int): Maybe<FormulirPembiayaanNonPerhutananSosial>
    fun insertFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial)
    fun updateFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial)
    fun getMitra(formulirId: Int): Flowable<List<Mitra>>
    fun insertMitra(mitra: List<Mitra>)
    fun updateMitra(mitra: Mitra)
    fun createOtherDocumentDraft(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>>
    fun getFile(parentId: Int, type: Int): Flowable<List<FileEntity>>
    fun insertFile(fileEntity: List<FileEntity>)
    fun updateFile(fileEntity: FileEntity)
    fun getJaminan(formulirId: Int): Flowable<List<JaminanEntity>>
    fun insertJaminan(jaminanEntity: List<JaminanEntity>)
    fun updateJaminan(jaminanEntity: JaminanEntity)
}