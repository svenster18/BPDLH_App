package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.data.common.FormulirPendaftaranKelompokRepoContract
import id.bpdlh.fdb.core.data.entities.AfiliasiPendampingApi
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.FungsiKawasanSourceApi
import id.bpdlh.fdb.core.data.entities.GroupProfileDataSourceApi
import id.bpdlh.fdb.core.data.entities.KategoriKegiatanApi
import id.bpdlh.fdb.core.data.sources.remote.FormulirPendaftaranKelompokApi
import id.bpdlh.fdb.core.data.sources.remote.MasterDataApi
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import io.reactivex.Single
import okhttp3.MultipartBody

/**
 * Created by hahn on 21/10/23.
 * Project: BPDLH App
 */
class FormulirPendaftaranKelompokRepository(
    private val api: FormulirPendaftaranKelompokApi,
    private val masterApi: MasterDataApi): FormulirPendaftaranKelompokRepoContract {

    override fun saveDraft(userId: String, body: GroupProfileEntity): Single<BaseDataSourceApi<GroupProfileDataSourceApi>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (!body.name.isNullOrEmpty()) data.addFormDataPart("name", body.name)
        if (!body.sk.isNullOrEmpty()) data.addFormDataPart("sk", body.sk)
        if (!body.kupsName.isNullOrEmpty()) data.addFormDataPart("kups_name", body.kupsName)
        if (!body.kupsSk.isNullOrEmpty()) data.addFormDataPart("kups_sk", body.kupsSk)
        if (!body.establishmentDate.isNullOrEmpty()) data.addFormDataPart("establishment_date", body.establishmentDate)
        if (!body.functionality.isNullOrEmpty()) data.addFormDataPart("functionality", body.functionality)
        if (!body.province.isNullOrEmpty()) data.addFormDataPart("province", body.province)
        if (!body.city.isNullOrEmpty()) data.addFormDataPart("city", body.city)
        if (!body.district.isNullOrEmpty()) data.addFormDataPart("district", body.district)
        if (!body.village.isNullOrEmpty()) data.addFormDataPart("village", body.village)
        if (!body.address.isNullOrEmpty()) data.addFormDataPart("address", body.address)
        if (!body.leaderName.isNullOrEmpty()) data.addFormDataPart("leader_name", body.leaderName)
        if (!body.leaderKtp.isNullOrEmpty()) data.addFormDataPart("leader_ktp", body.leaderKtp)
        if (!body.leaderPhoneNumber.isNullOrEmpty()) data.addFormDataPart("leader_phone_number", body.leaderPhoneNumber)
        if (!body.secretaryName.isNullOrEmpty()) data.addFormDataPart("secretary_name", body.secretaryName)
        if (!body.secretaryPhoneNumber.isNullOrEmpty()) data.addFormDataPart("secretary_phone_number", body.secretaryPhoneNumber)
        if (!body.treasurerName.isNullOrEmpty()) data.addFormDataPart("treasurer_name", body.treasurerName)
        if (!body.treasurerPhoneNumber.isNullOrEmpty()) data.addFormDataPart("treasurer_phone_number", body.treasurerPhoneNumber)
        if (!body.companionName.isNullOrEmpty()) data.addFormDataPart("companion_name", body.companionName)
        if (!body.companionPhoneNumber.isNullOrEmpty()) data.addFormDataPart("companion_phone_number", body.companionPhoneNumber)
        if (!body.companionAffiliate.isNullOrEmpty()) data.addFormDataPart("companion_affiliate", body.companionAffiliate)
        if (!body.administrationDeadlineDate.isNullOrEmpty()) data.addFormDataPart("administration_deadline_date", body.administrationDeadlineDate)
        if (body.amountOfMember != 0) data.addFormDataPart("amount_of_member", body.amountOfMember.toString())
        if (body.amountOfMemberSubmit != 0) data.addFormDataPart("amount_of_member_submit", body.amountOfMemberSubmit.toString())
        if (body.amountOfMemberSubmitLand != 0) data.addFormDataPart("amount_of_member_submit_land", body.amountOfMemberSubmitLand.toString())
        if (body.amountOfMemberSubmitLandArea != 0) data.addFormDataPart("amount_of_member_submit_land_area", body.amountOfMemberSubmitLandArea.toString())
        if (!body.createdIn.isNullOrEmpty()) data.addFormDataPart("created_in", body.createdIn)

        if (!body.listActivityEntity.isNullOrEmpty()) {
            body.listActivityEntity.forEachIndexed { index, activity ->
                data.addFormDataPart("activity[$index][category]", activity.category.orEmpty())
                    .addFormDataPart("activity[$index][description]", activity.description.orEmpty())
            }
        }
        if (!body.listGeneralDescription.isNullOrEmpty()) {
            body.listGeneralDescription.forEachIndexed { index, generalDescriptionEntity ->
                data.addFormDataPart("general_description[$index][category]", generalDescriptionEntity.category.orEmpty())
                    .addFormDataPart("general_description[$index][description]", generalDescriptionEntity.description.orEmpty())
            }
        }
        if (!body.listPartnerName.isNullOrEmpty()) {
            body.listPartnerName.forEachIndexed { index, partner ->
                data.addFormDataPart("partner[$index][name]", partner.category.orEmpty())
            }
        }
        return api.saveDraft(userId, data.build())
    }

    override fun submit(
        userId: String,
        body: GroupProfileEntity
    ): Single<BaseDataSourceApi<GroupProfileDataSourceApi>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("name", body.name.orEmpty())
            .addFormDataPart("sk", body.sk.orEmpty())
            .addFormDataPart("kups_name", body.kupsName.orEmpty())
            .addFormDataPart("kups_sk", body.kupsSk.orEmpty())
            .addFormDataPart("establishment_date", body.establishmentDate.orEmpty())
            .addFormDataPart("functionality", body.functionality.orEmpty())
            .addFormDataPart("province", body.province.orEmpty())
            .addFormDataPart("city", body.city.orEmpty())
            .addFormDataPart("district", body.district.orEmpty())
            .addFormDataPart("village", body.village.orEmpty())
            .addFormDataPart("address", body.address.orEmpty())
            .addFormDataPart("leader_name", body.leaderName ?: "-")
            .addFormDataPart("leader_ktp", body.leaderKtp.orEmpty())
            .addFormDataPart("leader_phone_number", body.leaderPhoneNumber.orEmpty())
            .addFormDataPart("secretary_name", body.secretaryName.orEmpty())
            .addFormDataPart("secretary_phone_number", body.secretaryPhoneNumber.orEmpty())
            .addFormDataPart("treasurer_name", body.treasurerName.orEmpty())
            .addFormDataPart("treasurer_phone_number", body.treasurerPhoneNumber.orEmpty())
            .addFormDataPart("companion_name", body.companionName.orEmpty())
            .addFormDataPart("companion_phone_number", body.companionPhoneNumber.orEmpty())
            .addFormDataPart("companion_affiliate", body.companionAffiliate.orEmpty())
            .addFormDataPart("administration_deadline_date", body.administrationDeadlineDate.orEmpty())
            .addFormDataPart("amount_of_member", body.amountOfMember.toString())
            .addFormDataPart("amount_of_member_submit", body.amountOfMemberSubmit.toString())
            .addFormDataPart("amount_of_member_submit_land", body.amountOfMemberSubmitLand.toString())
            .addFormDataPart("amount_of_member_submit_land_area", body.amountOfMemberSubmitLandArea.toString())
            .addFormDataPart("created_in", body.createdIn.orEmpty())

        if (body.listActivityEntity.isNullOrEmpty()) {
            data.addFormDataPart("activity[0][category]", "-")
                .addFormDataPart("activity[0][description]", "-")
        } else {
            body.listActivityEntity.forEachIndexed { index, activity ->
                data.addFormDataPart("activity[$index][category]", activity.category.orEmpty())
                    .addFormDataPart("activity[$index][description]", activity.description.orEmpty())
            }
        }
        if (body.listGeneralDescription.isNullOrEmpty()) {
            data.addFormDataPart("general_description[0][category]", "-")
                .addFormDataPart("general_description[0][description]", "-")
        } else {
            body.listGeneralDescription.forEachIndexed { index, generalDescriptionEntity ->
                data.addFormDataPart("general_description[$index][category]", generalDescriptionEntity.category.orEmpty())
                    .addFormDataPart("general_description[$index][description]", generalDescriptionEntity.description.orEmpty())
            }
        }
        if (body.listPartnerName.isNullOrEmpty()) {
            data.addFormDataPart("partner[0][name]", "")
        } else {
            body.listPartnerName.forEachIndexed { index, s ->
                data.addFormDataPart("partner[$index][name]", s.category.orEmpty())
            }
        }

        return api.submit(userId, data.build())
    }

    override fun fetchFungsiKawasan(): Single<BaseDataSourceApi<List<FungsiKawasanSourceApi>>> {
        return masterApi.fetchFungsiKawasan()
    }

    override fun fetchAfiliasiPendamping(): Single<BaseDataSourceApi<List<AfiliasiPendampingApi>>> {
        return masterApi.fetchAfiliasiPendamping()
    }

    override fun fetchKategoriKegiatan(): Single<BaseDataSourceApi<List<KategoriKegiatanApi>>> {
        return masterApi.fetchKategoriKegiatan()
    }

    override fun fetchKategoriUsahaDibiayai(): Single<BaseDataSourceApi<List<KategoriKegiatanApi>>> {
        return masterApi.fetchKategoriUsahaDibiayai()
    }
}