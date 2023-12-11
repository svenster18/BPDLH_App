package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OtherDocumentEntity(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val file: String? = null,
    val memberApplicationDetailDraftId: String? = null,
    val createdAt: String? = null,
    val fileUrl: String? = null
) : Parcelable
