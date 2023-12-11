package id.bpdlh.fdb.core.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 20/11/23.
 * Project: BPDLH App
 */
@Parcelize
data class FungsiKawasanSourceApi(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("description")
    val description: String? = null
): Parcelable
