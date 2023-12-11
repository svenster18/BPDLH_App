package id.bpdlh.fdb.core.data.post

import com.google.gson.annotations.SerializedName

data class RequestForgotPasswordPost(
    @SerializedName("email")
    val email: String,
    @SerializedName("service")
    val service: String,
)

data class VerifyForgotPasswordPost(
    @SerializedName("token")
    val token: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("retypeNewPassword")
    val retypeNewPassword: String,
)