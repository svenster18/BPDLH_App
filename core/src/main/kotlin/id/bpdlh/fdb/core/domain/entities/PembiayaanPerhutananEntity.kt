package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import id.bpdlh.fdb.core.data.post.PermohonanPembiayaanPerhutananSosialPost
import kotlinx.parcelize.Parcelize

@Parcelize
data class PembiayaanPerhutananEntity(
    val id: String,
    val date: String,
    val requestDate: String,
    val status: Int,
    val amountOfLoan: Long = 0L,
    val serviceType: Int = 0,
    val memberApplication: PermohonanPembiayaanPerhutananSosialPost? = null,
) : Parcelable
