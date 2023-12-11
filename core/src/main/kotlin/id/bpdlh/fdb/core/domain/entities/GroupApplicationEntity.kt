package id.bpdlh.fdb.core.domain.entities

import com.google.gson.annotations.SerializedName

data class GroupApplicationEntity(
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("_id")
    val _id: String?,
    val requestDate: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("total_member")
    val totalMember: Int? = null,
    @SerializedName("total_member_approved")
    val totalMemberApproved: Int?,
    @SerializedName("total_member_rejected")
    val totalMemberRejected: Int?,
    @SerializedName("user_id")
    val userId: String?
)