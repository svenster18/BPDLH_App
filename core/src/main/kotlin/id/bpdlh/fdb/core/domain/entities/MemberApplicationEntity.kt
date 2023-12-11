package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 13/10/23.
 * Project: BPDLH App
 */
@Parcelize
data class MemberApplicationEntity(
    val userID: String? = null,
    val email: String? = null,
    val status: String? = null,
    val role: String? = null,
    val isSubmittable: Boolean = false,
    val isSubmitted: Boolean = false,
    val serviceType: String? = null,
    val groupName: String? = null,
    val name: String? = null,
    val ktp: String? = null,
    val date_of_birth: String? = null,
    val gender: String? = null,
    val phoneNumber: String? = null
) : Parcelable
