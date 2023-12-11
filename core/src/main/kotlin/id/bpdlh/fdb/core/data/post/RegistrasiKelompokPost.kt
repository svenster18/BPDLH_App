package id.bpdlh.fdb.core.data.post

import com.google.gson.annotations.SerializedName

/**
 * Created by hahn on 08/10/23.
 * Project: BPDLH App
 */

data class RegistrasiKelompokPost(
    @field:SerializedName("name")
    var name: String? = "",
    @field:SerializedName("type")
    var type: String? = "",
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
    @field:SerializedName("contact_person_name")
    var contactPersonName: String? = "",
    @field:SerializedName("contact_person_ktp")
    var contactPersonKtp: String? = "",
    @field:SerializedName("contact_person_position")
    var contactPersonPosition: String? = "",
    @field:SerializedName("contact_person_phone_number")
    var contactPersonPhoneNumber: String? = "",
    @field:SerializedName("contact_person_email")
    var contactPersonEmail: String? = "",
    @field:SerializedName("email")
    var email: String? = "",
    @field:SerializedName("")
    var password: String? = "",
    @field:SerializedName("password_confirmation")
    var passwordConfirmation: String? = ""
)
