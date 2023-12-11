package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "kegiatan")
@Parcelize
data class Kegiatan(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val kategori: String,
    val deskripsi: String,
    var type: Int
): Parcelable