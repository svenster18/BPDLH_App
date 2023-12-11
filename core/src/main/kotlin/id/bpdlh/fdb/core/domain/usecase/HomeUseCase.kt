package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants.TYPE_KELOMPOK
import id.bpdlh.fdb.core.common.utils.Constants.TYPE_PERORANGAN
import id.bpdlh.fdb.core.common.utils.convertISOTimeToDate
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.HomeRepositoryContract
import id.bpdlh.fdb.core.domain.common.HomeUseCaseContract
import id.bpdlh.fdb.core.domain.entities.ActivityEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.core.domain.entities.UserEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single
import okhttp3.internal.toImmutableList
import kotlin.random.Random

/**
 * Created by hahn on 28/09/23.
 * Project: BPDLH App
 */
class HomeUseCase(private val repo: HomeRepositoryContract): HomeUseCaseContract {

    override fun fetchHomeType(): ResultState<String> {
        val type = if (Random.nextInt(0, 5) % 2 == 0) {
            TYPE_PERORANGAN
        } else {
            TYPE_KELOMPOK
        }
        return ResultState.Success(type)
    }

    override fun fetchGroupProfile(userId: String): Single<ResultState<GroupProfileEntity>> {

        return repo.fetchGroupProfileByUserId(userId).map { data ->
            return@map responseBaseDataSourceApiToResultState(data) {
                val user = it.groupProfile?.user
                val userEntity = UserEntity(
                    userId = user?.id,
                    email = user?.email,
                    role = user?.role
                )

                GroupProfileEntity(
                    name = it.name,
                    type = it.type,
                    sk = it.sk,
                    functionality = it.functionality,
                    establishmentDate = it.establishmentDate,
                    administrationDeadlineDate = it.administrationDeadlineDate,
                    amountOfMember = it.amountOfMember,
                    amountOfMemberSubmit = it.amountOfMemberSubmit,
                    amountOfMemberLand = it.amountOfMemberLand,
                    amountOfMemberLandArea = it.amountOfMemberLandArea,
                    amountOfMemberSubmitLand = it.amountOfMemberSubmitLand,
                    amountOfMemberSubmitLandArea = it.amountOfMemberSubmitLandArea,
                    province = it.province,
                    city = it.city,
                    district = it.district,
                    village = it.village,
                    address = it.address,
                    contactPersonName = it.contactPersonName,
                    contactPersonKtp = it.contactPersonKtp,
                    contactPersonPosition = it.contactPersonPosition,
                    contactPersonEmail = it.contactPersonEmail,
                    contactPersonPhoneNumber = it.contactPersonPhoneNumber,
                    kupsName = it.kupsName,
                    kupsSk = it.kupsSk,
                    leaderName = it.leaderName,
                    leaderKtp = it.leaderKtp,
                    leaderPhoneNumber = it.leaderPhoneNumber,
                    leaderGender = it.leaderGender,
                    secretaryName = it.secretaryName,
                    secretaryPhoneNumber = it.secretaryPhoneNumber,
                    secretaryGender = it.secretaryGender,
                    treasurerName = it.treasurerName,
                    treasurerPhoneNumber = it.treasurerPhoneNumber,
                    treasurerGender = it.treasurerGender,
                    companionName = it.companionName,
                    companionPhoneNumber = it.companionPhoneNumber,
                    companionAffiliate = it.companionAffiliate,
                    companionGender = it.companionGender,
                    status = it.status,
                    role = it.groupProfile?.user?.role,
                    isSubmittable = it.isSubmittable,
                    isSubmitted = it.isSubmitted,
                    createdIn = it.createdIn,
                    userEntity = userEntity,
                    skFile = it.skFile,
                    adFile = it.adFile,
                    companionRecomendationFile = it.companionRecomendationFile
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GroupProfileEntity>(it)
        }.compose(singleIo())
    }

    override fun fetchMemberApplication(userId: String): Single<ResultState<MemberApplicationEntity>> {
        return repo.fetchMemberApplicationByUserId(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userID = response.memberApplicationDetail.user?.id,
                    email = response.email,
                    status = response.status,
                    role = response.memberApplicationDetail.user?.role,
                    isSubmittable = response.isSubmittable,
                    isSubmitted = response.isSubmitted,
                    serviceType = response.serviceType,
                    groupName = response.memberApplicationDetail.memberApplication?.groupProfile?.name.orEmpty(),
                    name = response.name.orEmpty(),
                    ktp = response.ktp.orEmpty(),
                    date_of_birth = response.dateOfBirth.orEmpty()
                        .convertISOTimeToDate("dd/MM/yyyy"),
                    gender = response.gender,
                    phoneNumber = response.phoneNumber
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun getActivity(userId: String): Single<ResultState<List<ActivityEntity>>> {
        return repo.getActivity(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listActivities = mutableListOf<ActivityEntity>()
                response.forEach { activity ->
                    listActivities.add(
                        ActivityEntity(
                            id =activity.id.orEmpty(),
                            category = activity.category.orEmpty(),
                            description = activity.description.orEmpty()
                        )
                    )
                }
                listActivities.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<ActivityEntity>>(it)
        }.compose(singleIo())
    }

    override fun getGeneralDescription(userId: String): Single<ResultState<List<ActivityEntity>>> {
        return repo.getGeneralDescription(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listDescriptions = mutableListOf<ActivityEntity>()
                response.forEach { generalDescription ->
                    listDescriptions.add(
                        ActivityEntity(
                            id = generalDescription.id.orEmpty(),
                            category = generalDescription.category.orEmpty(),
                            description = generalDescription.description.orEmpty()
                        )
                    )
                }
                listDescriptions.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<ActivityEntity>>(it)
        }.compose(singleIo())
    }

    override fun getPartner(userId: String): Single<ResultState<List<ActivityEntity>>> {
        return repo.getPartner(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listDescriptions = mutableListOf<ActivityEntity>()
                response.forEach { generalDescription ->
                    listDescriptions.add(
                        ActivityEntity(
                            id = generalDescription.id.orEmpty(),
                            category = generalDescription.category.orEmpty()
                        )
                    )
                }
                listDescriptions.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<ActivityEntity>>(it)
        }.compose(singleIo())
    }
}