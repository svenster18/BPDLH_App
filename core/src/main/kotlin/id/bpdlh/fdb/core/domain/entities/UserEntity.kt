package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 19/10/23.
 * Project: BPDLH App
 */
@Parcelize
data class UserEntity(
    val userId: String? = null,
    val email: String? = null,
    val role: String? = null
): Parcelable
