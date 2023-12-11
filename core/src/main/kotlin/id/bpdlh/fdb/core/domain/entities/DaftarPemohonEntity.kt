package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 18/09/23.
 * Project: BPDLH App
 */
@Parcelize
data class DaftarPemohonEntity(
    val id: String,
    val date: String,
    val dataType: Int,
    val status: String? = null,
    val totalAnggota: Int
): Parcelable
