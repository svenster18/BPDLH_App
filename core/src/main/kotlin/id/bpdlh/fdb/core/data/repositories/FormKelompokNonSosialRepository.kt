package id.bpdlh.fdb.core.data.repositories

import id.bpdlh.fdb.core.common.utils.AppExecutors
import id.bpdlh.fdb.core.data.common.FormKelompokNonSosialRepositoryContract
import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.FileServiceSourceApi
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.entities.OtherDocumentDraftSourceApi
import id.bpdlh.fdb.core.data.post.FormulirKelompokNonSosialPost
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import id.bpdlh.fdb.core.data.sources.local.room.FormKelompokNonSosialDao
import id.bpdlh.fdb.core.data.sources.remote.FileServiceApi
import id.bpdlh.fdb.core.data.sources.remote.FormulirKelompokNonSosialApi
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FormKelompokNonSosialRepository(
    private val formKelompokNonSosialDao: FormKelompokNonSosialDao,
    private val appExecutors: AppExecutors,
    private val api: FormulirKelompokNonSosialApi,
    private val fileServiceApi: FileServiceApi
) : FormKelompokNonSosialRepositoryContract {

    override fun getDraft(): Maybe<GroupProfileNonSosial> = formKelompokNonSosialDao.getDraft()

    override fun insert(groupProfileNonSosial: GroupProfileNonSosial) {
        appExecutors.diskIO().execute { formKelompokNonSosialDao.insert(groupProfileNonSosial) }
    }

    override fun update(groupProfileNonSosial: GroupProfileNonSosial) {
        appExecutors.diskIO().execute { formKelompokNonSosialDao.update(groupProfileNonSosial) }
    }

    override fun deleteDraft() {
        appExecutors.diskIO().execute { formKelompokNonSosialDao.delete() }
    }

    override fun postOtherDocument(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>> {
        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", body.userId ?: "")
            .addFormDataPart("name", body.name ?: "")
            .addFormDataPart("description", body.description ?: "")
            .addFormDataPart("file", body.file ?: "")
            .build()
        return api.postOtherDocument(multipart)
    }

    override fun draft(
        userId: String,
        body: FormulirKelompokNonSosialPost
    ): Single<BaseDataSourceApi<GroupProfileNonSosial>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
        if (!body.name.isNullOrEmpty())  data.addFormDataPart("name", body.name ?: "")
        if (!body.sk.isNullOrEmpty())  data.addFormDataPart("sk", body.sk ?: "-")
        if (!body.province.isNullOrEmpty())  data.addFormDataPart("province", body.province ?: "")
        if (!body.city.isNullOrEmpty())  data.addFormDataPart("city", body.city ?: "")
        if (!body.district.isNullOrEmpty())  data.addFormDataPart("district", body.district ?: "")
        if (!body.village.isNullOrEmpty())  data.addFormDataPart("village", body.village ?: "")
        if (!body.address.isNullOrEmpty())  data.addFormDataPart("address", body.address ?: "")
        if (!body.leaderName.isNullOrEmpty())  data.addFormDataPart("leader_name", body.leaderName ?: "--")
        if (!body.leaderKtp.isNullOrEmpty())  data.addFormDataPart("leader_ktp", body.leaderKtp ?: "")
        if (!body.leaderPhoneNumber.isNullOrEmpty())  data.addFormDataPart("leader_phone_number", body.leaderPhoneNumber ?: "-")
        if (!body.leaderGender.isNullOrEmpty())  data.addFormDataPart("leader_gender", body.leaderGender ?: "-")
        if (!body.secretaryName.isNullOrEmpty())  data.addFormDataPart("secretary_name", body.secretaryName ?: "")
        if (!body.secretaryPhoneNumber.isNullOrEmpty())  data.addFormDataPart("secretary_phone_number", body.secretaryPhoneNumber ?: "")
        if (!body.secretaryGender.isNullOrEmpty()) data.addFormDataPart("secretary_gender", body.secretaryGender ?: "")
        if (!body.treasurerName.isNullOrEmpty())  data.addFormDataPart("treasurer_name", body.treasurerName ?: "")
        if (!body.treasurerPhoneNumber.isNullOrEmpty())  data.addFormDataPart("treasurer_phone_number", body.treasurerPhoneNumber ?: "")
        if(!body.treasurerGender.isNullOrEmpty()) data.addFormDataPart("treasurer_gender", body.treasurerGender ?: "")
        if (!body.companionName.isNullOrEmpty())  data.addFormDataPart("companion_name", body.companionName ?: "")
        if (!body.companionStatus.isNullOrEmpty())  data.addFormDataPart("companion_status", body.companionStatus ?: "")
        if (!body.companionPhoneNumber.isNullOrEmpty())  data.addFormDataPart("companion_phone_number", body.companionPhoneNumber ?: "")
        if(!body.companionGender.isNullOrEmpty()) data.addFormDataPart("companion_gender", body.companionGender ?: "")
        if (!body.administrationDeadlineDate.isNullOrEmpty())  data.addFormDataPart("administration_deadline_date", body.administrationDeadlineDate ?: "")
        body.amountOfMember?.let { data.addFormDataPart("amount_of_member", it.toString()) }
        body.amountOfMemberLand?.let { data.addFormDataPart("amount_of_member_land", it.toString()) }
        body.amountOfMemberLandArea?.let {  data.addFormDataPart("amount_of_member_land_area", it.toString()) }
        body.amountOfMemberSubmit?.let { data.addFormDataPart("amount_of_member_submit", it.toString()) }
        body.amountOfMemberSubmitLand?.let { data.addFormDataPart("amount_of_member_submit_land", it.toString()) }
        body.amountOfMemberSubmitLandArea?.let { data.addFormDataPart("amount_of_member_submit_land_area", it.toString()) }
        if (!body.createdIn.isNullOrEmpty())  data.addFormDataPart("created_in", body.createdIn ?: "")
        body.listActivities?.forEachIndexed { index, activity ->
            data.addFormDataPart("activity[$index][category]", activity.category.orEmpty())
            if (!activity.description.isNullOrEmpty()) data.addFormDataPart(
                "activity[$index][description]",
                activity.description
            )
        }
        body.listGambaranUmum?.forEachIndexed { index, generalDescriptionEntity ->
            data.addFormDataPart(
                "general_description[$index][category]",
                generalDescriptionEntity.category.orEmpty()
            )
            data.addFormDataPart(
                "general_description[$index][description]",
                generalDescriptionEntity.description.orEmpty()
            )
        }
        body.listMitraUsaha?.forEachIndexed { index, s ->
            data.addFormDataPart("partner[$index][name]", s.category.orEmpty())

        }
        body.skFile?.let {
            data.addFormDataPart("sk_file", it)
        }
        body.adFile?.let {
            data.addFormDataPart("ad_file", it)
        }
        body.companionRecomendationFile?.let {
            data.addFormDataPart("companion_recomendation_file", it)
        }
        return api.postDraft(userId, data.build())
    }

    override fun update(
        userId: String,
        body: FormulirKelompokNonSosialPost
    ): Single<BaseDataSourceApi<GroupProfileNonSosial>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("name", body.name ?: "")
            .addFormDataPart("sk", body.sk ?: "-")
            .addFormDataPart("province", body.province ?: "")
            .addFormDataPart("city", body.city ?: "")
            .addFormDataPart("district", body.district ?: "")
            .addFormDataPart("village", body.village ?: "")
            .addFormDataPart("address", body.address ?: "")
            .addFormDataPart("leader_name", body.leaderName ?: "--")
            .addFormDataPart("leader_ktp", body.leaderKtp ?: "")
            .addFormDataPart("leader_phone_number", body.leaderPhoneNumber ?: "-")
            .addFormDataPart("leader_gender", body.leaderGender ?: "-")
            .addFormDataPart("secretary_name", body.secretaryName ?: "")
            .addFormDataPart("secretary_phone_number", body.secretaryPhoneNumber ?: "")
            .addFormDataPart("secretary_gender", body.secretaryGender ?: "")
            .addFormDataPart("treasurer_name", body.treasurerName ?: "")
            .addFormDataPart("treasurer_phone_number", body.treasurerPhoneNumber ?: "")
            .addFormDataPart("treasurer_gender", body.treasurerGender ?: "")
            .addFormDataPart("companion_name", body.companionName ?: "")
            .addFormDataPart("companion_status", body.companionStatus ?: "")
            .addFormDataPart("companion_phone_number", body.companionPhoneNumber ?: "")
            .addFormDataPart("companion_gender", body.companionGender ?: "")
            .addFormDataPart("administration_deadline_date", body.administrationDeadlineDate ?: "")
            .addFormDataPart("amount_of_member", body.amountOfMember.toString())
            .addFormDataPart("amount_of_member_land", body.amountOfMemberLand.toString())
            .addFormDataPart(
                "amount_of_member_land_area",
                body.amountOfMemberLandArea.toString()
            )
            .addFormDataPart("amount_of_member_submit", body.amountOfMemberSubmit.toString())
            .addFormDataPart(
                "amount_of_member_submit_land",
                body.amountOfMemberSubmitLand.toString()
            )
            .addFormDataPart(
                "amount_of_member_submit_land_area",
                body.amountOfMemberSubmitLandArea.toString()
            )
            .addFormDataPart("created_in", body.createdIn ?: "")

        body.listActivities?.forEachIndexed { index, activity ->
            data.addFormDataPart("activity[$index][category]", activity.category.orEmpty())
                .addFormDataPart(
                    "activity[$index][description]",
                    activity.description.orEmpty()
                )
        }
        body.listGambaranUmum?.forEachIndexed { index, generalDescriptionEntity ->
            data.addFormDataPart(
                "general_description[$index][category]",
                generalDescriptionEntity.category.orEmpty()
            )
                .addFormDataPart(
                    "general_description[$index][description]",
                    generalDescriptionEntity.description.orEmpty()
                )
        }
        body.listMitraUsaha?.forEachIndexed { index, s ->
            data.addFormDataPart("partner[$index][name]", s.category.orEmpty())

        }
        body.skFile?.let {
            data.addFormDataPart("sk_file", it)
        }
        body.adFile?.let {
            data.addFormDataPart("ad_file", it)
        }
        body.companionRecomendationFile?.let {
            data.addFormDataPart("companion_recomendation_file", it)
        }
        return api.postUpdate(userId, data.build())
    }

    override fun submit(
        userId: String,
        body: FormulirKelompokNonSosialPost
    ): Single<BaseDataSourceApi<GroupProfileNonSosial>> {
        val data = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("name", body.name ?: "")
            .addFormDataPart("sk", body.sk ?: "")
            .addFormDataPart("province", body.province ?: "")
            .addFormDataPart("city", body.city ?: "")
            .addFormDataPart("district", body.district ?: "")
            .addFormDataPart("village", body.village ?: "")
            .addFormDataPart("address", body.address ?: "")
            .addFormDataPart("leader_name", body.leaderName ?: "")
            .addFormDataPart("leader_ktp", body.leaderKtp ?: "")
            .addFormDataPart("leader_phone_number", body.leaderPhoneNumber ?: "")
            .addFormDataPart("leader_gender", body.leaderGender ?: "-")
            .addFormDataPart("secretary_name", body.secretaryName ?: "")
            .addFormDataPart("secretary_phone_number", body.secretaryPhoneNumber ?: "")
            .addFormDataPart("secretary_gender", body.secretaryGender ?: "")
            .addFormDataPart("treasurer_name", body.treasurerName ?: "")
            .addFormDataPart("treasurer_phone_number", body.treasurerPhoneNumber ?: "")
            .addFormDataPart("treasurer_gender", body.treasurerGender ?: "")
            .addFormDataPart("companion_name", body.companionName ?: "")
            .addFormDataPart("companion_status", body.companionStatus ?: "")
            .addFormDataPart("companion_phone_number", body.companionPhoneNumber ?: "")
            .addFormDataPart("companion_gender", body.companionGender ?: "")
            .addFormDataPart("administration_deadline_date", body.administrationDeadlineDate ?: "")
            .addFormDataPart("amount_of_member", body.amountOfMember.toString())
            .addFormDataPart("amount_of_member_land", body.amountOfMemberLand.toString())
            .addFormDataPart(
                "amount_of_member_land_area",
                body.amountOfMemberSubmitLandArea.toString()
            )
            .addFormDataPart("amount_of_member_submit", body.amountOfMemberSubmit.toString())
            .addFormDataPart(
                "amount_of_member_submit_land",
                body.amountOfMemberSubmitLand.toString()
            )
            .addFormDataPart(
                "amount_of_member_submit_land_area",
                body.amountOfMemberSubmitLandArea.toString()
            )
            .addFormDataPart("created_in", body.createdIn ?: "")

        if (body.listActivities.isNullOrEmpty()) {
            data.addFormDataPart("activity[0][category]", "-")
                .addFormDataPart("activity[0][description]", "-")
        } else {
            body.listActivities?.forEachIndexed { index, activity ->
                data.addFormDataPart("activity[$index][category]", activity.category.orEmpty())
                    .addFormDataPart(
                        "activity[$index][description]",
                        activity.description.orEmpty()
                    )
            }
        }
        if (body.listGambaranUmum.isNullOrEmpty()) {
            data.addFormDataPart("general_description[0][category]", "-")
                .addFormDataPart("general_description[0][description]", "-")
        } else {
            body.listGambaranUmum?.forEachIndexed { index, generalDescriptionEntity ->
                data.addFormDataPart(
                    "general_description[$index][category]",
                    generalDescriptionEntity.category.orEmpty()
                )
                    .addFormDataPart(
                        "general_description[$index][description]",
                        generalDescriptionEntity.description.orEmpty()
                    )
            }
        }
        if (body.listMitraUsaha.isNullOrEmpty()) {
            data.addFormDataPart("partner[0][name]", "")
        } else {
            body.listMitraUsaha?.forEachIndexed { index, s ->
                data.addFormDataPart("partner[$index][name]", s.category.orEmpty())

            }
        }
        body.skFile?.let {
            data.addFormDataPart("sk_file", it)
        }
        body.adFile?.let {
            data.addFormDataPart("ad_file", it)
        }
        body.companionRecomendationFile?.let {
            data.addFormDataPart("companion_recomendation_file", it)
        }

        return api.submitForm(userId, data.build())
    }

    override fun postOtherDocumentDraft(body: OtherDocumentPost): Single<BaseDataSourceApi<OtherDocumentDraftSourceApi>> {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", body.userId.orEmpty())
            .addFormDataPart("name", body.name.orEmpty())
            .addFormDataPart("description", body.description.orEmpty())
            .addFormDataPart("file", body.file ?: "")
            .build()

        return api.postOtherDocument(multipartBody)
    }

    override fun getOtherDocumentDraft(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>> {
        return api.getOtherDocumentDraft(userId)
    }

    override fun getOtherDocument(userId: String): Single<BaseDataSourceApi<List<OtherDocumentDraftSourceApi>>> {
        return api.getOtherDocument(userId)
    }

    override fun deleteOtherDocument(id: String): Single<BaseDataSourceApi<Any>> {
        return api.deleteOtherDocumentDraft(id)
    }

    override fun uploadFile(file: File): Single<BaseDataSourceApi<FileServiceSourceApi>> {
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            file.path,
            file.asRequestBody("application/octet-stream".toMediaType())
        )

        return fileServiceApi.uploadFile(multipartBody)
    }

    override fun getSingleFile(id: String): Single<BaseDataSourceApi<GetFileSourceApi>> {
        return fileServiceApi.fetchFile(id)
    }
}