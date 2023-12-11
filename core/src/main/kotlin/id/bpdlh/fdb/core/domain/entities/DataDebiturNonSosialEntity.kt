package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataDebiturNonSosialEntity(
    val id: String? = null,
    val namaKelompok: String? = null,
    val nik: String? = null,
    val nama: String? = null,
    val tanggalLahir: String? = null,
    val email: String? = null,
    val noTelp: String? = null,
    val jenisLayanan: String? = null,
    val nilaiPermohonan: Int? = null,
    val tanggalDisetujui: String? = null,
    val disetujuiOleh: String? = null,
    val userId: String? = null,
    val gender: String? = null,
    val submissionPurpose: String? = null,
    val detailSubmissionPurpose: String? = null,
    val memberApplicationId: String? = null,
    val isFinancingSubmittable: Boolean? = null,
    val isSend: Boolean? = false
): Parcelable
