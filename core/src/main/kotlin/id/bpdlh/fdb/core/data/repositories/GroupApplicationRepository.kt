package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.common.GroupApplicationRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.remote.GroupApplicationApi
import id.bpdlh.fdb.core.domain.entities.DataDebiturNonSosialEntity
import id.bpdlh.fdb.core.domain.entities.GroupApplicationEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDetailEntity
import io.reactivex.Single
import okhttp3.MultipartBody

class GroupApplicationRepository(
    private val api: GroupApplicationApi
) : GroupApplicationRepositoryContract {
    override fun getGroupApplication(userId: String): Single<BaseDataSourceApi<List<GroupApplicationEntity>>> {
        return api.getGroupApplication(userId)
    }

    override fun createApplication(
        userId: String,
        groupApplicationMemberPost: List<DataDebiturNonSosialEntity>
    ): Single<BaseDataSourceApi<List<MemberApplicationDetailEntity>>> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId.toString())
        groupApplicationMemberPost.forEachIndexed { index, value ->
            builder.addFormDataPart("member[$index][service_type]", value.jenisLayanan.toString())
            builder.addFormDataPart("member[$index][name]", value.nama.orEmpty())
            builder.addFormDataPart("member[$index][email]", value.email.orEmpty())
            builder.addFormDataPart("member[$index][date_of_birth]", value.tanggalLahir.orEmpty())
            builder.addFormDataPart("member[$index][phone_number]", value.noTelp.orEmpty())
            builder.addFormDataPart("member[$index][ktp]", value.nik.orEmpty())
            builder.addFormDataPart("member[$index][gender]", value.gender.orEmpty())
            builder.addFormDataPart("member[$index][submission_purpose]", value.submissionPurpose.orEmpty())
            builder.addFormDataPart("member[$index][detail_submission_purpose]", value.detailSubmissionPurpose.orEmpty())
            value.nilaiPermohonan?.let { builder.addFormDataPart("member[$index][amount_of_loan]", it.toString()) }
        }

        return api.create(builder.build())
    }

    override fun submitApplication(memberApplicationId: String): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("member_application_id", memberApplicationId)
        return api.submit(data.build())
    }

    override fun deleteApplication(userId: String): Single<ResultState<Any>> {
        TODO("Not yet implemented")
    }

    override fun getMemberApplication(memberApplicationId: String): Single<BaseDataSourceApi<List<MemberApplicationDetailEntity>>> {
       return api.getMemberApplication(memberApplicationId)
    }

    override fun delayCutting(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (body.amountOfLoan != 0) data.addFormDataPart("amount_of_loan", body.amountOfLoan.toString())
        if (body.timePeriod != 0) data.addFormDataPart("time_period", body.timePeriod.toString())
        if (!body.timePeriodUnit.isNullOrEmpty()) data.addFormDataPart("time_period_unit", body.timePeriodUnit!!)
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
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return api.delayCuttingUpdate(userId, requestBody)
    }

    override fun nonForestyComodity(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
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
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return api.nonForestyComodityDraft(userId, requestBody)
    }

    override fun nonWoodForestProduct(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>> {
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
        body.businessPartner!!.forEachIndexed { index, value ->
            data.addFormDataPart("business_partner[$index]name", value)
        }
        val requestBody = data.build()
        return api.nonWoodForestProductDraft(userId, requestBody)
    }
}