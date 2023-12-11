package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "file")
@Parcelize
data class FileEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var id_temp_api: String = "",
    var nama: String = "",
    var path: String = "",
    var parent_id: Int = 1,
    var type: Int = 0
) : Parcelable
