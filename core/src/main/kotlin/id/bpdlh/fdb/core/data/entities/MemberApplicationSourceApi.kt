package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 13/10/23.
 * Project: BPDLH App
 */

@Parcelize
data class MemberApplicationSourceApi(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("ktp")
    val ktp: String? = null,
    @field:SerializedName("place_of_birth")
    val placeOfBirth: String? = null,
    @field:SerializedName("date_of_birth")
    val dateOfBirth: String? = null,
    @field:SerializedName("gender")
    val gender: String? = null,
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,
    @field:SerializedName("kk")
    val kk: String? = null,
    @field:SerializedName("job")
    val job: String? = null,
    @field:SerializedName("other_job")
    val otherJob: String? = null,
    @field:SerializedName("couple_name")
    val coupleName: String? = null,
    @field:SerializedName("couple_ktp")
    val coupleKtp: String? = null,
    @field:SerializedName("couple_place_of_birth")
    val couplePlaceOfBirth: String? = null,
    @field:SerializedName("couple_date_of_birth")
    val coupleDateOfBirth: String? = null,
    @field:SerializedName("ktp_province")
    val ktpProvince: String? = null,
    @field:SerializedName("ktp_city")
    val ktpCity: String? = null,
    @field:SerializedName("ktp_district")
    val ktpDistrict: String? = null,
    @field:SerializedName("ktp_village")
    val ktpVillage: String? = null,
    @field:SerializedName("ktp_rt")
    val ktpRt: String? = null,
    @field:SerializedName("ktp_rw")
    val ktpRw: String? = null,
    @field:SerializedName("ktp_address")
    val ktpAddress: String? = null,
    @field:SerializedName("amount_of_loan")
    val amountOfLoan: Long? = null,
    @field:SerializedName("is_submittable")
    val isSubmittable: Boolean = false,
    @field:SerializedName("is_submitted")
    val isSubmitted: Boolean = false,
    @field:SerializedName("is_financing_submitted")
    val isFinancingSubmitted: Boolean = false,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("financing_status")
    val financingStatus: String? = null,
    @field:SerializedName("member_application_detail")
    val memberApplicationDetail: MemberApplicationDetail,
    @field:SerializedName("business_cycle_unit")
    val businessCycleUnit: String? = null,
    @field:SerializedName("usage_plan")
    val usagePlan: String? = null,
    @field:SerializedName("domicile_since_year")
    val domicileSinceYear: String? = null,
    @field:SerializedName("domicile_address")
    val domicileAddress: String? = null,
    @field:SerializedName("member_application_detail_id")
    val memberApplicationDetailId: String? = null,
    @field:SerializedName("estimated_net_income")
    val estimatedNetIncome: Long? = null,
    @field:SerializedName("domicile_longitude")
    val domicileLongitude: String? = null,
    @field:SerializedName("business_duration")
    val businessDuration: Int? = null,
    @field:SerializedName("business_duration_unit")
    val businessDurationUnit: String? = null,
    @field:SerializedName("time_period_unit")
    val timePeriodUnit: String? = null,
    @field:SerializedName("couple_ktp_file")
    val coupleKtpFile: String? = null,
    @field:SerializedName("income_cycle_unit")
    val incomeCycleUnit: String? = null,
    @field:SerializedName("created_at")
    val createdAt: String? = null,
    @field:SerializedName("is_completed")
    val isCompleted: Boolean? = null,
    @field:SerializedName("updated_at")
    val updatedAt: String? = null,
    @field:SerializedName("business_commodity")
    val businessCommodity: String? = null,
    @field:SerializedName("income_cycle")
    val incomeCycle: Int? = null,
    @field:SerializedName("estimated_production_cost")
    val estimatedProductionCost: Long? = null,
    @field:SerializedName("marketing_objective")
    val marketingObjective: String? = null,
    @field:SerializedName("ktp_file")
    val ktpFile: String? = null,
    @field:SerializedName("domicile_village")
    val domicileVillage: String? = null,
    @field:SerializedName("time_period")
    val timePeriod: Int? = null,
    @field:SerializedName("created_in")
    val createdIn: String? = null,
    @field:SerializedName("domicile_province")
    val domicileProvince: String? = null,
    @field:SerializedName("estimated_turnover")
    val estimatedTurnover: Long? = null,
    @field:SerializedName("expense")
    val expense: Long? = null,
    @field:SerializedName("domicile_city")
    val domicileCity: String? = null,
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("couple_ktp_file_url")
    val coupleKtpFileUrl: String? = null,
    @field:SerializedName("largest_use_expense")
    val largestUseExpense: String? = null,
    @field:SerializedName("income")
    val income: Long? = null,
    @field:SerializedName("kk_file")
    val kkFile: String? = null,
    @field:SerializedName("production_business_cycle_unit")
    val productionBusinessCycleUnit: String? = null,
    @field:SerializedName("production_business_cycle")
    val productionBusinessCycle: Int? = null,
    @field:SerializedName("domicile_latitude")
    val domicileLatitude: String? = null,
    @field:SerializedName("ktp_file_url")
    val ktpFileUrl: String? = null,
    @field:SerializedName("business_type")
    val businessType: String? = null,
    @field:SerializedName("domicile_rt")
    val domicileRt: String? = null,
    @field:SerializedName("domicile_rw")
    val domicileRw: String? = null,
    @field:SerializedName("largest_expense")
    val largestExpense: Long? = null,
    @field:SerializedName("business_economic_value")
    val businessEconomicValue: Long? = null,
    @field:SerializedName("amount_of_installment")
    val amountOfInstallment: Long? = null,
    @field:SerializedName("source_of_production")
    val sourceOfProduction: String? = null,
    @field:SerializedName("business_capacity")
    val businessCapacity: String? = null,
    @field:SerializedName("business_cycle")
    val businessCycle: Int? = null,
    @field:SerializedName("kk_file_url")
    val kkFileUrl: String? = null,
    @field:SerializedName("domicile_district")
    val domicileDistrict: String? = null,
    @field:SerializedName("service_type")
    val serviceType: String? = null,
): Parcelable
