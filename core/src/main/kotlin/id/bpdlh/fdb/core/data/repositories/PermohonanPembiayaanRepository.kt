package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.common.utils.AppExecutors
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertStringDateAPI
import id.bpdlh.fdb.core.data.common.PermohonanPembiayaanRepositoryContract
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
import id.bpdlh.fdb.core.data.sources.local.room.FileDao
import id.bpdlh.fdb.core.data.sources.local.room.FormulirPembiayaanNonPerhutananSosialDao
import id.bpdlh.fdb.core.data.sources.local.room.JaminanDao
import id.bpdlh.fdb.core.data.sources.local.room.MitraDao
import id.bpdlh.fdb.core.data.sources.remote.FileServiceApi
import id.bpdlh.fdb.core.data.sources.remote.PermohonanPembiayaanApi
import id.bpdlh.fdb.core.data.sources.remote.UserApi
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PermohonanPembiayaanRepository(
    private val formulirPembiayaanNonPerhutananSosialDao: FormulirPembiayaanNonPerhutananSosialDao,
    private val mitraDao: MitraDao,
    private val fileDao: FileDao,
    private val jaminanDao: JaminanDao,
    private val appExecutors: AppExecutors,
    private val permohonanPembiayaanApi: PermohonanPembiayaanApi,
    private val userApi: UserApi,
    private val fileServiceApi: FileServiceApi
) : PermohonanPembiayaanRepositoryContract {

    override fun fetchMemberApplicationByUserId(userId: String): Single<BaseDataSourceApi<MemberApplicationSourceApi>> =
        userApi.fetchMemberApplicationByUserId(userId)

    override fun fetchBusinessPartnerDraftByUserId(userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>> =
        permohonanPembiayaanApi.fetchBusinessPartnerDraftByUserId(userId)

    override fun fetchBusinessPartnerByUserId(userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>> =
        permohonanPembiayaanApi.fetchBusinessPartnerByUserId(userId)

    override fun updateSocialForestry(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>> {
        return permohonanPembiayaanApi.updateSocialForestry(
            userId,
            buildSocialForestryMultiPart(body)
        )
    }

    override fun updateSocialForestryDraft(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>> {
        return permohonanPembiayaanApi.updateSocialForestryDraft(
            userId,
            buildSocialForestryMultiPart(body)
        )
    }

    override fun updateNonSocialForestry(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>> {
        return when (body.jenis_layanan) {
            Constants.TUNDA_TEBANG -> permohonanPembiayaanApi.updateDelayCutting(
                userId,
                buildNonSocialForestryMultiPart(body)
            )

            Constants.HASIL_HUTAN_BUKAN_KAYU -> permohonanPembiayaanApi.updateNonWoodForestProduct(
                userId,
                buildNonSocialForestryMultiPart(body)
            )

            Constants.KOMODITAS_NON_KEHUTANAN -> permohonanPembiayaanApi.updateNonForestryComodity(
                userId,
                buildNonSocialForestryMultiPart(body)
            )

            else -> permohonanPembiayaanApi.updateDelayCutting(
                userId,
                buildNonSocialForestryMultiPart(body)
            )
        }
    }

    override fun updateNonSocialForestryDraft(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>> {
        return when (body.jenis_layanan) {
            Constants.TUNDA_TEBANG -> permohonanPembiayaanApi.updateDelayCuttingDraft(
                userId,
                buildNonSocialForestryMultiPart(body)
            )

            Constants.HASIL_HUTAN_BUKAN_KAYU -> permohonanPembiayaanApi.updateNonWoodForestProductDraft(
                userId,
                buildNonSocialForestryMultiPart(body)
            )

            Constants.KOMODITAS_NON_KEHUTANAN -> permohonanPembiayaanApi.updateNonForestryComodityDraft(
                userId,
                buildNonSocialForestryMultiPart(body)
            )

            else -> permohonanPembiayaanApi.updateDelayCuttingDraft(
                userId,
                buildNonSocialForestryMultiPart(body)
            )
        }
    }

    override fun submitApplication(memberApplicationId: String): Single<BaseDataSourceApi<DaftarPermohonanSourceApi>> {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("member_application_id", memberApplicationId)
            .build()
        return permohonanPembiayaanApi.submitApplication(multipartBody)
    }

    private fun buildSocialForestryMultiPart(body: PermohonanPembiayaanPerhutananSosialPost): MultipartBody {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.ktp.isNotEmpty()) multipartBody.addFormDataPart("ktp", body.ktp)
        if (body.name.isNotEmpty()) multipartBody.addFormDataPart("name", body.name)
        if (body.placeOfBirth.isNotEmpty()) multipartBody.addFormDataPart(
            "place_of_birth",
            body.placeOfBirth
        )
        if (body.dateOfBirth.isNotEmpty()) multipartBody.addFormDataPart(
            "date_of_birth",
            body.dateOfBirth.convertStringDateAPI()
        )
        if (body.kk.isNotEmpty()) multipartBody.addFormDataPart("kk", body.kk)
        if (body.job.isNotEmpty()) multipartBody.addFormDataPart("job", body.job)
        if (body.otherJob.isNotEmpty()) multipartBody.addFormDataPart("other_job", body.otherJob)
        if (body.coupleKtp.isNotEmpty()) multipartBody.addFormDataPart("couple_ktp", body.coupleKtp)
        if (body.coupleName.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_name",
            body.coupleName
        )
        if (body.couplePlaceOfBirth.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_place_of_birth",
            body.couplePlaceOfBirth
        )
        if (body.coupleDateOfBirth.isNotEmpty() && body.coupleDateOfBirth != "-") multipartBody.addFormDataPart(
            "couple_date_of_birth",
            body.coupleDateOfBirth.convertStringDateAPI()
        )
        if (body.ktpProvince.isNotEmpty()) multipartBody.addFormDataPart(
            "ktp_province",
            body.ktpProvince
        )
        if (body.ktpCity.isNotEmpty()) multipartBody.addFormDataPart("ktp_city", body.ktpCity)
        if (body.ktpDistrict.isNotEmpty()) multipartBody.addFormDataPart(
            "ktp_district",
            body.ktpDistrict
        )
        if (body.ktpVillage.isNotEmpty()) multipartBody.addFormDataPart(
            "ktp_village",
            body.ktpVillage
        )
        if (body.ktpRt.isNotEmpty()) multipartBody.addFormDataPart("ktp_rt", body.ktpRt)
        if (body.ktpRw.isNotEmpty()) multipartBody.addFormDataPart("ktp_rw", body.ktpRw)
        if (body.ktpAddress.isNotEmpty()) multipartBody.addFormDataPart(
            "ktp_address",
            body.ktpAddress
        )
        if (body.domicileProvince.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_province",
            body.domicileProvince
        )
        if (body.domicileCity.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_city",
            body.domicileCity
        )
        if (body.domicileDistrict.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_district",
            body.domicileDistrict
        )
        if (body.domicileVillage.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_village",
            body.domicileVillage
        )
        if (body.domicileRt.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_rt",
            body.domicileRt
        )
        if (body.domicileRw.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_rw",
            body.domicileRw
        )
        if (body.domicileAddress.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_address",
            body.domicileAddress
        )
        if (body.domicileLatitude.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_latitude",
            body.domicileLatitude
        )
        if (body.domicileLongitude.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_longitude",
            body.domicileLongitude
        )
        if (body.domicileSinceYear.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_since_year",
            body.domicileSinceYear
        )
        if (body.income != 0L) multipartBody.addFormDataPart("income", body.income.toString())
        if (body.incomeCycle != 0) multipartBody.addFormDataPart(
            "income_cycle",
            body.incomeCycle.toString()
        )
        if (body.incomeCycleUnit.isNotEmpty()) multipartBody.addFormDataPart(
            "income_cycle_unit",
            body.incomeCycleUnit
        )
        if (body.expense != 0L) multipartBody.addFormDataPart("expense", body.expense.toString())
        if (body.largestExpense != 0L) multipartBody.addFormDataPart(
            "largest_expense",
            body.largestExpense.toString()
        )
        if (body.largestUseExpense.isNotEmpty()) multipartBody.addFormDataPart(
            "largest_use_expense",
            body.largestUseExpense
        )
        if (body.businessType.isNotEmpty()) multipartBody.addFormDataPart(
            "business_type",
            body.businessType
        )
        if (body.businessCommodity.isNotEmpty()) multipartBody.addFormDataPart(
            "business_commodity",
            body.businessCommodity
        )
        if (body.businessDuration != 0) multipartBody.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (body.businessDurationUnit.isNotEmpty()) multipartBody.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit
        )
        if (body.sourceOfProduction.isNotEmpty()) multipartBody.addFormDataPart(
            "source_of_production",
            body.sourceOfProduction
        )
        if (body.businessCapacity.isNotEmpty()) multipartBody.addFormDataPart(
            "business_capacity",
            body.businessCapacity
        )
        if (body.businessEconomicValue != 0L) multipartBody.addFormDataPart(
            "business_economic_value",
            body.businessEconomicValue.toString()
        )
        if (body.marketingObjective.isNotEmpty()) multipartBody.addFormDataPart(
            "marketing_objective",
            body.marketingObjective
        )
        if (body.usagePlan.isNotEmpty()) multipartBody.addFormDataPart("usage_plan", body.usagePlan)
        if (body.estimatedTurnover != 0L) multipartBody.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0L) multipartBody.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (body.estimatedNetIncome != 0L) multipartBody.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (body.productionBusinessCycle != 0) multipartBody.addFormDataPart(
            "production_business_cycle",
            body.productionBusinessCycle.toString()
        )
        if (body.productionBusinessCycleUnit.isNotEmpty()) multipartBody.addFormDataPart(
            "production_business_cycle_unit",
            body.productionBusinessCycleUnit
        )
        if (body.amountOfLoan != 0L) multipartBody.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) multipartBody.addFormDataPart(
            "time_period",
            body.timePeriod.toString()
        )
        if (body.timePeriodUnit.isNotEmpty()) multipartBody.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit
        )
        if (body.amountOfInstallment != 0L) multipartBody.addFormDataPart(
            "amount_of_installment",
            body.amountOfInstallment.toString()
        )
        if (body.createdIn.isNotEmpty()) multipartBody.addFormDataPart("created_in", body.createdIn)
        if (body.ktpFileId.isNotEmpty()) multipartBody.addFormDataPart("ktp_file", body.ktpFileId)
        if (body.kkFileId.isNotEmpty()) multipartBody.addFormDataPart("kk_file", body.kkFileId)
        if (body.coupleKtpFileId.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_ktp_file",
            body.coupleKtpFileId
        )

        for (index in body.businessPartners.indices) {
            multipartBody.addFormDataPart(
                "business_partner[$index][name]",
                body.businessPartners[index].nama
            )
        }

        for (index in body.guarantee.indices) {
            multipartBody.addFormDataPart(
                "guarantee[$index][guarantee_form]",
                body.guarantee[index].bentuk
            )
            multipartBody.addFormDataPart(
                "guarantee[$index][guarantee_value]",
                body.guarantee[index].nilai.toString()
            )
            multipartBody.addFormDataPart(
                "guarantee[$index][guarantee_file]",
                body.guarantee[index].photoFileId
            )
        }

        return multipartBody.build()
    }

    private fun buildNonSocialForestryMultiPart(body: FormulirPembiayaanNonPerhutananSosial): MultipartBody {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        multipartBody.addFormDataPart(
            "amount_of_loan",
            body.nominal_permohonan.toString()
        )
        if (body.jangka_waktu != 0) multipartBody.addFormDataPart(
            "time_period",
            body.jangka_waktu.toString()
        )
        multipartBody.addFormDataPart(
            "time_period_unit",
            "Tahun"
        )
        if (body.skema_pembiayaan.isNotEmpty()) multipartBody.addFormDataPart(
            "financing_scheme",
            body.skema_pembiayaan
        )
        if (body.rencana_jaminan.isNotEmpty()) multipartBody.addFormDataPart(
            "warranty_plan",
            body.rencana_jaminan
        )
        if (body.jenis_usaha.isNotEmpty()) multipartBody.addFormDataPart(
            "business_type",
            body.jenis_usaha
        )
        if (body.komoditas_usaha.isNotEmpty()) multipartBody.addFormDataPart(
            "business_commodity",
            body.komoditas_usaha
        )
        if (body.lama_usaha != 0) multipartBody.addFormDataPart(
            "business_duration",
            body.lama_usaha.toString()
        )
        if (body.satuan_lama_usaha.isNotEmpty()) multipartBody.addFormDataPart(
            "business_duration_unit",
            body.satuan_lama_usaha
        )
        if (body.produktivitas.isNotEmpty()) multipartBody.addFormDataPart(
            "productivity",
            body.produktivitas
        )
        if (body.harga != 0L) multipartBody.addFormDataPart(
            "recent_sale",
            body.harga.toString()
        )
        if (body.luas_lahan != 0.0) multipartBody.addFormDataPart(
            "land_area_cultivated",
            body.luas_lahan.toString()
        )
        if (body.omzet != 0L) multipartBody.addFormDataPart(
            "estimated_turnover",
            body.omzet.toString()
        )
        if (body.hpp != 0L) multipartBody.addFormDataPart(
            "estimated_production_cost",
            body.hpp.toString()
        )
        if (body.pendapatan_bersih != 0L) multipartBody.addFormDataPart(
            "estimated_net_income",
            body.pendapatan_bersih.toString()
        )
        if (body.tujuan_pemasaran.isNotEmpty()) multipartBody.addFormDataPart(
            "marketing_objective",
            body.tujuan_pemasaran
        )
        if (body.tipe_usaha != 0) multipartBody.addFormDataPart(
            "business_management_type", when (body.tipe_usaha) {
                Constants.DIKELOLA_SENDIRI -> "Dikelola Sendiri"
                else -> "Dikelola Orang Lain"
            }
        )
        if (body.siklus_usaha != 0) multipartBody.addFormDataPart(
            "business_cycle",
            body.siklus_usaha.toString()
        )
        if (body.satuan_siklus_usaha.isNotEmpty()) multipartBody.addFormDataPart(
            "business_cycle_unit",
            body.satuan_siklus_usaha
        )
        if (body.kuantitas_komoditas.isNotEmpty()) multipartBody.addFormDataPart(
            "qty_business_commodity",
            body.kuantitas_komoditas
        )
        if (body.tujuan_pengajuan != 0) multipartBody.addFormDataPart(
            "submission_purpose", when (body.tujuan_pengajuan) {
                Constants.MODAL_KERJA -> "Untuk Modal Kerja"
                else -> "Untuk Investasi"
            }
        )
        if (body.penjelasan_tujuan.isNotEmpty()) multipartBody.addFormDataPart(
            "detail_submission_purpose",
            body.penjelasan_tujuan
        )
        if (body.dibuat_pada_tempat.isNotEmpty()) multipartBody.addFormDataPart(
            "financing_created_in",
            body.dibuat_pada_tempat
        )
        if (body.laporan_laba_rugi_id.isNotEmpty()) multipartBody.addFormDataPart(
            "profit_loss_file",
            body.laporan_laba_rugi_id
        )
        if (body.data_jaminan_id.isNotEmpty()) multipartBody.addFormDataPart(
            "collateral_file",
            body.data_jaminan_id
        )
        if (body.ijin_lahan_id.isNotEmpty()) multipartBody.addFormDataPart(
            "land_tenure_file",
            body.ijin_lahan_id
        )
        if (body.surat_tanah_id.isNotEmpty()) multipartBody.addFormDataPart(
            "land_history_file",
            body.surat_tanah_id
        )
        if (body.surat_jual_beli_id.isNotEmpty()) multipartBody.addFormDataPart(
            "transfer_declaration_file",
            body.surat_jual_beli_id
        )
        if (body.sppt_id.isNotEmpty()) multipartBody.addFormDataPart(
            "sppt_file",
            body.sppt_id
        )
        if (body.foto_lahan_id.isNotEmpty()) multipartBody.addFormDataPart(
            "land_photo_file",
            body.foto_lahan_id
        )

        for (index in body.businessPartners.indices) {
            multipartBody.addFormDataPart(
                "business_partner[$index][name]",
                body.businessPartners[index].nama
            )
        }

        return multipartBody.build()
    }

    override fun uploadFile(file: File): Single<BaseDataSourceApi<FileServiceSourceApi>> {
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.path,
            file.asRequestBody("application/octet-stream".toMediaType())
        )

        return fileServiceApi.uploadFile(multipartBody)
    }

    override fun getSingleFile(id: String): Single<BaseDataSourceApi<GetFileSourceApi>> {
        return fileServiceApi.fetchFile(id)
    }

    override fun getFormulirPembiayaanNonPerhutananSosial(status: Int): Maybe<FormulirPembiayaanNonPerhutananSosial> {
        return formulirPembiayaanNonPerhutananSosialDao.getData(status)
    }

    override fun insertFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial) {
        appExecutors.diskIO().execute {
            formulirPembiayaanNonPerhutananSosialDao.insert(formulirPembiayaanNonPerhutananSosial)
        }
    }

    override fun updateFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial) {
        appExecutors.diskIO().execute {
            formulirPembiayaanNonPerhutananSosialDao.update(formulirPembiayaanNonPerhutananSosial)
        }
    }

    override fun getMitra(formulirId: Int): Flowable<List<Mitra>> {
        return mitraDao.getMitra(formulirId)
    }

    override fun insertMitra(mitra: List<Mitra>) {
        appExecutors.diskIO().execute {
            mitraDao.deleteAll(mitra.first().formulir_id)
            mitraDao.insert(mitra)
        }
    }

    override fun updateMitra(mitra: Mitra) {
        appExecutors.diskIO().execute {
            mitraDao.update(mitra)
        }
    }

    override fun createOtherDocumentDraft(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>> {

        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", body.userId.orEmpty())
            .addFormDataPart("name", body.name.orEmpty())
            .addFormDataPart("description", body.description.orEmpty())
            .addFormDataPart("file", body.file ?: "")
            .build()

        return permohonanPembiayaanApi.createOtherDocumentDraft(multipartBody)
    }

    override fun getFile(parentId: Int, type: Int): Flowable<List<FileEntity>> {
        return fileDao.getFile(parentId, type)
    }

    override fun insertFile(fileEntity: List<FileEntity>) {
        appExecutors.diskIO().execute {
            fileDao.deleteAll(fileEntity.first().parent_id, fileEntity.first().type)
            fileDao.insert(fileEntity)
        }
    }

    override fun updateFile(fileEntity: FileEntity) {
        appExecutors.diskIO().execute {
            fileDao.update(fileEntity)
        }
    }

    override fun getJaminan(formulirId: Int): Flowable<List<JaminanEntity>> {
        return jaminanDao.getJaminan(formulirId)
    }

    override fun insertJaminan(jaminanEntity: List<JaminanEntity>) {
        appExecutors.diskIO().execute {
            jaminanDao.deleteAll(jaminanEntity.first().id)
            jaminanDao.insert(jaminanEntity)
        }
    }

    override fun updateJaminan(jaminanEntity: JaminanEntity) {
        appExecutors.diskIO().execute {
            jaminanDao.update(jaminanEntity)
        }
    }
}