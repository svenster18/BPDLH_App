package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.common.utils.AppExecutors
import id.bpdlh.fdb.core.data.common.FormPermohonanNonSosialRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.BusinessPartnerSourceApi
import id.bpdlh.fdb.core.data.entities.FileServiceSourceApi
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.room.FileDao
import id.bpdlh.fdb.core.data.sources.local.room.JaminanDao
import id.bpdlh.fdb.core.data.sources.local.room.MitraDao
import id.bpdlh.fdb.core.data.sources.remote.FileServiceApi
import id.bpdlh.fdb.core.data.sources.remote.MemberApplicationApi
import id.bpdlh.fdb.core.data.sources.remote.UserApi
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class FormPermohonanNonSosialRepository(
    private val memberApplicationApi: MemberApplicationApi,
    private val mitraDao: MitraDao,
    private val fileDao: FileDao,
    private val jaminanDao: JaminanDao,
    private val appExecutors: AppExecutors,
    private val fileServiceApi: FileServiceApi
) : FormPermohonanNonSosialRepositoryContract {
    override fun getMemberApplication(userId: String): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        return memberApplicationApi.getMemberApplicationData(userId)
    }

    override fun delayCuttingDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        body.amountOfLoan?.let { data.addFormDataPart("amount_of_loan", it.toString()) }
        body.timePeriod?.let { data.addFormDataPart("time_period", it.toString()) }
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.financingScheme!!)
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("financing_scheme", body.financingScheme!!)
        if (!body.warrantyplan.isNullOrEmpty()) data.addFormDataPart("warranty_plan", body.warrantyplan!!)
        if (!body.businessType.isNullOrEmpty()) data.addFormDataPart("business_type", body.businessType!!)
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("business_commodity", body.businessCommodity!!)
        body.businessDuration?.let { data.addFormDataPart("business_duration", it.toString()) }
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart("business_duration_unit", body.businessDurationUnit!!)
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart("productivity", body.productivity!!)
        body.recentSale?.let { data.addFormDataPart("recent_sale", it.toString()) }
        body.landAreaCultivated?.let {  data.addFormDataPart("land_area_cultivated", it.toString()) }
        body.estimatedTurnover?.let { data.addFormDataPart("estimated_turnover", it.toString()) }
        body.estimatedProductionCost?.let { data.addFormDataPart("estimated_production_cost", it.toString()) }
        body.estimatedNetIncome?.let { data.addFormDataPart("estimated_net_income", it.toString()) }
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart("marketing_objective", body.marketingObjective!!)
        if (!body.businessManagementType.isNullOrEmpty()) data.addFormDataPart("business_management_type", body.businessManagementType!!)
        body.businessCycle?.let { data.addFormDataPart("business_cycle", it.toString()) }
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart("business_cycle_unit", body.businessCycleUnit!!)
        body.qtyBusinessCommodity?.let { data.addFormDataPart("qty_business_commodity", it.toString()) }
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart("submission_purpose", body.submissionPurpose!!)
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart("detail_submission_purpose", body.detailSubmissionPurpose!!)
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart("financing_created_in", body.financingCreatedIn!!)
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart("profit_loss_file", body.profitLossFile!!)
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart("collateral_file", body.collateralFile!!)
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart("land_tenure_file", body.landTenureFile!!)
        if (!body.landHistoryFile.isNullOrEmpty()) data.addFormDataPart("land_history_file", body.landHistoryFile!!)
        if (!body.transferDeclarationFile.isNullOrEmpty()) data.addFormDataPart("transfer_declaration_file", body.transferDeclarationFile!!)
        if (!body.spptFile.isNullOrEmpty()) data.addFormDataPart("sppt_file", body.spptFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart("land_photo_file", body.landPhotoFile!!)
        body.businessPartner?.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index][name]", value)
        }
        val requestBody = data.build()
        return memberApplicationApi.delayCuttingDraft(userId, requestBody)
    }

    override fun delayCuttingUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        body.amountOfLoan?.let { data.addFormDataPart("amount_of_loan", it.toString()) }
        body.timePeriod?.let { data.addFormDataPart("time_period", it.toString()) }
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.financingScheme!!)
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("financing_scheme", body.financingScheme!!)
        if (!body.warrantyplan.isNullOrEmpty()) data.addFormDataPart("warranty_plan", body.warrantyplan!!)
        if (!body.businessType.isNullOrEmpty()) data.addFormDataPart("business_type", body.businessType!!)
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("business_commodity", body.businessCommodity!!)
        if (body.businessDuration != 0) data.addFormDataPart("business_duration", body.businessDuration.toString())
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart("business_duration_unit", body.businessDurationUnit!!)
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart("productivity", body.productivity!!)
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (body.landAreaCultivated != 0) data.addFormDataPart("land_area_cultivated", body.landAreaCultivated.toString())
        if (body.estimatedTurnover != 0) data.addFormDataPart("estimated_turnover", body.estimatedTurnover.toString())
        if (body.estimatedProductionCost != 0) data.addFormDataPart("estimated_production_cost", body.estimatedProductionCost.toString())
        if (body.estimatedNetIncome != 0) data.addFormDataPart("estimated_net_income", body.estimatedNetIncome.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart("marketing_objective", body.marketingObjective!!)
        if (!body.businessManagementType.isNullOrEmpty()) data.addFormDataPart("business_management_type", body.businessManagementType!!)
        if (body.businessCycle != 0) data.addFormDataPart("business_cycle", body.businessCycle.toString())
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart("business_cycle_unit", body.businessCycleUnit!!)
        if (body.qtyBusinessCommodity != 0) data.addFormDataPart("qty_business_commodity", body.qtyBusinessCommodity.toString())
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart("submission_purpose", body.submissionPurpose!!)
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart("detail_submission_purpose", body.detailSubmissionPurpose!!)
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart("financing_created_in", body.financingCreatedIn!!)
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart("profit_loss_file", body.profitLossFile!!)
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart("collateral_file", body.collateralFile!!)
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart("land_tenure_file", body.landTenureFile!!)
        if (!body.landHistoryFile.isNullOrEmpty()) data.addFormDataPart("land_history_file", body.landHistoryFile!!)
        if (!body.transferDeclarationFile.isNullOrEmpty()) data.addFormDataPart("transfer_declaration_file", body.transferDeclarationFile!!)
        if (!body.spptFile.isNullOrEmpty()) data.addFormDataPart("sppt_file", body.spptFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart("land_photo_file", body.landPhotoFile!!)
        body.businessPartner?.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index][name]", value)
        }
        val requestBody = data.build()
        return memberApplicationApi.delayCuttingUpdate(userId, requestBody)
    }

    override fun nonForestyComodityDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart("amount_of_loan", body.amountOfLoan.toString())
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.timePeriodUnit!!)
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("financing_scheme", body.financingScheme!!)
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("business_commodity", body.businessCommodity!!)
        if (body.businessDuration != 0) data.addFormDataPart("business_duration", body.businessDuration.toString())
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart("business_duration_unit", body.businessDurationUnit!!)
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart("productivity", body.productivity!!)
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart("marketing_objective", body.marketingObjective!!)
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart("submission_purpose", body.submissionPurpose!!)
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart("detail_submission_purpose", body.detailSubmissionPurpose!!)
        if (body.estimatedTurnover != 0) data.addFormDataPart("estimated_turnover", body.estimatedTurnover.toString())
        if (body.estimatedProductionCost != 0) data.addFormDataPart("estimated_production_cost", body.estimatedProductionCost.toString())
        if (body.estimatedNetIncome != 0) data.addFormDataPart("estimated_net_income", body.estimatedNetIncome.toString())
        if (body.businessCycle != 0) data.addFormDataPart("business_cycle", body.businessCycle.toString())
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart("business_cycle_unit", body.businessCycleUnit!!)
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart("financing_created_in", body.financingCreatedIn!!)
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart("profit_loss_file", body.profitLossFile!!)
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart("collateral_file", body.collateralFile!!)
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart("land_tenure_file", body.landTenureFile!!)
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart("other_supporting_file", body.otherSupportingFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart("land_photo_file", body.landPhotoFile!!)
        body.businessPartner?.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index][name]", value)
        }
        val requestBody = data.build()
        return memberApplicationApi.nonForestyComodityDraft(userId, requestBody)
    }

    override fun nonForestyComodityUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart("amount_of_loan", body.amountOfLoan.toString())
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.timePeriodUnit!!)
        if (!body.businessType.isNullOrEmpty()) data.addFormDataPart("business_type", body.businessType!!)
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("financing_scheme", body.financingScheme!!)
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("business_commodity", body.businessCommodity!!)
        if (body.businessDuration != 0) data.addFormDataPart("business_duration", body.businessDuration.toString())
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart("business_duration_unit", body.businessDurationUnit!!)
        if (!body.productivity.isNullOrEmpty()) data.addFormDataPart("productivity", body.productivity!!)
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart("marketing_objective", body.marketingObjective!!)
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart("submission_purpose", body.submissionPurpose!!)
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart("detail_submission_purpose", body.detailSubmissionPurpose!!)
        if (body.estimatedTurnover != 0) data.addFormDataPart("estimated_turnover", body.estimatedTurnover.toString())
        if (body.estimatedProductionCost != 0) data.addFormDataPart("estimated_production_cost", body.estimatedProductionCost.toString())
        if (body.estimatedNetIncome != 0) data.addFormDataPart("estimated_net_income", body.estimatedNetIncome.toString())
        if (body.businessCycle != 0) data.addFormDataPart("business_cycle", body.businessCycle.toString())
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart("business_cycle_unit", body.businessCycleUnit!!)
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart("financing_created_in", body.financingCreatedIn!!)
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart("profit_loss_file", body.profitLossFile!!)
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart("collateral_file", body.collateralFile!!)
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart("land_tenure_file", body.landTenureFile!!)
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart("other_supporting_file", body.otherSupportingFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart("land_photo_file", body.landPhotoFile!!)
        body.businessPartner?.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index][name]", value)
        }
        val requestBody = data.build()
        return memberApplicationApi.nonForestyComodityUpdate(userId, requestBody)
    }

    override fun nonWoodForestProductDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart("amount_of_loan", body.amountOfLoan.toString())
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.timePeriodUnit!!)
        if (!body.businessType.isNullOrEmpty()) data.addFormDataPart("business_type", body.businessType!!)
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("financing_scheme", body.financingScheme!!)
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("business_commodity", body.businessCommodity!!)
        if (!body.managementActivityType.isNullOrEmpty()) data.addFormDataPart("management_activity_type", body.managementActivityType!!)
        if (body.businessDuration != 0) data.addFormDataPart("business_duration", body.businessDuration.toString())
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart("business_duration_unit", body.businessDurationUnit!!)
        if (!body.sourceOfProduction.isNullOrEmpty()) data.addFormDataPart("source_of_production", body.sourceOfProduction!!)
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart("marketing_objective", body.marketingObjective!!)
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart("submission_purpose", body.submissionPurpose!!)
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart("detail_submission_purpose", body.detailSubmissionPurpose!!)
        if (body.estimatedTurnover != 0) data.addFormDataPart("estimated_turnover", body.estimatedTurnover.toString())
        if (body.estimatedProductionCost != 0) data.addFormDataPart("estimated_production_cost", body.estimatedProductionCost.toString())
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("estimated_net_income", body.estimatedNetIncome.toString())
        if (body.estimatedNetIncome != 0) data.addFormDataPart("business_cycle", body.businessCycle.toString())
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart("business_cycle_unit", body.businessCycleUnit!!)
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart("financing_created_in", body.financingCreatedIn!!)
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart("profit_loss_file", body.profitLossFile!!)
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart("collateral_file", body.collateralFile!!)
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart("land_tenure_file", body.landTenureFile!!)
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart("other_supporting_file", body.otherSupportingFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart("land_photo_file", body.landPhotoFile!!)
        body.businessPartner?.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index][name]", value)
        }
        val requestBody = data.build()
        return memberApplicationApi.nonWoodForestProductDraft(userId, requestBody)
    }

    override fun nonWoodForestProductUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart("amount_of_loan", body.amountOfLoan.toString())
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.timePeriodUnit!!)
        if (!body.financingScheme.isNullOrEmpty()) data.addFormDataPart("financing_scheme", body.financingScheme!!)
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("business_commodity", body.businessCommodity!!)
        if (!body.managementActivityType.isNullOrEmpty()) data.addFormDataPart("management_activity_type", body.managementActivityType!!)
        if (body.businessDuration != 0) data.addFormDataPart("business_duration", body.businessDuration.toString())
        if (!body.businessDurationUnit.isNullOrEmpty()) data.addFormDataPart("business_duration_unit", body.businessDurationUnit!!)
        if (!body.sourceOfProduction.isNullOrEmpty()) data.addFormDataPart("source_of_production", body.sourceOfProduction!!)
        body.businessCapacity?.let { data.addFormDataPart("business_capacity", it.toString()) }
        if (body.recentSale != 0) data.addFormDataPart("recent_sale", body.recentSale.toString())
        if (!body.marketingObjective.isNullOrEmpty()) data.addFormDataPart("marketing_objective", body.marketingObjective!!)
        if (!body.submissionPurpose.isNullOrEmpty()) data.addFormDataPart("submission_purpose", body.submissionPurpose!!)
        if (!body.detailSubmissionPurpose.isNullOrEmpty()) data.addFormDataPart("detail_submission_purpose", body.detailSubmissionPurpose!!)
        if (body.estimatedTurnover != 0) data.addFormDataPart("estimated_turnover", body.estimatedTurnover.toString())
        if (body.estimatedProductionCost != 0) data.addFormDataPart("estimated_production_cost", body.estimatedProductionCost.toString())
        if (!body.businessCommodity.isNullOrEmpty()) data.addFormDataPart("estimated_net_income", body.estimatedNetIncome.toString())
        if (body.estimatedNetIncome != 0) data.addFormDataPart("business_cycle", body.businessCycle.toString())
        if (!body.businessCycleUnit.isNullOrEmpty()) data.addFormDataPart("business_cycle_unit", body.businessCycleUnit!!)
        if (!body.financingCreatedIn.isNullOrEmpty()) data.addFormDataPart("financing_created_in", body.financingCreatedIn!!)
        if (!body.profitLossFile.isNullOrEmpty()) data.addFormDataPart("profit_loss_file", body.profitLossFile!!)
        if (!body.collateralFile.isNullOrEmpty()) data.addFormDataPart("collateral_file", body.collateralFile!!)
        if (!body.landTenureFile.isNullOrEmpty()) data.addFormDataPart("land_tenure_file", body.landTenureFile!!)
        if (!body.otherSupportingFile.isNullOrEmpty()) data.addFormDataPart("other_supporting_file", body.otherSupportingFile!!)
        if (!body.landPhotoFile.isNullOrEmpty()) data.addFormDataPart("land_photo_file", body.landPhotoFile!!)
        body.businessPartner?.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index][name]", value)
        }
        val requestBody = data.build()
        return memberApplicationApi.nonWoodForestProductUpdate(userId, requestBody)
    }

    override fun postOtherDocumentDraft(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>> {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", body.userId.orEmpty())
            .addFormDataPart("name", body.name.orEmpty())
            .addFormDataPart("description", body.description.orEmpty())
            .addFormDataPart("file", body.file ?: "")
            .build()

        return memberApplicationApi.postOtherDocument(multipartBody)
    }

    override fun getOtherDocumentDraft(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>> {
        return memberApplicationApi.getOtherDocumentDraft(userId)
    }

    override fun getOtherDocument(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>> {
        return memberApplicationApi.getOtherDocument(userId)
    }

    override fun deleteOtherDocument(id: String): Single<BaseDataSourceApi<Any>> {
        return memberApplicationApi.deleteOtherDocument(id)
    }

    override fun getBusinessPartner(userId: String): Single<BaseDataSourceApi<List<BusinessPartnerSourceApi>>> {
        return memberApplicationApi.getBusinessPartnerDraft(userId)
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
}