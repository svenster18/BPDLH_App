package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.common.utils.AppExecutors
import id.bpdlh.fdb.core.common.utils.convertStringDateAPI
import id.bpdlh.fdb.core.data.common.RegistrasiPeroranganRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.data.sources.local.room.RegistrasiPeroranganDao
import id.bpdlh.fdb.core.data.sources.remote.GroupApplicationApi
import id.bpdlh.fdb.core.data.sources.remote.PermohonanPembiayaanApi
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MultipartBody
import timber.log.Timber

class RegistrasiPeroranganRepository(
    private val registrasiPeroranganDao: RegistrasiPeroranganDao,
    private val appExecutors: AppExecutors,
    private val groupApplicationApi: GroupApplicationApi,
    private val permohonanPembiayaanApi: PermohonanPembiayaanApi
) : RegistrasiPeroranganRepositoryContract {

    override fun getDraft(): Maybe<RegistrasiPerorangan> = registrasiPeroranganDao.getDraft()

    override fun insert(registrasiPerorangan: RegistrasiPerorangan) {
        appExecutors.diskIO().execute { registrasiPeroranganDao.insert(registrasiPerorangan) }
    }

    override fun update(registrasiPerorangan: RegistrasiPerorangan) {
        appExecutors.diskIO().execute { registrasiPeroranganDao.update(registrasiPerorangan) }
    }

    override fun getMemberApplication(userId: String): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        return groupApplicationApi.getMemberApplicationData(userId)
    }

    override fun updateNonSocialForestry(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>> {
        return permohonanPembiayaanApi.updateNonSocialForestry(
            userId,
            buildNonSocialForestryMultiPart(body)
        )
    }

    override fun updateNonSocialForestryDraft(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>> {
        return permohonanPembiayaanApi.updateNonSocialForestryDraft(
            userId,
            buildNonSocialForestryMultiPart(body)
        )
    }

    private fun buildNonSocialForestryMultiPart(body: RegistrasiPerorangan): MultipartBody {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.nik.isNotEmpty()) multipartBody.addFormDataPart("ktp", body.nik)
        if (body.nama.isNotEmpty()) multipartBody.addFormDataPart("name", body.nama)
        if (body.tempatLahir.isNotEmpty()) multipartBody.addFormDataPart(
            "place_of_birth",
            body.tempatLahir
        )
        if (body.tanggalLahir.isNotEmpty()) multipartBody.addFormDataPart(
            "date_of_birth",
            body.tanggalLahir.convertStringDateAPI()
        )
        multipartBody.addFormDataPart(
            "gender",
            body.jenisKelamin
        )
        if (body.noKk.isNotEmpty()) multipartBody.addFormDataPart("kk", body.noKk)
        if (body.pekerjaanUtama.isNotEmpty()) multipartBody.addFormDataPart(
            "job",
            body.pekerjaanUtama
        )
        if (body.nikPasangan.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_ktp",
            body.nikPasangan
        )
        if (body.namaPasangan.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_name",
            body.namaPasangan
        )
        if (body.tempatLahirPasangan.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_place_of_birth",
            body.tempatLahirPasangan
        )
        if (body.tanggalLahirPasangan.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_date_of_birth",
            body.tanggalLahirPasangan.convertStringDateAPI()
        )
        if (body.provinsi.isNotEmpty()) multipartBody.addFormDataPart("ktp_province", body.provinsi)
        if (body.kota.isNotEmpty()) multipartBody.addFormDataPart("ktp_city", body.kota)
        if (body.kecamatan.isNotEmpty()) multipartBody.addFormDataPart(
            "ktp_district",
            body.kecamatan
        )
        if (body.kelurahan.isNotEmpty()) multipartBody.addFormDataPart(
            "ktp_village",
            body.kelurahan
        )
        if (body.rt.isNotEmpty()) multipartBody.addFormDataPart("ktp_rt", body.rt)
        if (body.rw.isNotEmpty()) multipartBody.addFormDataPart("ktp_rw", body.rw)
        if (body.alamat.isNotEmpty()) multipartBody.addFormDataPart("ktp_address", body.alamat)
        if (body.provinsiDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_province",
            body.provinsiDomisili
        )
        if (body.kotaDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_city",
            body.kotaDomisili
        )
        if (body.kecamatanDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_district",
            body.kecamatanDomisili
        )
        if (body.kelurahanDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_village",
            body.kelurahanDomisili
        )
        if (body.rtDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_rt",
            body.rtDomisili
        )
        if (body.rwDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_rw",
            body.rwDomisili
        )
        if (body.alamatDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_address",
            body.alamatDomisili
        )
        if (body.latitude != 0.0) multipartBody.addFormDataPart(
            "domicile_latitude",
            body.latitude.toString()
        )
        if (body.longitude != 0.0) multipartBody.addFormDataPart(
            "domicile_longitude",
            body.longitude.toString()
        )
        if (body.tahunDomisili.isNotEmpty()) multipartBody.addFormDataPart(
            "domicile_since_year",
            body.tahunDomisili
        )
        multipartBody.addFormDataPart("other_business_type", "Tipe bisniss lainnya apa")
        multipartBody.addFormDataPart("other_business_cycle", "1")
        multipartBody.addFormDataPart(
            "other_business_cycle_unit",
            "Tahun"
        )
        multipartBody.addFormDataPart("other_business_income", "1000000")
        if (body.pengeluaranRutin != 0L) multipartBody.addFormDataPart(
            "expense",
            body.pengeluaranRutin.toString()
        )
        if (body.pengeluaranTerbesar != 0L) multipartBody.addFormDataPart(
            "largest_expense",
            body.pengeluaranTerbesar.toString()
        )
        multipartBody.addFormDataPart("created_in", "Jakarta")
        if (body.ktpFile.isNotEmpty()) multipartBody.addFormDataPart("ktp_file", body.ktpFile)
        if (body.kkFile.isNotEmpty()) multipartBody.addFormDataPart("kk_file", body.kkFile)
        if (body.coupleKtpFile.isNotEmpty()) multipartBody.addFormDataPart(
            "couple_ktp_file",
            body.coupleKtpFile
        )
//            if (body.incomeFile.isNotEmpty()) multipartBody.addFormDataPart("income_file","b5ab9308-debd-4cd2-92ee-40cb4e8b542f")
        if (body.swaphotoKtpFile.isNotEmpty()) multipartBody.addFormDataPart(
            "swaphoto_ktp_file",
            body.swaphotoKtpFile
        )
        if (body.housePhotoFile.isNotEmpty()) multipartBody.addFormDataPart(
            "house_photo_file",
            body.housePhotoFile
        )
        if (body.businessPhotoFile.isNotEmpty()) multipartBody.addFormDataPart(
            "business_photo_file",
            body.businessPhotoFile
        )
        multipartBody.addFormDataPart("swaphoto_ktp_description", "Deskripsinya apa")
        multipartBody.addFormDataPart("house_photo_description", "Deskripsinya apa")
        multipartBody.addFormDataPart("business_photo_description", "Deskripsinya apa")

        Timber.d(body.otherBusiness.toString())
        body.otherBusiness.forEachIndexed { index, otherBusiness ->
            multipartBody.addFormDataPart(
                "other_business[$index][other_business_type]",
                otherBusiness.jenis
            )
            multipartBody.addFormDataPart(
                "other_business[$index][other_business_income]",
                otherBusiness.perkiraanPendapatan.toString()
            )
            multipartBody.addFormDataPart(
                "other_business[$index][other_business_cycle]",
                otherBusiness.siklusPendapatan.toString()
            )
            multipartBody.addFormDataPart(
                "other_business[$index][other_business_cycle_unit]",
                otherBusiness.satuanSiklusPendapatan
            )
        }
        return multipartBody.build()
    }

    override fun delayCuttingDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit!!
        )
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart(
            "financing_scheme",
            body.financingScheme!!
        )
        if (!body.warrantyplan.isNullOrEmpty()) data.addFormDataPart(
            "warranty_plan",
            body.warrantyplan!!
        )
        if (!body.businessType.isNullOrEmpty()) data.addFormDataPart(
            "business_type",
            body.businessType!!
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "business_commodity",
            body.businessCommodity!!
        )
        if (body.businessDuration != 0) data.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit!!
        )
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart(
            "productivity",
            body.productivity!!
        )
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (body.landAreaCultivated != 0) data.addFormDataPart(
            "land_area_cultivated",
            body.landAreaCultivated.toString()
        )
        if (body.estimatedTurnover != 0) data.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0) data.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (body.estimatedNetIncome != 0) data.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart(
            "marketing_objective",
            body.marketingObjective!!
        )
        if (!body.businessManagementType.isNullOrEmpty()) data.addFormDataPart(
            "business_management_type",
            body.businessManagementType!!
        )
        if (body.businessCycle != 0) data.addFormDataPart(
            "business_cycle",
            body.businessCycle.toString()
        )
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_cycle_unit",
            body.businessCycleUnit!!
        )
        if (body.qtyBusinessCommodity != 0) data.addFormDataPart(
            "qty_business_commodity",
            body.qtyBusinessCommodity.toString()
        )
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "submission_purpose",
            body.submissionPurpose!!
        )
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "detail_submission_purpose",
            body.detailSubmissionPurpose!!
        )
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart(
            "financing_created_in",
            body.financingCreatedIn!!
        )
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart(
            "profit_loss_file",
            body.profitLossFile!!
        )
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart(
            "collateral_file",
            body.collateralFile!!
        )
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart(
            "land_tenure_file",
            body.landTenureFile!!
        )
        if (!body.landHistoryFile.isNullOrEmpty()) data.addFormDataPart(
            "land_history_file",
            body.landHistoryFile!!
        )
        if (!body.transferDeclarationFile.isNullOrEmpty()) data.addFormDataPart(
            "transfer_declaration_file",
            body.transferDeclarationFile!!
        )
        if (!body.spptFile.isNullOrEmpty()) data.addFormDataPart("sppt_file", body.spptFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart(
            "land_photo_file",
            body.landPhotoFile!!
        )
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return groupApplicationApi.delayCuttingDraft(userId, requestBody)
    }

    override fun delayCuttingUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit!!
        )
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart(
            "financing_scheme",
            body.financingScheme!!
        )
        if (!body.warrantyplan.isNullOrEmpty()) data.addFormDataPart(
            "warranty_plan",
            body.warrantyplan!!
        )
        if (!body.businessType.isNullOrEmpty()) data.addFormDataPart(
            "business_type",
            body.businessType!!
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "business_commodity",
            body.businessCommodity!!
        )
        if (body.businessDuration != 0) data.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit!!
        )
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart(
            "productivity",
            body.productivity!!
        )
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (body.landAreaCultivated != 0) data.addFormDataPart(
            "land_area_cultivated",
            body.landAreaCultivated.toString()
        )
        if (body.estimatedTurnover != 0) data.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0) data.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (body.estimatedNetIncome != 0) data.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart(
            "marketing_objective",
            body.marketingObjective!!
        )
        if (!body.businessManagementType.isNullOrEmpty()) data.addFormDataPart(
            "business_management_type",
            body.businessManagementType!!
        )
        if (body.businessCycle != 0) data.addFormDataPart(
            "business_cycle",
            body.businessCycle.toString()
        )
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_cycle_unit",
            body.businessCycleUnit!!
        )
        if (body.qtyBusinessCommodity != 0) data.addFormDataPart(
            "qty_business_commodity",
            body.qtyBusinessCommodity.toString()
        )
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "submission_purpose",
            body.submissionPurpose!!
        )
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "detail_submission_purpose",
            body.detailSubmissionPurpose!!
        )
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart(
            "financing_created_in",
            body.financingCreatedIn!!
        )
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart(
            "profit_loss_file",
            body.profitLossFile!!
        )
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart(
            "collateral_file",
            body.collateralFile!!
        )
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart(
            "land_tenure_file",
            body.landTenureFile!!
        )
        if (!body.landHistoryFile.isNullOrEmpty()) data.addFormDataPart(
            "land_history_file",
            body.landHistoryFile!!
        )
        if (!body.transferDeclarationFile.isNullOrEmpty()) data.addFormDataPart(
            "transfer_declaration_file",
            body.transferDeclarationFile!!
        )
        if (!body.spptFile.isNullOrEmpty()) data.addFormDataPart("sppt_file", body.spptFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart(
            "land_photo_file",
            body.landPhotoFile!!
        )
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return groupApplicationApi.delayCuttingUpdate(userId, requestBody)
    }

    override fun nonForestyComodityDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit!!
        )
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart(
            "financing_scheme",
            body.financingScheme!!
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "business_commodity",
            body.businessCommodity!!
        )
        if (body.businessDuration != 0) data.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit!!
        )
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart(
            "productivity",
            body.productivity!!
        )
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart(
            "marketing_objective",
            body.marketingObjective!!
        )
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "submission_purpose",
            body.submissionPurpose!!
        )
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "detail_submission_purpose",
            body.detailSubmissionPurpose!!
        )
        if (body.estimatedTurnover != 0) data.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0) data.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (body.estimatedNetIncome != 0) data.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (body.businessCycle != 0) data.addFormDataPart(
            "business_cycle",
            body.businessCycle.toString()
        )
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_cycle_unit",
            body.businessCycleUnit!!
        )
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart(
            "financing_created_in",
            body.financingCreatedIn!!
        )
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart(
            "profit_loss_file",
            body.profitLossFile!!
        )
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart(
            "collateral_file",
            body.collateralFile!!
        )
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart(
            "land_tenure_file",
            body.landTenureFile!!
        )
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart(
            "other_supporting_file",
            body.otherSupportingFile!!
        )
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart(
            "land_photo_file",
            body.landPhotoFile!!
        )
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return groupApplicationApi.nonForestyComodityDraft(userId, requestBody)
    }

    override fun nonForestyComodityUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit!!
        )
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart(
            "financing_scheme",
            body.financingScheme!!
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "business_commodity",
            body.businessCommodity!!
        )
        if (body.businessDuration != 0) data.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit!!
        )
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart(
            "productivity",
            body.productivity!!
        )
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart(
            "marketing_objective",
            body.marketingObjective!!
        )
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "submission_purpose",
            body.submissionPurpose!!
        )
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "detail_submission_purpose",
            body.detailSubmissionPurpose!!
        )
        if (body.estimatedTurnover != 0) data.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0) data.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (body.estimatedNetIncome != 0) data.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (body.businessCycle != 0) data.addFormDataPart(
            "business_cycle",
            body.businessCycle.toString()
        )
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_cycle_unit",
            body.businessCycleUnit!!
        )
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart(
            "financing_created_in",
            body.financingCreatedIn!!
        )
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart(
            "profit_loss_file",
            body.profitLossFile!!
        )
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart(
            "collateral_file",
            body.collateralFile!!
        )
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart(
            "land_tenure_file",
            body.landTenureFile!!
        )
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart(
            "other_supporting_file",
            body.otherSupportingFile!!
        )
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart(
            "land_photo_file",
            body.landPhotoFile!!
        )
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return groupApplicationApi.nonForestyComodityUpdate(userId, requestBody)
    }

    override fun nonWoodForestProductDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit!!
        )
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart(
            "financing_scheme",
            body.financingScheme!!
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "business_commodity",
            body.businessCommodity!!
        )
        if (!body.managementActivityType.isNullOrEmpty()) data.addFormDataPart(
            "management_activity_type",
            body.managementActivityType!!
        )
        if (body.businessDuration != 0) data.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit!!
        )
        if (!body.sourceOfProduction.isNullOrEmpty()) data.addFormDataPart(
            "source_of_production",
            body.sourceOfProduction!!
        )
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart(
            "marketing_objective",
            body.marketingObjective!!
        )
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "submission_purpose",
            body.submissionPurpose!!
        )
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "detail_submission_purpose",
            body.detailSubmissionPurpose!!
        )
        if (body.estimatedTurnover != 0) data.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0) data.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (body.estimatedNetIncome != 0) data.addFormDataPart(
            "business_cycle",
            body.businessCycle.toString()
        )
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_cycle_unit",
            body.businessCycleUnit!!
        )
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart(
            "financing_created_in",
            body.financingCreatedIn!!
        )
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart(
            "profit_loss_file",
            body.profitLossFile!!
        )
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart(
            "collateral_file",
            body.collateralFile!!
        )
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart(
            "land_tenure_file",
            body.landTenureFile!!
        )
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart(
            "other_supporting_file",
            body.otherSupportingFile!!
        )
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart(
            "land_photo_file",
            body.landPhotoFile!!
        )
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return groupApplicationApi.nonWoodForestProductDraft(userId, requestBody)
    }

    override fun nonWoodForestProductUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart(
            "amount_of_loan",
            body.amountOfLoan.toString()
        )
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart(
            "time_period_unit",
            body.timePeriodUnit!!
        )
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart(
            "financing_scheme",
            body.financingScheme!!
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "business_commodity",
            body.businessCommodity!!
        )
        if (!body.managementActivityType.isNullOrEmpty()) data.addFormDataPart(
            "management_activity_type",
            body.managementActivityType!!
        )
        if (body.businessDuration != 0) data.addFormDataPart(
            "business_duration",
            body.businessDuration.toString()
        )
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_duration_unit",
            body.businessDurationUnit!!
        )
        if (!body.sourceOfProduction.isNullOrEmpty()) data.addFormDataPart(
            "source_of_production",
            body.sourceOfProduction!!
        )
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart(
            "marketing_objective",
            body.marketingObjective!!
        )
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "submission_purpose",
            body.submissionPurpose!!
        )
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart(
            "detail_submission_purpose",
            body.detailSubmissionPurpose!!
        )
        if (body.estimatedTurnover != 0) data.addFormDataPart(
            "estimated_turnover",
            body.estimatedTurnover.toString()
        )
        if (body.estimatedProductionCost != 0) data.addFormDataPart(
            "estimated_production_cost",
            body.estimatedProductionCost.toString()
        )
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart(
            "estimated_net_income",
            body.estimatedNetIncome.toString()
        )
        if (body.estimatedNetIncome != 0) data.addFormDataPart(
            "business_cycle",
            body.businessCycle.toString()
        )
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart(
            "business_cycle_unit",
            body.businessCycleUnit!!
        )
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart(
            "financing_created_in",
            body.financingCreatedIn!!
        )
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart(
            "profit_loss_file",
            body.profitLossFile!!
        )
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart(
            "collateral_file",
            body.collateralFile!!
        )
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart(
            "land_tenure_file",
            body.landTenureFile!!
        )
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart(
            "other_supporting_file",
            body.otherSupportingFile!!
        )
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart(
            "land_photo_file",
            body.landPhotoFile!!
        )
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return groupApplicationApi.nonWoodForestProductUpdate(userId, requestBody)
    }
}