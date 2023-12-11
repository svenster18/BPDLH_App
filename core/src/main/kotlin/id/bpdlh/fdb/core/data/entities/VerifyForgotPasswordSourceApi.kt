package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

data class VerifyForgotPasswordSourceApi(
    @SerializedName("email")
    val email: String?,
)