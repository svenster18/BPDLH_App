package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 20/11/23.
 * Project: BPDLH App
 */
@Parcelize
data class FungsiKawasanEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null
): Parcelable
