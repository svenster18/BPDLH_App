package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "mitra")
@Parcelize
data class Mitra(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nama: String = "",
    var formulir_id: Int = 1
) : Parcelable