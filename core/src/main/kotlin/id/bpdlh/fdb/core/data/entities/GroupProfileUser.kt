package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 12/10/23.
 * Project: BPDLH App
 */

@Parcelize
data class GroupProfileUser(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("type")
    val type: String? = null,
    @field:SerializedName("sk")
    val sk: String? = null,
    @field:SerializedName("kups_name")
    val kupsName: String? = null,
    @field:SerializedName("kups_sk")
    val kupsSk: String? = null,
    @field:SerializedName("establishment_date")
    val establishmentDate: String? = null,
    @field:SerializedName("administration_deadline_date")
    val administrationDeadlineDate: String? = null,
    @field:SerializedName("amount_of_member")
    val amountOfMember: Int? = null,
    @field:SerializedName("amount_of_member_submit_land")
    val amountOfMemberSubmitLand: Int? = null,
    @field:SerializedName("amount_of_member_submit_land_area")
    val amountOfMemberSubmitLandArea: Int? = null,
    @field:SerializedName("created_in")
    val createdIn: String? = null,
    @field:SerializedName("province")
    val province: String? = null,
    @field:SerializedName("city")
    val city: String? = null,
    @field:SerializedName("district")
    val district: String? = null,
    @field:SerializedName("village")
    val village: String? = null,
    @field:SerializedName("address")
    val address: String? = null,
    @field:SerializedName("contact_person_name")
    val contactPersonName: String? = null,
    @field:SerializedName("contact_person_ktp")
    val contactPersonKtp: String? = null,
    @field:SerializedName("contact_person_position")
    val contactPersonPosition: String? = null,
    @field:SerializedName("contact_person_phone_number")
    val contactPersonPhoneNumber: String? = null,
    @field:SerializedName("contact_person_email")
    val contactPersonEmail: String? = null,
    @field:SerializedName("leader_name")
    val leaderName: String? = null,
    @field:SerializedName("leader_ktp")
    val leaderKtp: String? = null,
    @field:SerializedName("leader_phone_number")
    val leaderPhoneNumber: String? = null,
    @field:SerializedName("secretary_name")
    val secretaryName: String? = null,
    @field:SerializedName("secretary_phone_number")
    val secretaryPhoneNumber: String? = null,
    @field:SerializedName("treasurer_name")
    val treasurerName: String? = null,
    @field:SerializedName("treasurer_phone_number")
    val treasurerPhoneNumber: String? = null,
    @field:SerializedName("companion_name")
    val companionName: String? = null,
    @field:SerializedName("companion_phone_number")
    val companionPhoneNumber: String? = null,
    @field:SerializedName("is_submittable")
    val isSubmittable: Boolean? = false,
    @field:SerializedName("is_submitted")
    val isSubmitted: Boolean? = false,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("user_id")
    val userId: String? = null,
    @SerializedName("user") val user: User? = null
): Parcelable
