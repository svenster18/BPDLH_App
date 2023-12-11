package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 05/11/23.
 * Project: BPDLH App
 */
@Parcelize
data class DaftarPermohonanSourceApi(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("request_date")
    val requestDate: String? = null,
    @field:SerializedName("total_member")
    val totalMember: Int? = 0,
    @field:SerializedName("total_member_approved")
    val totalMemberApproved: Int? = null,
    @field:SerializedName("total_member_rejected")
    val totalMemberRejected: Int? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("created_at")
    val createdAt: String? = null,
    @field:SerializedName("user")
    val user: User? = null
): Parcelable
