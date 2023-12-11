package id.bpdlh.fdb.core.data.post

import com.google.gson.annotations.SerializedName

data class MemberApplicationPost(
    @field:SerializedName("service_type")
    var serviceType: String? = null,
    @field:SerializedName("time_period")
    var applicationValue: Number? = null,
    @field:SerializedName("amount_of_loan")
    var amountOfLoan: Number? = null,
    @field:SerializedName("time_period")
    var timePeriod: Number? = null,
    @field:SerializedName("time_period_unit")
    var timePeriodUnit: String? = null,
    @field:SerializedName("financing_scheme")
    var financingScheme: String? = null,
    @field:SerializedName("warranty_plan")
    var warrantyplan: String? = null,
    @field:SerializedName("business_type")
    var businessType: String? = null,
    @field:SerializedName("business_commodity")
    var businessCommodity: String? = null,
    @field:SerializedName("business_duration")
    var businessDuration: Number? = null,
    @field:SerializedName("business_duration_unit")
    var businessDurationUnit: String? = null,
    @field:SerializedName("productivity")
    var productivity: String? = null,
    @field:SerializedName("recent_sale")
    var recentSale: Number? = null,
    @field:SerializedName("land_area_cultivated")
    var landAreaCultivated: Number? = null,
    @field:SerializedName("estimated_turnover")
    var estimatedTurnover: Number? = null,
    @field:SerializedName("estimated_production_cost")
    var estimatedProductionCost: Number? = null,
    @field:SerializedName("estimated_net_income")
    var estimatedNetIncome: Number? = null,
    @field:SerializedName("marketing_objective")
    var marketingObjective: String? = null,
    @field:SerializedName("business_management_type")
    var businessManagementType: String? = null,
    @field:SerializedName("business_cycle")
    var businessCycle: Number? = null,
    @field:SerializedName("business_cycle_unit")
    var businessCycleUnit: String? = null,
    @field:SerializedName("qty_business_commodity")
    var qtyBusinessCommodity: Number? = null,
    @field:SerializedName("submission_purpose")
    var submissionPurpose: String? = null,
    @field:SerializedName("detail_submission_purpose")
    var detailSubmissionPurpose: String? = null,
    @field:SerializedName("financing_created_in")
    var financingCreatedIn: String? = null,
    @field:SerializedName("business_partner")
    var businessPartner: MutableList<String>? = mutableListOf(),
    @field:SerializedName("profit_loss_file")
    var profitLossFile: String? = null,
    @field:SerializedName("collateral_file")
    var collateralFile: String? = null,
    @field:SerializedName("land_tenure_file")
    var landTenureFile: String? = null,
    @field:SerializedName("land_history_file")
    var landHistoryFile: String? = null,
    @field:SerializedName("transfer_declaration_file")
    var transferDeclarationFile: String? = null,
    @field:SerializedName("sppt_file")
    var spptFile: String? = null,
    @field:SerializedName("land_photo_file")
    var landPhotoFile: String? = null,
    @field:SerializedName("management_activity_type")
    var managementActivityType: String? = null,
    @field:SerializedName("source_of_production")
    var sourceOfProduction: String? = null,
    @field:SerializedName("business_capacity")
    var businessCapacity: Int? = null,
    @field:SerializedName("other_supporting_file")
    var otherSupportingFile: String? = null,
)

//data class NonForestyComodityPost(
//    @field:SerializedName("amount_of_loan")
//    var amountOfLoan: Number,
//    @field:SerializedName("time_period")
//    var timePeriod: Number,
//    @field:SerializedName("time_period_unit")
//    var timePeriodUnit: String,
//    @field:SerializedName("financing_scheme")
//    var financingScheme: String,
//    @field:SerializedName("warranty_plan")
//    var warrantyplan: String,
//    @field:SerializedName("business_type")
//    var businessType: String,
//    @field:SerializedName("business_commodity")
//    var businessCommodity: String,
//    @field:SerializedName("business_duration")
//    var businessDuration: Number,
//    @field:SerializedName("business_duration_unit")
//    var businessDurationUnit: String,
//    @field:SerializedName("productivity")
//    var productivity: String,
//    @field:SerializedName("business_capacity")
//    var businessCapacity: String,
//    @field:SerializedName("recent_sale")
//    var recentSale: Number,
//    @field:SerializedName("marketing_objective")
//    var marketingObjective: String,
//    @field:SerializedName("submission_purpose")
//    var submissionPurpose: String,
//    @field:SerializedName("detail_submission_purpose")
//    var detailSubmissionPurpose: String,
//    @field:SerializedName("estimated_turnover")
//    var estimatedTurnover: Number,
//    @field:SerializedName("estimated_production_cost")
//    var estimatedProductionCost: Number,
//    @field:SerializedName("estimated_net_income")
//    var estimatedNetIncome: Number,
//    @field:SerializedName("business_cycle")
//    var businessCycle: Number,
//    @field:SerializedName("business_cycle_unit")
//    var businessCycleUnit: String,
//    @field:SerializedName("financing_created_in")
//    var financingCreatedIn: String,
//    @field:SerializedName("business_partner")
//    var businessPartner: MutableList<String>? = mutableListOf(),
//    @field:SerializedName("profit_loss_file")
//    var profitLossFile: String,
//    @field:SerializedName("collateral_file")
//    var collateralFile: String,
//    @field:SerializedName("land_tenure_file")
//    var landTenureFile: String,
//    @field:SerializedName("other_supporting_file")
//    var otherSupportingFile: String,
//    @field:SerializedName("land_photo_file")
//    var landPhotoFile: String,
//)

//data class NonWoodForestProductPost(
//    @field:SerializedName("amount_of_loan")
//    var amountOfLoan: Number,
//    @field:SerializedName("time_period")
//    var timePeriod: Number,
//    @field:SerializedName("time_period_unit")
//    var timePeriodUnit: String,
//    @field:SerializedName("financing_scheme")
//    var financingScheme: String,
//    @field:SerializedName("business_commodity")
//    var businessCommodity: String,
//    @field:SerializedName("management_activity_type")
//    var managementActivityType: String,
//    @field:SerializedName("business_duration")
//    var businessDuration: Number,
//    @field:SerializedName("business_duration_unit")
//    var businessDurationUnit: String,
//    @field:SerializedName("source_of_production")
//    var sourceOfProduction: String,
//    @field:SerializedName("business_capacity")
//    var businessCapacity: String,
//    @field:SerializedName("recent_sale")
//    var recentSale: Number,
//    @field:SerializedName("marketing_objective")
//    var marketingObjective: String,
//    @field:SerializedName("submission_purpose")
//    var submissionPurpose: String,
//    @field:SerializedName("detail_submission_purpose")
//    var detailSubmissionPurpose: String,
//    @field:SerializedName("estimated_turnover")
//    var estimatedTurnover: Number,
//    @field:SerializedName("estimated_production_cost")
//    var estimatedProductionCost: Number,
//    @field:SerializedName("estimated_net_income")
//    var estimatedNetIncome: Number,
//    @field:SerializedName("business_cycle")
//    var businessCycle: Number,
//    @field:SerializedName("business_cycle_unit")
//    var businessCycleUnit: String,
//    @field:SerializedName("financing_created_in")
//    var financingCreatedIn: String,
//    @field:SerializedName("business_partner")
//    var businessPartner: MutableList<String>? = mutableListOf(),
//    @field:SerializedName("profit_loss_file")
//    var profitLossFile: String,
//    @field:SerializedName("collateral_file")
//    var collateralFile: String,
//    @field:SerializedName("land_tenure_file")
//    var landTenureFile: String,
//    @field:SerializedName("other_supporting_file")
//    var otherSupportingFile: String,
//    @field:SerializedName("land_photo_file")
//    var landPhotoFile: String,
//)