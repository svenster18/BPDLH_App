package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "formulir_pembiayaan_perhutanan_sosial")
@Parcelize
data class FormulirPembiayaanPerhutananSosial(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var perkiraanPendapatan: Long = 0L,
    var perkiraanPendapatanLainnya: Long = 0L
) : BaseData(), Parcelable
