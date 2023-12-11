package id.bpdlh.fdb.core.domain.entities

import android.os.Parcelable
import id.bpdlh.fdb.core.data.entities.GroupProfileUser
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import kotlinx.parcelize.Parcelize

/**
 * Created by hahn on 12/10/23.
 * Project: BPDLH App
 */
@Parcelize
data class GroupProfileEntity(
    val userID: String? = null,
    val email: String? = null,
    val status: String? = null,
    val role: String? = null,
    val isSubmittable: Boolean = false,
    val isSubmitted: Boolean = false,
    val name: String? = null,
    val sk: String? = null,
    val type: String? = null,
    val establishmentDate: String? = null,
    val functionality: String? = null,
    val kupsName: String? = null,
    val kupsSk: String? = null,
    val leaderName: String? = null,
    val leaderKtp: String? = null,
    val leaderPhoneNumber: String? = null,
    val leaderGender: String? = null,
    val secretaryName: String? = null,
    val secretaryPhoneNumber: String? = null,
    val secretaryGender: String? = null,
    val treasurerName: String? = null,
    val treasurerPhoneNumber: String? = null,
    val treasurerGender: String? = null,
    val companionName: String? = null,
    val companionPhoneNumber: String? = null,
    val companionAffiliate: String? = null,
    val companionGender: String? = null,
    val administrationDeadlineDate: String? = null,
    val amountOfMember: Int? = null,
    val amountOfMemberLand: Int? = null,
    val amountOfMemberLandArea: Int? = null,
    val amountOfMemberSubmit: Int? = null,
    val amountOfMemberSubmitLand: Int? = null,
    val amountOfMemberSubmitLandArea: Int? = null,
    val province: String? = null,
    val city: String? = null,
    val district: String? = null,
    val village: String? = null,
    val address: String? = null,
    val contactPersonName: String? = null,
    val contactPersonKtp: String? = null,
    val contactPersonPosition: String? = null,
    val contactPersonPhoneNumber: String? = null,
    val contactPersonEmail: String? = null,
    val listActivityEntity: List<ActivityEntity>? = null,
    val listPartnerName: List<ActivityEntity>? = null,
    val listGeneralDescription: List<ActivityEntity>? = null,
    val userEntity: UserEntity? = null,
    val createdAt: String? = null,
    val createdIn: String? = null,
    val skFile: String? = null,
    val adFile: String? = null,
    val companionRecomendationFile: String? = null,
): Parcelable
