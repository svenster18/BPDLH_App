package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupProfileDraftEntity(
    val groupProfileNonSosial: GroupProfileNonSosial
): Parcelable
