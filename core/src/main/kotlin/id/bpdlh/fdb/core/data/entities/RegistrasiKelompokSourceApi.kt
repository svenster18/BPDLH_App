package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by hahn on 08/10/23.
 * Project: BPDLH App
 */
data class RegistrasiKelompokSourceApi(
    @field:SerializedName("status") val status: String? = null,
    @field:SerializedName("group_profile") val groupProfile: GroupProfileUser? = null
)