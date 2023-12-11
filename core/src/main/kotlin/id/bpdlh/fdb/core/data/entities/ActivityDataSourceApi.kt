package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by hahn on 22/10/23.
 * Project: BPDLH App
 */
data class ActivityDataSourceApi(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("group_profile_draft_id")
    val groupProfileDraftId: String? = null,
    @field:SerializedName("category", alternate = ["name"])
    val category: String? = null,
    @field:SerializedName("description")
    val description: String? = null
)
