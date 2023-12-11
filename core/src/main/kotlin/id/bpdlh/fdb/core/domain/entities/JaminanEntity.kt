package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "jaminan")
@Parcelize
data class JaminanEntity(
    val bentuk: String,
    val nilai: Long,
    val photoPath: String,
    var formulir_id: Int = 1,
    var photoFileId: String = "",
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Parcelable