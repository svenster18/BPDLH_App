package id.bpdlh.fdb.core.data.post

import java.io.File
import com.google.gson.annotations.SerializedName
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.domain.entities.ActivityEntity

data class FormulirKelompokNonSosialPost(
    @field:SerializedName("name")
    var name: String? = "",
    @field:SerializedName("kps_name")
    var kpsName: String? = "",
    @field:SerializedName("sk")
    var sk: String? = "",
    @field:SerializedName("province")
    var province: String? = "",
    @field:SerializedName("city")
    var city: String? = "",
    @field:SerializedName("district")
    var district: String? = "",
    @field:SerializedName("village")
    var village: String? = "",
    @field:SerializedName("address")
    var address: String? = "",
    @field:SerializedName("leader_name")
    var leaderName: String? = "",
    @field:SerializedName("leader_ktp")
    var leaderKtp: String? = "",
    @field:SerializedName("leader_gender")
    var leaderGender: String? = "",
    @field:SerializedName("leader_phone_number")
    var leaderPhoneNumber: String? = "",
    @field:SerializedName("secretary_name")
    var secretaryName: String? = "",
    @field:SerializedName("secretary_phone_number")
    var secretaryGender: String? = "",
    @field:SerializedName("secretary_gender")
    var secretaryPhoneNumber: String? = "",
    @field:SerializedName("treasurer_name")
    var treasurerName: String? = "",
    @field:SerializedName("treasurer_phone_number")
    var treasurerPhoneNumber: String? = "",
    @field:SerializedName("treasurer_gender")
    var treasurerGender: String? = "",
    @field:SerializedName("companion_name")
    var companionName: String? = "",
    @field:SerializedName("companion_status")
    var companionStatus: String? = "",
    @field:SerializedName("companion_phone_number")
    var companionPhoneNumber: String? = "",
    @field:SerializedName("companion_gender")
    var companionGender: String? = "",
    @field:SerializedName("administration_deadline_date")
    var administrationDeadlineDate: String? = "",
    @field:SerializedName("amount_of_member")
    var amountOfMember: Number? = 0,
    @field:SerializedName("amount_of_member_land")
    var amountOfMemberLand: Number? = 0,
    @field:SerializedName("amount_of_member_land_area")
    var amountOfMemberLandArea: Number? = 0,
    @field:SerializedName("amount_of_member_submit")
    var amountOfMemberSubmit: Number? = 0,
    @field:SerializedName("amount_of_member_submit_land")
    var amountOfMemberSubmitLand: Number? = 0,
    @field:SerializedName("amount_of_member_submit_land_area")
    var amountOfMemberSubmitLandArea: Number? = 0,
    @field:SerializedName("created_in")
    var createdIn: String? = "",
    @field:SerializedName("sk_file")
    var skFile: String? = null,
    @field:SerializedName("ad_file")
    var adFile: String?  = null,
    @field:SerializedName("companion_recomendation_file")
    var companionRecomendationFile: String? = null,
    var listActivities: List<ActivityEntity>? = null,
    var listMitraUsaha: List<ActivityEntity>? = null,
    var listGambaranUmum: List<ActivityEntity>? = null,
)
