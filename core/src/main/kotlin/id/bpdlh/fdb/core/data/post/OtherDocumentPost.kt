package id.bpdlh.fdb.core.data.post

import com.google.gson.annotations.SerializedName

data class OtherDocumentPost(
    @field:SerializedName("user_id")
    var userId: String? = null,
    @field:SerializedName("name")
    var name: String? = null,
    @field:SerializedName("description")
    var description: String? = null,
    @field:SerializedName("file")
    var file: String? = "",
)