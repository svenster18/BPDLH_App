package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusinessPartnerSourceApi(

    @field:SerializedName("member_application_detail_id")
    val memberApplicationDetailId: String? = null,

    @field:SerializedName("member_application_detail")
    val memberApplicationDetail: MemberApplicationDetail? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("_id")
    val id: String? = null
) : Parcelable
