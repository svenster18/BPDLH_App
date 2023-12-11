package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DaftarPemohonNonSosialEntity(
    val id: String,
    val date: String,
    val dataType: Int,
    val totalAnggota: Int
): Parcelable
