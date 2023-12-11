package id.bpdlh.fdb.core.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by hahn on 16/11/23.
 * Project: BPDLH App
 */

data class DataAlamatSourceApi(
    @field:SerializedName("_id")
    val id: String? = null,
    @field:SerializedName("name")
    val name: String? = null
)
