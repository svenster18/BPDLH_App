package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 05/10/23.
 * Project: BPDLH App
 */
@Parcelize
data class AlamatEntity(
    var provinsi: String? = "",
    var kabKota: String? = "",
    var kecamatan: String? = "",
    var kelurahan: String? = "",
    var rw: String? = "",
    var rt: String? = "",
    var alamatLengkap: String? = ""
): Parcelable
