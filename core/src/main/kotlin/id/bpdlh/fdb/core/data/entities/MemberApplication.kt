package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 13/10/23.
 * Project: BPDLH App
 */

@Parcelize
data class MemberApplication(
    @field:SerializedName("_id")
    val memberId: String? = null,
    @field:SerializedName("request_date")
    val requestDate: String? = null,
    @field:SerializedName("total_member")
    val totalMember: Int? = null,
    @field:SerializedName("total_member_approved")
    val totalMemberApproved: Int? = null,
    @field:SerializedName("total_member_rejected")
    val totalMemberRejected: Int? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("user_id")
    val userId: String? = null,
    @field:SerializedName("group_profile")
    val groupProfile: GroupProfileUser,
    @field:SerializedName("user")
    val user: User? = null
): Parcelable
