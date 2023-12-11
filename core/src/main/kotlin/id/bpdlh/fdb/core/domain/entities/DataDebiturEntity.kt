package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Created by hahn on 23/09/23.
 * Project: BPDLH App
 */

//TODO: pindahin ke package local/entity
@Entity(tableName = "data_debitur")
@Parcelize
data class DataDebiturEntity(
    @PrimaryKey
    val userId: String = UUID.randomUUID().toString(), //id from table
    val id: String? = null, //id from api,
    val memberApplicationId: String? = null,
    val nik: String? = null,
    val nama: String? = null,
    val tanggalLahir: String? = null,
    val email: String? = null,
    val noTelp: String? = null,
    val tanggalDisetujui: String? = null,
    val disetujuiOleh: String? = null
): Parcelable
