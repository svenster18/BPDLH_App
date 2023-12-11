package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 21/11/23.
 * Project: BPDLH App
 */
@Parcelize
data class KategoriKegiatanEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null
): Parcelable {
    override fun toString(): String {
        return name.toString()
    }
}
