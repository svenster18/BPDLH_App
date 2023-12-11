package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsahaLainEntity(
    val jenis: String,
    val perkiraanPendapatan: Long,
    val siklusPendapatan: Int,
    val satuanSiklusPendapatan: String
) : Parcelable
