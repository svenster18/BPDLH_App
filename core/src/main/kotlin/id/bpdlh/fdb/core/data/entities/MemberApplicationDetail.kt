package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 13/10/23.
 * Project: BPDLH App
 */
@Parcelize
data class MemberApplicationDetail(
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("ktp")
    val ktp: String? = null,
    @field:SerializedName("date_of_birth")
    val dateOfBirth: String? = null,
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,
    @field:SerializedName("kk")
    val kk: String? = null,
    @field:SerializedName("couple_name")
    val coupleName: String? = null,
    @field:SerializedName("couple_ktp")
    val coupleKtp: String? = null,
    @field:SerializedName("couple_date_of_birth")
    val coupleDateOfBirth: String? = null,
    @field:SerializedName("ktp_province")
    val ktProvince: String? = null,
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
    val amountOfLoan: Long,
    @field:SerializedName("is_submittable")
    val isSubmittable: Boolean? = false,
    @field:SerializedName("is_submitted")
    val isSubmitted: String? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("user_id")
    val userId: String? = null,
    @field:SerializedName("member_application_id")
    val memberApplicationId: String? = null,
    @field:SerializedName("member_application")
    val memberApplication: MemberApplication? = null,
    @field:SerializedName("user")
    val user: User? = null
): Parcelable
