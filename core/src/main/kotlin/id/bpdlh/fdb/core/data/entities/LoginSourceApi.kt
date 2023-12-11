package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

data class LoginSourceApi(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("expiration")
    val expiration: String?,
    @field:SerializedName("_id")
    val userID: String? = null,
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("role")
    val role: String? = null,
)