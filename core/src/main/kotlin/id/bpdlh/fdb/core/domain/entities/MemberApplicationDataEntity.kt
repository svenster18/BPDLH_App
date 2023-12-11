package id.bpdlh.fdb.core.domain.entities


import com.google.gson.annotations.SerializedName

data class MemberApplicationDataEntity(
    @SerializedName("agreement_id")
    val agreementId: Any?,
    @SerializedName("amount_of_installment")
    val amountOfInstallment: Int?,
    @SerializedName("amount_of_loan")
    val amountOfLoan: Int?,
    @SerializedName("business_capacity")
    val businessCapacity: Int?,
    @SerializedName("business_commodity")
    val businessCommodity: String?,
    @SerializedName("business_cycle")
    val businessCycle: Int?,
    @SerializedName("business_cycle_unit")
    val businessCycleUnit: String?,
    @SerializedName("business_duration")
    val businessDuration: Int?,
    @SerializedName("business_duration_unit")
    val businessDurationUnit: String?,
    @SerializedName("business_economic_value")
    val businessEconomicValue: Int?,
    @SerializedName("business_management_type")
    val businessManagementType: String?,
    @SerializedName("business_photo_description")
    val businessPhotoDescription: Any?,
    @SerializedName("business_photo_file")
    val businessPhotoFile: String?,
    @SerializedName("business_type")
    val businessType: String?,
    @SerializedName("collateral_file")
    val collateralFile: String?,
    @SerializedName("couple_date_of_birth")
    val coupleDateOfBirth: Any?,
    @SerializedName("couple_ktp")
    val coupleKtp: Any?,
    @SerializedName("couple_ktp_file")
    val coupleKtpFile: String?,
    @SerializedName("couple_name")
    val coupleName: Any?,
    @SerializedName("couple_place_of_birth")
    val couplePlaceOfBirth: Any?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("created_in")
    val createdIn: Any?,
    @SerializedName("date_of_birth")
    val dateOfBirth: String?,
    @SerializedName("detail_submission_purpose")
    val detailSubmissionPurpose: String?,
    @SerializedName("domicile_address")
    val domicileAddress: Any?,
    @SerializedName("domicile_city")
    val domicileCity: Any?,
    @SerializedName("domicile_district")
    val domicileDistrict: Any?,
    @SerializedName("domicile_latitude")
    val domicileLatitude: Any?,
    @SerializedName("domicile_longitude")
    val domicileLongitude: Any?,
    @SerializedName("domicile_province")
    val domicileProvince: Any?,
    @SerializedName("domicile_rt")
    val domicileRt: Any?,
    @SerializedName("domicile_rw")
    val domicileRw: Any?,
    @SerializedName("domicile_since_year")
    val domicileSinceYear: Any?,
    @SerializedName("domicile_village")
    val domicileVillage: Any?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("estimated_net_income")
    val estimatedNetIncome: Int?,
    @SerializedName("estimated_production_cost")
    val estimatedProductionCost: Int?,
    @SerializedName("estimated_turnover")
    val estimatedTurnover: Int?,
    @SerializedName("expense")
    val expense: Int?,
    @SerializedName("financing_created_in")
    val financingCreatedIn: String?,
    @SerializedName("financing_scheme")
    val financingScheme: String?,
    @SerializedName("financing_status")
    val financingStatus: String?,
    @SerializedName("house_photo_description")
    val housePhotoDescription: Any?,
    @SerializedName("house_photo_file")
    val housePhotoFile: String?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("income")
    val income: Int?,
    @SerializedName("income_cycle")
    val incomeCycle: Int?,
    @SerializedName("income_cycle_unit")
    val incomeCycleUnit: Any?,
    @SerializedName("income_file")
    val incomeFile: String?,
    @SerializedName("is_completed")
    val isCompleted: Boolean?,
    @SerializedName("is_financing_completed")
    val isFinancingCompleted: Boolean?,
    @SerializedName("is_financing_submittable")
    val isFinancingSubmittable: Boolean?,
    @SerializedName("is_financing_submitted")
    val isFinancingSubmitted: Boolean?,
    @SerializedName("is_submittable")
    val isSubmittable: Boolean?,
    @SerializedName("is_submitted")
    val isSubmitted: Boolean?,
    @SerializedName("job")
    val job: Any?,
    @SerializedName("kk")
    val kk: Any?,
    @SerializedName("kk_file")
    val kkFile: String?,
    @SerializedName("ktp")
    val ktp: String?,
    @SerializedName("ktp_address")
    val ktpAddress: Any?,
    @SerializedName("ktp_city")
    val ktpCity: Any?,
    @SerializedName("ktp_district")
    val ktpDistrict: Any?,
    @SerializedName("ktp_file")
    val ktpFile: String?,
    @SerializedName("ktp_province")
    val ktpProvince: Any?,
    @SerializedName("ktp_rt")
    val ktpRt: Any?,
    @SerializedName("ktp_rw")
    val ktpRw: Any?,
    @SerializedName("ktp_village")
    val ktpVillage: Any?,
    @SerializedName("land_area_cultivated")
    val landAreaCultivated: Int?,
    @SerializedName("land_history_file")
    val landHistoryFile: String?,
    @SerializedName("land_photo_file")
    val landPhotoFile: String?,
    @SerializedName("land_tenure_file")
    val landTenureFile: String?,
    @SerializedName("largest_expense")
    val largestExpense: Int?,
    @SerializedName("largest_use_expense")
    val largestUseExpense: Any?,
    @SerializedName("management_activity_type")
    val managementActivityType: String?,
    @SerializedName("marketing_objective")
    val marketingObjective: String?,
    @SerializedName("member_application_detail")
    val memberApplicationDetail: MemberApplicationDetail?,
    @SerializedName("member_application_detail_id")
    val memberApplicationDetailId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("other_business_cycle")
    val otherBusinessCycle: Int?,
    @SerializedName("other_business_cycle_unit")
    val otherBusinessCycleUnit: String?,
    @SerializedName("other_business_income")
    val otherBusinessIncome: Int?,
    @SerializedName("other_business_type")
    val otherBusinessType: String?,
    @SerializedName("other_job")
    val otherJob: String?,
    @SerializedName("other_supporting_file")
    val otherSupportingFile: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    @SerializedName("production_business_cycle")
    val productionBusinessCycle: Int?,
    @SerializedName("production_business_cycle_unit")
    val productionBusinessCycleUnit: String?,
    @SerializedName("productivity")
    val productivity: String?,
    @SerializedName("profit_loss_file")
    val profitLossFile: String?,
    @SerializedName("program_name")
    val programName: String?,
    @SerializedName("qty_business_commodity")
    val qtyBusinessCommodity: Int?,
    @SerializedName("recent_sale")
    val recentSale: Int?,
    @SerializedName("service_type")
    val serviceType: String?,
    @SerializedName("source_of_production")
    val sourceOfProduction: String?,
    @SerializedName("sppt_file")
    val spptFile: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status_submission")
    val statusSubmission: String?,
    @SerializedName("submission_purpose")
    val submissionPurpose: String?,
    @SerializedName("swaphoto_ktp_description")
    val swaphotoKtpDescription: String?,
    @SerializedName("swaphoto_ktp_file")
    val swaphotoKtpFile: String?,
    @SerializedName("time_period")
    val timePeriod: Int?,
    @SerializedName("time_period_unit")
    val timePeriodUnit: String?,
    @SerializedName("transfer_declaration_file")
    val transferDeclarationFile: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("usage_plan")
    val usagePlan: String?,
    @SerializedName("warranty_plan")
    val warrantyPlan: String?
) {
    data class MemberApplicationDetail(
        @SerializedName("agreement_id")
        val agreementId: Any?,
        @SerializedName("amount_of_installment")
        val amountOfInstallment: Int?,
        @SerializedName("amount_of_loan")
        val amountOfLoan: Int?,
        @SerializedName("business_capacity")
        val businessCapacity: Any?,
        @SerializedName("business_commodity")
        val businessCommodity: Any?,
        @SerializedName("business_cycle")
        val businessCycle: Int?,
        @SerializedName("business_cycle_unit")
        val businessCycleUnit: Any?,
        @SerializedName("business_duration")
        val businessDuration: Int?,
        @SerializedName("business_duration_unit")
        val businessDurationUnit: Any?,
        @SerializedName("business_economic_value")
        val businessEconomicValue: Int?,
        @SerializedName("business_management_type")
        val businessManagementType: Any?,
        @SerializedName("business_photo_description")
        val businessPhotoDescription: Any?,
        @SerializedName("business_photo_file")
        val businessPhotoFile: String?,
        @SerializedName("business_type")
        val businessType: Any?,
        @SerializedName("collateral_file")
        val collateralFile: String?,
        @SerializedName("couple_date_of_birth")
        val coupleDateOfBirth: Any?,
        @SerializedName("couple_ktp")
        val coupleKtp: Any?,
        @SerializedName("couple_ktp_file")
        val coupleKtpFile: String?,
        @SerializedName("couple_name")
        val coupleName: Any?,
        @SerializedName("couple_place_of_birth")
        val couplePlaceOfBirth: Any?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("created_in")
        val createdIn: Any?,
        @SerializedName("date_of_birth")
        val dateOfBirth: String?,
        @SerializedName("detail_submission_purpose")
        val detailSubmissionPurpose: Any?,
        @SerializedName("domicile_address")
        val domicileAddress: Any?,
        @SerializedName("domicile_city")
        val domicileCity: Any?,
        @SerializedName("domicile_district")
        val domicileDistrict: Any?,
        @SerializedName("domicile_latitude")
        val domicileLatitude: Any?,
        @SerializedName("domicile_longitude")
        val domicileLongitude: Any?,
        @SerializedName("domicile_province")
        val domicileProvince: Any?,
        @SerializedName("domicile_rt")
        val domicileRt: Any?,
        @SerializedName("domicile_rw")
        val domicileRw: Any?,
        @SerializedName("domicile_since_year")
        val domicileSinceYear: Any?,
        @SerializedName("domicile_village")
        val domicileVillage: Any?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("estimated_net_income")
        val estimatedNetIncome: Int?,
        @SerializedName("estimated_production_cost")
        val estimatedProductionCost: Int?,
        @SerializedName("estimated_turnover")
        val estimatedTurnover: Int?,
        @SerializedName("expense")
        val expense: Int?,
        @SerializedName("financing_created_in")
        val financingCreatedIn: Any?,
        @SerializedName("financing_scheme")
        val financingScheme: Any?,
        @SerializedName("financing_status")
        val financingStatus: String?,
        @SerializedName("house_photo_description")
        val housePhotoDescription: Any?,
        @SerializedName("house_photo_file")
        val housePhotoFile: String?,
        @SerializedName("_id")
        val id: String?,
        @SerializedName("income")
        val income: Int?,
        @SerializedName("income_cycle")
        val incomeCycle: Int?,
        @SerializedName("income_cycle_unit")
        val incomeCycleUnit: Any?,
        @SerializedName("income_file")
        val incomeFile: String?,
        @SerializedName("is_completed")
        val isCompleted: Boolean?,
        @SerializedName("is_financing_completed")
        val isFinancingCompleted: Boolean?,
        @SerializedName("is_financing_submittable")
        val isFinancingSubmittable: Boolean?,
        @SerializedName("is_financing_submitted")
        val isFinancingSubmitted: Boolean?,
        @SerializedName("is_submittable")
        val isSubmittable: Boolean?,
        @SerializedName("is_submitted")
        val isSubmitted: Boolean?,
        @SerializedName("job")
        val job: Any?,
        @SerializedName("kk")
        val kk: Any?,
        @SerializedName("kk_file")
        val kkFile: String?,
        @SerializedName("ktp")
        val ktp: String?,
        @SerializedName("ktp_address")
        val ktpAddress: Any?,
        @SerializedName("ktp_city")
        val ktpCity: Any?,
        @SerializedName("ktp_district")
        val ktpDistrict: Any?,
        @SerializedName("ktp_file")
        val ktpFile: String?,
        @SerializedName("ktp_province")
        val ktpProvince: Any?,
        @SerializedName("ktp_rt")
        val ktpRt: Any?,
        @SerializedName("ktp_rw")
        val ktpRw: Any?,
        @SerializedName("ktp_village")
        val ktpVillage: Any?,
        @SerializedName("land_area_cultivated")
        val landAreaCultivated: Int?,
        @SerializedName("land_history_file")
        val landHistoryFile: String?,
        @SerializedName("land_photo_file")
        val landPhotoFile: String?,
        @SerializedName("land_tenure_file")
        val landTenureFile: String?,
        @SerializedName("largest_expense")
        val largestExpense: Int?,
        @SerializedName("largest_use_expense")
        val largestUseExpense: Any?,
        @SerializedName("management_activity_type")
        val managementActivityType: Any?,
        @SerializedName("marketing_objective")
        val marketingObjective: Any?,
        @SerializedName("member_application")
        val memberApplication: MemberApplication?,
        @SerializedName("member_application_id")
        val memberApplicationId: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("other_business_cycle")
        val otherBusinessCycle: Int?,
        @SerializedName("other_business_cycle_unit")
        val otherBusinessCycleUnit: Any?,
        @SerializedName("other_business_income")
        val otherBusinessIncome: Int?,
        @SerializedName("other_business_type")
        val otherBusinessType: Any?,
        @SerializedName("other_job")
        val otherJob: Any?,
        @SerializedName("other_supporting_file")
        val otherSupportingFile: String?,
        @SerializedName("phone_number")
        val phoneNumber: String?,
        @SerializedName("place_of_birth")
        val placeOfBirth: Any?,
        @SerializedName("production_business_cycle")
        val productionBusinessCycle: Int?,
        @SerializedName("production_business_cycle_unit")
        val productionBusinessCycleUnit: Any?,
        @SerializedName("productivity")
        val productivity: Any?,
        @SerializedName("profit_loss_file")
        val profitLossFile: String?,
        @SerializedName("program_name")
        val programName: Any?,
        @SerializedName("qty_business_commodity")
        val qtyBusinessCommodity: Int?,
        @SerializedName("recent_sale")
        val recentSale: Int?,
        @SerializedName("service_type")
        val serviceType: String?,
        @SerializedName("source_of_production")
        val sourceOfProduction: Any?,
        @SerializedName("sppt_file")
        val spptFile: String?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("status_submission")
        val statusSubmission: String?,
        @SerializedName("submission_purpose")
        val submissionPurpose: Any?,
        @SerializedName("swaphoto_ktp_description")
        val swaphotoKtpDescription: Any?,
        @SerializedName("swaphoto_ktp_file")
        val swaphotoKtpFile: String?,
        @SerializedName("time_period")
        val timePeriod: Int?,
        @SerializedName("time_period_unit")
        val timePeriodUnit: Any?,
        @SerializedName("transfer_declaration_file")
        val transferDeclarationFile: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("usage_plan")
        val usagePlan: Any?,
        @SerializedName("user")
        val user: User?,
        @SerializedName("user_id")
        val userId: String?,
        @SerializedName("warranty_plan")
        val warrantyPlan: Any?
    ) {
        data class MemberApplication(
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("request_date")
            val requestDate: String?,
            @SerializedName("status")
            val status: String?,
            @SerializedName("status_progress_desk_analyst")
            val statusProgressDeskAnalyst: String?,
            @SerializedName("status_progress_field_analyst")
            val statusProgressFieldAnalyst: String?,
            @SerializedName("total_member")
            val totalMember: Int?,
            @SerializedName("total_member_approved")
            val totalMemberApproved: Int?,
            @SerializedName("total_member_rejected")
            val totalMemberRejected: Int?,
            @SerializedName("user")
            val user: User?,
            @SerializedName("user_id")
            val userId: String?
        ) {
            data class User(
                @SerializedName("created_at")
                val createdAt: String?,
                @SerializedName("email")
                val email: String?,
                @SerializedName("_id")
                val id: String?,
                @SerializedName("reference_id")
                val referenceId: Any?,
                @SerializedName("role")
                val role: String?
            )
        }

        data class User(
            @SerializedName("created_at")
            val createdAt: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("_id")
            val id: String?,
            @SerializedName("reference_id")
            val referenceId: Any?,
            @SerializedName("role")
            val role: String?
        )
    }
}