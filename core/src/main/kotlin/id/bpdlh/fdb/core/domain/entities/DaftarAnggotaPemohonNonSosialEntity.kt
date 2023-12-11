package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DaftarAnggotaPemohonNonSosialEntity(
    val tanggalPengajuanAnggota: String? = null,
    val totalAnggotaDiajukan: Int? = null,
    val totalNilaipermohonan: Int? = null,
    val tanggalDisetujui: String? = null,
    val daftarAnggota: List<DataDebiturNonSosialEntity>? = null
): Parcelable
