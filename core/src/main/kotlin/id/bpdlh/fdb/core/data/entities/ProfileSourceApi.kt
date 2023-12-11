package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

data class ProfileSourceApi(
    @SerializedName("email")
    val email: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("usermetadata")
    val userMetaData: UserMetaDataSourceApi?,
    @SerializedName("userType")
    val userType: String?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("roles")
    val roles: List<String?>?,
)

data class UserMetaDataSourceApi(
    @SerializedName("name")
    val name: String?,
    @SerializedName("clientId")
    val clientId: String?,
    @SerializedName("warehouseID")
    val warehouseID: String?
)