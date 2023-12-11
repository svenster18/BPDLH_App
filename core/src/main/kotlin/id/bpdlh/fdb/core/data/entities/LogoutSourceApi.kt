package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName


data class LogoutSourceApi(
    @SerializedName("logout")
    val logout: Boolean?,
)