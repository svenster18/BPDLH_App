package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 12/10/23.
 * Project: BPDLH App
 */

@Parcelize
data class User(
    @field:SerializedName("_id") val id: String? = null,
    @field:SerializedName("email") val email: String? = null,
    @field:SerializedName("role") val role: String? = null,
): Parcelable
