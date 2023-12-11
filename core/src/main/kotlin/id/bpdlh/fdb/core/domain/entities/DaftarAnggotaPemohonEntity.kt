package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 27/09/23.
 * Project: BPDLH App
 */
@Parcelize
data class DaftarAnggotaPemohonEntity(
    val tanggalPengajuanAnggota: String? = null,
    val totalAnggotaDiajukan: Int? = null,
    val tanggalDisetujui: String? = null,
    val daftarAnggota: List<DataDebiturEntity>? = null
): Parcelable
