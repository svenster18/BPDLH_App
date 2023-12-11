package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

data class OtherDocumentDraftSourceApi(

    @field:SerializedName("file_url")
    val fileUrl: String,

    @field:SerializedName("file")
    val file: String,

    @field:SerializedName("member_application_detail_draft")
    val memberApplicationDetailDraft: MemberApplicationDetailDraft,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("member_application_detail_draft_id")
    val memberApplicationDetailDraftId: String,

    @field:SerializedName("_id")
    val id: String
)

data class MemberApplicationDetailDraft(

    @field:SerializedName("other_job")
    val otherJob: String,

    @field:SerializedName("ktp_district")
    val ktpDistrict: String,

    @field:SerializedName("business_cycle_unit")
    val businessCycleUnit: String,

    @field:SerializedName("usage_plan")
    val usagePlan: String,

    @field:SerializedName("domicile_since_year")
    val domicileSinceYear: String,

    @field:SerializedName("income_file")
    val incomeFile: String,

    @field:SerializedName("domicile_address")
    val domicileAddress: String,

    @field:SerializedName("business_photo_description")
    val businessPhotoDescription: String,

    @field:SerializedName("business_management_type")
    val businessManagementType: String,

    @field:SerializedName("member_application_detail_id")
    val memberApplicationDetailId: String,

    @field:SerializedName("estimated_net_income")
    val estimatedNetIncome: Int,

    @field:SerializedName("warranty_plan")
    val warrantyPlan: String,

    @field:SerializedName("ktp")
    val ktp: String,

    @field:SerializedName("ktp_address")
    val ktpAddress: String,

    @field:SerializedName("financing_status")
    val financingStatus: String,

    @field:SerializedName("domicile_longitude")
    val domicileLongitude: String,

    @field:SerializedName("is_submittable")
    val isSubmittable: Boolean,

    @field:SerializedName("business_duration")
    val businessDuration: Int,

    @field:SerializedName("financing_created_in")
    val financingCreatedIn: String,

    @field:SerializedName("is_financing_submittable")
    val isFinancingSubmittable: Boolean,

    @field:SerializedName("business_duration_unit")
    val businessDurationUnit: String,

    @field:SerializedName("time_period_unit")
    val timePeriodUnit: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("couple_ktp_file")
    val coupleKtpFile: String,

    @field:SerializedName("date_of_birth")
    val dateOfBirth: String,

    @field:SerializedName("income_cycle_unit")
    val incomeCycleUnit: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("is_completed")
    val isCompleted: Boolean,

    @field:SerializedName("house_photo_description")
    val housePhotoDescription: String,

    @field:SerializedName("couple_name")
    val coupleName: String,

    @field:SerializedName("business_photo_file")
    val businessPhotoFile: String,

    @field:SerializedName("ktp_village")
    val ktpVillage: String,

    @field:SerializedName("business_commodity")
    val businessCommodity: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("kk")
    val kk: String,

    @field:SerializedName("income_cycle")
    val incomeCycle: Int,

    @field:SerializedName("land_history_file")
    val landHistoryFile: String,

    @field:SerializedName("estimated_production_cost")
    val estimatedProductionCost: Int,

    @field:SerializedName("marketing_objective")
    val marketingObjective: String,

    @field:SerializedName("ktp_file")
    val ktpFile: String,

    @field:SerializedName("ktp_rw")
    val ktpRw: String,

    @field:SerializedName("other_business_type")
    val otherBusinessType: String,

    @field:SerializedName("domicile_village")
    val domicileVillage: String,

    @field:SerializedName("ktp_rt")
    val ktpRt: String,

    @field:SerializedName("ktp_city")
    val ktpCity: String,

    @field:SerializedName("time_period")
    val timePeriod: Int,

    @field:SerializedName("created_in")
    val createdIn: String,

    @field:SerializedName("sppt_file")
    val spptFile: String,

    @field:SerializedName("is_submitted")
    val isSubmitted: Boolean,

    @field:SerializedName("financing_scheme")
    val financingScheme: String,

    @field:SerializedName("domicile_province")
    val domicileProvince: String,

    @field:SerializedName("management_activity_type")
    val managementActivityType: String,

    @field:SerializedName("couple_ktp")
    val coupleKtp: String,

    @field:SerializedName("estimated_turnover")
    val estimatedTurnover: Int,

    @field:SerializedName("other_business_income")
    val otherBusinessIncome: Int,

    @field:SerializedName("house_photo_file")
    val housePhotoFile: String,

    @field:SerializedName("expense")
    val expense: Int,

    @field:SerializedName("submission_purpose")
    val submissionPurpose: String,

    @field:SerializedName("domicile_city")
    val domicileCity: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("phone_number")
    val phoneNumber: String,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("job")
    val job: String,

    @field:SerializedName("detail_submission_purpose")
    val detailSubmissionPurpose: String,

    @field:SerializedName("amount_of_loan")
    val amountOfLoan: Int,

    @field:SerializedName("largest_use_expense")
    val largestUseExpense: String,

    @field:SerializedName("other_business_cycle_unit")
    val otherBusinessCycleUnit: String,

    @field:SerializedName("income")
    val income: Int,

    @field:SerializedName("kk_file")
    val kkFile: String,

    @field:SerializedName("production_business_cycle_unit")
    val productionBusinessCycleUnit: String,

    @field:SerializedName("ktp_province")
    val ktpProvince: String,

    @field:SerializedName("recent_sale")
    val recentSale: Int,

    @field:SerializedName("production_business_cycle")
    val productionBusinessCycle: Int,

    @field:SerializedName("couple_place_of_birth")
    val couplePlaceOfBirth: String,

    @field:SerializedName("domicile_latitude")
    val domicileLatitude: String,

    @field:SerializedName("is_financing_completed")
    val isFinancingCompleted: Boolean,

    @field:SerializedName("qty_business_commodity")
    val qtyBusinessCommodity: Int,

    @field:SerializedName("productivity")
    val productivity: String,

    @field:SerializedName("transfer_declaration_file")
    val transferDeclarationFile: String,

    @field:SerializedName("business_type")
    val businessType: String,

    @field:SerializedName("domicile_rt")
    val domicileRt: String,

    @field:SerializedName("other_supporting_file")
    val otherSupportingFile: String,

    @field:SerializedName("domicile_rw")
    val domicileRw: String,

    @field:SerializedName("largest_expense")
    val largestExpense: Int,

    @field:SerializedName("business_economic_value")
    val businessEconomicValue: Int,

    @field:SerializedName("amount_of_installment")
    val amountOfInstallment: Int,

    @field:SerializedName("swaphoto_ktp_file")
    val swaphotoKtpFile: String,

    @field:SerializedName("land_photo_file")
    val landPhotoFile: String,

    @field:SerializedName("collateral_file")
    val collateralFile: String,

    @field:SerializedName("other_business_cycle")
    val otherBusinessCycle: Int,

    @field:SerializedName("couple_date_of_birth")
    val coupleDateOfBirth: String,

    @field:SerializedName("source_of_production")
    val sourceOfProduction: String,

    @field:SerializedName("profit_loss_file")
    val profitLossFile: String,

    @field:SerializedName("business_capacity")
    val businessCapacity: String,

    @field:SerializedName("business_cycle")
    val businessCycle: Int,

    @field:SerializedName("place_of_birth")
    val placeOfBirth: String,

    @field:SerializedName("service_type")
    val serviceType: String,

    @field:SerializedName("member_application_detail")
    val memberApplicationDetail: MemberApplicationDetail,

    @field:SerializedName("land_tenure_file")
    val landTenureFile: String,

    @field:SerializedName("land_area_cultivated")
    val landAreaCultivated: Int,

    @field:SerializedName("is_financing_submitted")
    val isFinancingSubmitted: Boolean,

    @field:SerializedName("domicile_district")
    val domicileDistrict: String,

    @field:SerializedName("swaphoto_ktp_description")
    val swaphotoKtpDescription: String
)
