package id.bpdlh.fdb.core.data.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class PermohonanPembiayaanPerhutananSosialPost(
    @field:SerializedName("ktp") var ktp: String = "",
    @field:SerializedName("group_name") var groupName: String = "",
    @field:SerializedName("name") var name: String = "",
    @field:SerializedName("place_of_birth") var placeOfBirth: String = "",
    @field:SerializedName("date_of_birth") var dateOfBirth: String = "",
    @field:SerializedName("gender") var gender: String = "",
    @field:SerializedName("kk") var kk: String = "",
    @field:SerializedName("job") var job: String = "",
    @field:SerializedName("other_job") var otherJob: String = "",
    @field:SerializedName("couple_ktp") var coupleKtp: String = "",
    @field:SerializedName("couple_name") var coupleName: String = "",
    @field:SerializedName("couple_place_of_birth") var couplePlaceOfBirth: String = "",
    @field:SerializedName("couple_date_of_birth") var coupleDateOfBirth: String = "",
    @field:SerializedName("ktp_province") var ktpProvince: String = "",
    @field:SerializedName("ktp_city") var ktpCity: String = "",
    @field:SerializedName("ktp_district") var ktpDistrict: String = "",
    @field:SerializedName("ktp_village") var ktpVillage: String = "",
    @field:SerializedName("ktp_rt") var ktpRt: String = "",
    @field:SerializedName("ktp_rw") var ktpRw: String = "",
    @field:SerializedName("ktp_address") var ktpAddress: String = "",
    @field:SerializedName("domicile_province") var domicileProvince: String = "",
    @field:SerializedName("domicile_city") var domicileCity: String = "",
    @field:SerializedName("domicile_district") var domicileDistrict: String = "",
    @field:SerializedName("domicile_village") var domicileVillage: String = "",
    @field:SerializedName("domicile_rt") var domicileRt: String = "",
    @field:SerializedName("domicile_rw") var domicileRw: String = "",
    @field:SerializedName("domicile_address") var domicileAddress: String = "",
    @field:SerializedName("domicile_latitude") var domicileLatitude: String = "",
    @field:SerializedName("domicile_longitude") var domicileLongitude: String = "",
    @field:SerializedName("domicile_since_year") var domicileSinceYear: String = "",
    @field:SerializedName("income") var income: Long = 0,
    @field:SerializedName("income_cycle") var incomeCycle: Int = 0,
    @field:SerializedName("income_cycle_unit") var incomeCycleUnit: String = "",
    @field:SerializedName("expense") var expense: Long = 0,
    @field:SerializedName("largest_expense") var largestExpense: Long = 0,
    @field:SerializedName("largest_use_expense") var largestUseExpense: String = "",
    @field:SerializedName("business_type") var businessType: String = "",
    @field:SerializedName("business_commodity") var businessCommodity: String = "",
    @field:SerializedName("business_duration") var businessDuration: Int = 0,
    @field:SerializedName("business_duration_unit") var businessDurationUnit: String = "",
    @field:SerializedName("source_of_production") var sourceOfProduction: String = "",
    @field:SerializedName("business_capacity") var businessCapacity: String = "",
    @field:SerializedName("business_economic_value") var businessEconomicValue: Long = 0,
    @field:SerializedName("marketing_objective") var marketingObjective: String = "",
    @field:SerializedName("usage_plan") var usagePlan: String = "",
    @field:SerializedName("estimated_turnover") var estimatedTurnover: Long = 0,
    @field:SerializedName("estimated_production_cost") var estimatedProductionCost: Long = 0,
    @field:SerializedName("estimated_net_income") var estimatedNetIncome: Long = 0,
    @field:SerializedName("production_business_cycle") var productionBusinessCycle: Int = 0,
    @field:SerializedName("production_business_cycle_unit") var productionBusinessCycleUnit: String = "",
    @field:SerializedName("amount_of_loan") var amountOfLoan: Long = 0,
    @field:SerializedName("time_period") var timePeriod: Int = 0,
    @field:SerializedName("time_period_unit") var timePeriodUnit: String = "",
    @field:SerializedName("amount_of_installment") var amountOfInstallment: Long = 0,
    @field:SerializedName("created_in") var createdIn: String = "",
    @field:SerializedName("created_at") var createdAt: String = "",
    @field:SerializedName("ktp_file") var ktpFile: File? = null,
    @field:SerializedName("kk_file") var kkFile: File? = null,
    @field:SerializedName("couple_ktp_file") var coupleKtpFile: File? = null,
    @field:SerializedName("ktp_file") var ktpFileId: String = "",
    @field:SerializedName("kk_file") var kkFileId: String = "",
    @field:SerializedName("couple_ktp_file") var coupleKtpFileId: String = "",
    @field:SerializedName("business_partner") var businessPartners: List<Mitra>,
    @field:SerializedName("business_partner") var guarantee: List<JaminanEntity>,
    var ktpFileUrl: String = "",
    var kkFileUrl: String = "",
    var coupleKtpFileUrl: String = "",
    var ktpFileMime: String = "",
    var ktpFileSize: String = ""
) : Parcelable
