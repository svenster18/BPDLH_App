package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 17/11/23.
 * Project: BPDLH App
 */

@Parcelize
data class DataAlamatEntity(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("name")
    val name: String? = null
): Parcelable {
    override fun toString(): String {
        return name.orEmpty()
    }
}
