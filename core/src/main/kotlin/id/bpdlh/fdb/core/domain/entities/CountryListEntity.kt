package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryListEntity(
    val data: List<String>,
    val maxData: Int,
    val maxPage: Int
) : Parcelable
