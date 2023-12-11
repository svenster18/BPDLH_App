package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 05/10/23.
 * Project: BPDLH App
 *
 */

@Parcelize
data class RegistrasiKelompokEntity(
    val userID: String? = null,
    val email: String? = null,
    val status: String? = null,
    val role: String? = null
): Parcelable
