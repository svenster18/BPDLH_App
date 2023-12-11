package id.bpdlh.fdb.core.data.post

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GroupApplicationCreatePost(
    @field:SerializedName("user_id")
    var userId: String,
    var groupApplicationMemberPost: MutableList<GroupApplicationMemberPost> = mutableListOf()
)
@Parcelize
data class GroupApplicationMemberPost(
    @field:SerializedName("service_type")
    var serviceType: String? = "",
    @field:SerializedName("name")
    var name: String,
    @field:SerializedName("email")
    var email: String,
    @field:SerializedName("ktp")
    var ktp: String,
    @field:SerializedName("date_of_birth")
    var dateOfBirth: String,
    @field:SerializedName("phone_number")
    var phoneNumber: String,
) : Parcelable
