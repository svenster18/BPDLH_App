package id.bpdlh.fdb.core.domain.entities


import com.google.gson.annotations.SerializedName

data class MemberApplicationDetailEntity(
    @SerializedName("amount_of_installment")
    val amountOfInstallment: Int? = null,
    @SerializedName("amount_of_loan")
    val amountOfLoan: Int? = null,
    @SerializedName("business_capacity")
    val businessCapacity: Any? = null,
    @SerializedName("business_commodity")
    val businessCommodity: Any? = null,
    @SerializedName("business_cycle")
    val businessCycle: Int? = null,
    @SerializedName("business_cycle_unit")
    val businessCycleUnit: Any? = null,
    @SerializedName("business_duration")
    val businessDuration: Int? = null,
    @SerializedName("business_duration_unit")
    val businessDurationUnit: Any? = null,
    @SerializedName("business_economic_value")
    val businessEconomicValue: Int? = null,
    @SerializedName("business_management_type")
    val businessManagementType: Any? = null,
    @SerializedName("business_photo_description")
    val businessPhotoDescription: Any? = null,
    @SerializedName("business_photo_file")
    val businessPhotoFile: Any? = null,
    @SerializedName("business_type")
    val businessType: Any? = null,
    @SerializedName("collateral_file")
    val collateralFile: Any? = null,
    @SerializedName("couple_date_of_birth")
    val coupleDateOfBirth: Any? = null,
    @SerializedName("couple_ktp")
    val coupleKtp: Any? = null,
    @SerializedName("couple_ktp_file")
    val coupleKtpFile: Any? = null,
    @SerializedName("couple_name")
    val coupleName: Any? = null,
    @SerializedName("couple_place_of_birth")
    val couplePlaceOfBirth: Any? = null,
    @SerializedName("created_in")
    val createdIn: Any? = null,
    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,
    @SerializedName("detail_submission_purpose")
    val detailSubmissionPurpose: Any? = null,
    @SerializedName("domicile_address")
    val domicileAddress: Any? = null,
    @SerializedName("domicile_city")
    val domicileCity: Any? = null,
    @SerializedName("domicile_district")
    val domicileDistrict: Any? = null,
    @SerializedName("domicile_latitude")
    val domicileLatitude: Any? = null,
    @SerializedName("domicile_longitude")
    val domicileLongitude: Any? = null,
    @SerializedName("domicile_province")
    val domicileProvince: Any? = null,
    @SerializedName("domicile_rt")
    val domicileRt: Any? = null,
    @SerializedName("domicile_rw")
    val domicileRw: Any? = null,
    @SerializedName("domicile_since_year")
    val domicileSinceYear: Any? = null,
    @SerializedName("domicile_village")
    val domicileVillage: Any? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("estimated_net_income")
    val estimatedNetIncome: Int? = null,
    @SerializedName("estimated_production_cost")
    val estimatedProductionCost: Int? = null,
    @SerializedName("estimated_turnover")
    val estimatedTurnover: Int? = null,
    @SerializedName("expense")
    val expense: Int? = null,
    @SerializedName("financing_created_in")
    val financingCreatedIn: Any? = null,
    @SerializedName("financing_scheme")
    val financingScheme: Any? = null,
    @SerializedName("financing_status")
    val financingStatus: String? = null,
    @SerializedName("house_photo_description")
    val housePhotoDescription: Any? = null,
    @SerializedName("house_photo_file")
    val housePhotoFile: Any? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("income")
    val income: Int? = null,
    @SerializedName("income_cycle")
    val incomeCycle: Int? = null,
    @SerializedName("income_cycle_unit")
    val incomeCycleUnit: Any? = null,
    @SerializedName("income_file")
    val incomeFile: Any? = null,
    @SerializedName("is_completed")
    val isCompleted: Boolean? = null,
    @SerializedName("is_financing_completed")
    val isFinancingCompleted: Boolean? = null,
    @SerializedName("is_financing_submittable")
    val isFinancingSubmittable: Boolean? = null,
    @SerializedName("is_financing_submitted")
    val isFinancingSubmitted: Boolean? = null,
    @SerializedName("is_submittable")
    val isSubmittable: Boolean? = null,
    @SerializedName("is_submitted")
    val isSubmitted: Boolean? = null,
    @SerializedName("job")
    val job: Any? = null,
    @SerializedName("kk")
    val kk: Any? = null,
    @SerializedName("kk_file")
    val kkFile: Any? = null,
    @SerializedName("ktp")
    val ktp: String? = null,
    @SerializedName("ktp_address")
    val ktpAddress: Any? = null,
    @SerializedName("ktp_city")
    val ktpCity: Any? = null,
    @SerializedName("ktp_district")
    val ktpDistrict: Any? = null,
    @SerializedName("ktp_file")
    val ktpFile: Any? = null,
    @SerializedName("ktp_province")
    val ktpProvince: Any? = null,
    @SerializedName("ktp_rt")
    val ktpRt: Any? = null,
    @SerializedName("ktp_rw")
    val ktpRw: Any? = null,
    @SerializedName("ktp_village")
    val ktpVillage: Any? = null,
    @SerializedName("land_area_cultivated")
    val landAreaCultivated: Int? = null,
    @SerializedName("land_history_file")
    val landHistoryFile: Any? = null,
    @SerializedName("land_photo_file")
    val landPhotoFile: Any? = null,
    @SerializedName("land_tenure_file")
    val landTenureFile: Any? = null,
    @SerializedName("largest_expense")
    val largestExpense: Int? = null,
    @SerializedName("largest_use_expense")
    val largestUseExpense: Any? = null,
    @SerializedName("management_activity_type")
    val managementActivityType: Any? = null,
    @SerializedName("marketing_objective")
    val marketingObjective: Any? = null,
    @SerializedName("member_application_detail_id")
    val memberApplicationDetailId: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("other_business_cycle")
    val otherBusinessCycle: Int? = null,
    @SerializedName("other_business_cycle_unit")
    val otherBusinessCycleUnit: Any? = null,
    @SerializedName("other_business_income")
    val otherBusinessIncome: Int? = null,
    @SerializedName("other_business_type")
    val otherBusinessType: Any? = null,
    @SerializedName("other_job")
    val otherJob: Any? = null,
    @SerializedName("other_supporting_file")
    val otherSupportingFile: Any? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    @SerializedName("place_of_birth")
    val placeOfBirth: Any? = null,
    @SerializedName("production_business_cycle")
    val productionBusinessCycle: Int? = null,
    @SerializedName("production_business_cycle_unit")
    val productionBusinessCycleUnit: Any? = null,
    @SerializedName("productivity")
    val productivity: Any? = null,
    @SerializedName("profit_loss_file")
    val profitLossFile: Any? = null,
    @SerializedName("qty_business_commodity")
    val qtyBusinessCommodity: Int? = null,
    @SerializedName("recent_sale")
    val recentSale: Int? = null,
    @SerializedName("service_type")
    val serviceType: String? = null,
    @SerializedName("source_of_production")
    val sourceOfProduction: Any? = null,
    @SerializedName("sppt_file")
    val spptFile: Any? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("submission_purpose")
    val submissionPurpose: Any? = null,
    @SerializedName("swaphoto_ktp_description")
    val swaphotoKtpDescription: Any? = null,
    @SerializedName("swaphoto_ktp_file")
    val swaphotoKtpFile: Any? = null,
    @SerializedName("time_period")
    val timePeriod: Int? = null,
    @SerializedName("time_period_unit")
    val timePeriodUnit: Any? = null,
    @SerializedName("transfer_declaration_file")
    val transferDeclarationFile: Any? = null,
    @SerializedName("usage_plan")
    val usagePlan: Any? = null,
    @SerializedName("warranty_plan")
    val warrantyPlan: Any? = null,
    @SerializedName("member_application_detail")
    val memberApplicationDetail: MemberApplicationDetail? = null
)


data class MemberApplicationDetail(
    @SerializedName("user_id")
    val userId: String? = null,
    @SerializedName("member_application_id")
    val memberApplicationId: String? = null,
)