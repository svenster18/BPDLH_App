package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "group_profile_non_sosial")
@Parcelize
data class GroupProfileNonSosial(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var idTabel : Int? = null,
    @SerializedName("_id")
    var Id : String? = null,
    @SerializedName("name")
    var name : String? = null,
    @SerializedName("type")
    var type : String? = null,
    @SerializedName("sk")
    var sk : String? = null,
    @SerializedName("establishment_date")
    var establishmentDate : String? = null,
    @SerializedName("administration_deadline_date")
    var administrationDeadlineDate : String? = null,
    @SerializedName("amount_of_member")
    var amountOfMember : Int? = null,
    @SerializedName("amount_of_member_land")
    var amountOfMemberLand : Int? = null,
    @SerializedName("amount_of_member_land_area")
    var amountOfMemberLandArea : Int? = null,
    @SerializedName("amount_of_member_submit")
    var amountOfMemberSubmit : Int? = null,
    @SerializedName("amount_of_member_submit_land")
    var amountOfMemberSubmitLand : Int? = null,
    @SerializedName("amount_of_member_submit_land_area")
    var amountOfMemberSubmitLandArea : Int? = null,
    @SerializedName("nominal_submit")
    var nominalSubmit : String? = null,
    @SerializedName("province")
    var province : String? = null,
    @SerializedName("city")
    var city : String? = null,
    @SerializedName("district")
    var district : String? = null,
    @SerializedName("village")
    var village : String? = null,
    @SerializedName("address")
    var address : String? = null,
    @SerializedName("contact_person_name")
    var contactPersonName : String? = null,
    @SerializedName("contact_person_ktp")
    var contactPersonKtp : String? = null,
    @SerializedName("contact_person_position")
    var contactPersonPosition : String? = null,
    @SerializedName("contact_person_phone_number")
    var contactPersonPhoneNumber : String? = null,
    @SerializedName("contact_person_email")
    var contactPersonEmail : String? = null,
    @SerializedName("kups_name")
    var kupsName : String? = null,
    @SerializedName("kups_sk")
    var kupsSk : String? = null,
    @SerializedName("leader_name")
    var leaderName : String? = null,
    @SerializedName("leader_ktp")
    var leaderKtp : String? = null,
    @SerializedName("leader_phone_number")
    var leaderPhoneNumber : String? = null,
    @SerializedName("secretary_name")
    var secretaryName : String? = null,
    @SerializedName("secretary_phone_number")
    var secretaryPhoneNumber : String? = null,
    @SerializedName("treasurer_name")
    var treasurerName : String? = null,
    @SerializedName("treasurer_phone_number")
    var treasurerPhoneNumber : String? = null,
    @SerializedName("companion_name")
    var companionName : String? = null,
    @SerializedName("companion_affiliate")
    var companionAffiliate : String? = null,
    @SerializedName("companion_status")
    var companionStatus : String? = null,
    @SerializedName("companion_phone_number")
    var companionPhoneNumber : String? = null,
    @SerializedName("sk_file")
    var skFile : String? = null,
    @SerializedName("ad_file")
    var adFile : String? = null,
    @SerializedName("companion_recomendation_file")
    var companionRecomendationFile : String? = null,
    @SerializedName("is_submittable")
    var isSubmittable : Boolean? = null,
    @SerializedName("is_submitted")
    var isSubmitted : Boolean? = null,
    @SerializedName("status")
    var status : String? = null,
    @SerializedName("created_in")
    var createdIn : String? = null,
    @SerializedName("user_id")
    var userId : String? = null,
    @SerializedName("created_at")
    var createdAt : String? = null,
    @SerializedName("sk_file_url")
    var skFileUrl : String? = null,
    @SerializedName("ad_file_url")
    var adFileUrl : String? = null,
    @SerializedName("companion_recomendation_file_url")
    var companionRecomendationFileUrl : String? = null,
): Parcelable