package id.bpdlh.fdb.core.data.sources.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import id.bpdlh.fdb.core.domain.entities.UsahaLainEntity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "registrasi_perorangan")
@Parcelize
data class RegistrasiPerorangan(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var penggunaanTerbesar: String = "",
    var dokumenPendukungPath: String = "",
    var swafotoPath: String = "",
    var fotoRumahPath: String = "",
    var fotoUsahaPath: String = "",
    var ktpFile: String = "",
    var kkFile: String = "",
    var coupleKtpFile: String = "",
    var swaphotoKtpFile: String = "",
    var housePhotoFile: String = "",
    var businessPhotoFile: String = "",
    var jenisKelamin: String = "",
    @Ignore
    var otherBusiness: List<UsahaLainEntity> = emptyList(),
    var groupName: String = "",
) : Parcelable, BaseData()
