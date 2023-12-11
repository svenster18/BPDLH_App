package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BaseDataSourceApi<T>(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("statusCode")
    val statusCode: Int?,
    @SerializedName("meta")
    val meta: Meta?,
    @SerializedName("status")
    val status: Boolean?,
)

@Parcelize
data class Meta(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("dataLength")
    val dataLength: Int?,
    @SerializedName("MaxData")
    val maxData: Int?,
    @SerializedName("MaxPage")
    val maxPage: Int?
) : Parcelable
