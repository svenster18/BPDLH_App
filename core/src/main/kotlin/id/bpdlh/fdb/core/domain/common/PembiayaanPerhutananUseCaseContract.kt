package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.post.PermohonanPembiayaanPerhutananSosialPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanNonPerhutananSosial
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File

interface PembiayaanPerhutananUseCaseContract {

    fun fetchDataPembiayaanPerhutanan(
        userId: String,
        param: HashMap<String, String>,
        type: Int
    ): Single<ResultState<List<PembiayaanPerhutananEntity>>>

    fun fetchDataMitra(
        userId: String,
        param: HashMap<String, String>,
        type: Int
    ): Single<ResultState<List<Mitra>>>

    fun fetchDataPembiayaanNonPerhutanan(
        param: HashMap<String, String>,
        type: Int
    ): ResultState<List<PembiayaanPerhutananEntity>>

    fun submitFormulirPembiayaanPerhutananSosial(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<ResultState<MemberApplicationEntity>>

    fun saveFormulirPembiayaanPerhutananSosialDraft(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<ResultState<MemberApplicationEntity>>

    fun submitFormulirPembiayaanNonPerhutananSosial(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<ResultState<MemberApplicationEntity>>

    fun saveFormulirPembiayaanNonPerhutananSosialDraft(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<ResultState<MemberApplicationEntity>>

    fun submitPermohonanPembiayaan(
        memberApplicationId: String,
    ): Single<ResultState<DaftarPemohonEntity>>

    fun uploadFile(file: File): Single<ResultState<String>>
    fun getSingleFile(id: String): Single<ResultState<GetFileSourceApi>>
    fun fetchFormulirPembiayaanNonPerhutananSosial(status: Int): Maybe<FormulirPembiayaanNonPerhutananSosial>
    fun insertFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial)
    fun updateFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial)
    fun fetchMitra(formulirId: Int): Flowable<List<Mitra>>
    fun insertMitra(mitra: List<Mitra>)
    fun updateMitra(mitra: Mitra)
    fun createOtherDocumentDraft(otherDocumentPost: OtherDocumentPost): Single<ResultState<OtherDocumentEntity>>
    fun fetchFile(parentId: Int, type: Int): Flowable<List<FileEntity>>
    fun insertFile(fileEntity: List<FileEntity>)
    fun updateFile(fileEntity: FileEntity)
    fun fetchJaminan(formulirId: Int): Flowable<List<JaminanEntity>>
    fun insertJaminan(jaminan: List<JaminanEntity>)
    fun updateJaminan(jaminan: JaminanEntity)
}