package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 19/10/23.
 * Project: BPDLH App
 */
@Parcelize
data class ActivityEntity(
    val id: String? = null,
    val category: String? = null,
    val description: String? = null
): Parcelable
