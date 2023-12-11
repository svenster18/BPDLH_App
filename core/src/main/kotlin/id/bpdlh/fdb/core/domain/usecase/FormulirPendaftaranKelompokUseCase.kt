package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.orFalse
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.data.common.FormulirPendaftaranKelompokRepoContract
import id.bpdlh.fdb.core.domain.common.FormulirPendaftaranKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.entities.AfiliasiPendampingEntity
import id.bpdlh.fdb.core.domain.entities.FungsiKawasanEntity
import id.bpdlh.fdb.core.domain.entities.GroupProfileEntity
import id.bpdlh.fdb.core.domain.entities.KategoriKegiatanEntity
import id.bpdlh.fdb.core.domain.entities.UserEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Single
import okhttp3.internal.toImmutableList

/**
 * Created by hahn on 20/10/23.
 * Project: BPDLH App
 */
class FormulirPendaftaranKelompokUseCase(private val repo: FormulirPendaftaranKelompokRepoContract): FormulirPendaftaranKelompokUseCaseContract {

    override fun submitData(userId: String, data: GroupProfileEntity): Single<ResultState<GroupProfileEntity>> {
        return repo.submit(userId, data).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val user = UserEntity(
                    userId = response.groupProfile?.user?.id,
                    email = response.groupProfile?.user?.email,
                    role = response.groupProfile?.user?.role
                )
                GroupProfileEntity(
                    name = response.name,
                    type = response.type,
                    sk = response.sk,
                    establishmentDate = response.establishmentDate,
                    administrationDeadlineDate = response.administrationDeadlineDate,
                    amountOfMember = response.amountOfMember,
                    amountOfMemberSubmit = response.amountOfMemberSubmit,
                    amountOfMemberSubmitLand = response.amountOfMemberSubmitLand,
                    amountOfMemberSubmitLandArea = response.amountOfMemberSubmitLandArea,
                    functionality = response.functionality,
                    province = response.province,
                    city = response.city,
                    district = response.district,
                    village = response.village,
                    address = response.address,
                    contactPersonName = response.contactPersonName,
                    contactPersonKtp = response.contactPersonKtp,
                    contactPersonPosition = response.contactPersonPosition,
                    contactPersonEmail = response.contactPersonEmail,
                    contactPersonPhoneNumber = response.contactPersonPhoneNumber,
                    kupsName = response.kupsName,
                    kupsSk = response.kupsSk,
                    leaderName = response.leaderName,
                    leaderKtp = response.leaderKtp,
                    leaderPhoneNumber = response.leaderPhoneNumber,
                    leaderGender = response.leaderGender,
                    secretaryName = response.secretaryName,
                    secretaryPhoneNumber = response.secretaryPhoneNumber,
                    secretaryGender = response.secretaryGender,
                    treasurerName = response.treasurerName,
                    treasurerPhoneNumber = response.treasurerPhoneNumber,
                    treasurerGender = response.treasurerGender,
                    companionName = response.companionName,
                    companionPhoneNumber = response.companionPhoneNumber,
                    companionAffiliate = response.companionAffiliate,
                    companionGender = response.companionGender,
                    status = response.status,
                    role = response.groupProfile?.user?.role,
                    isSubmittable = response.isSubmittable.orFalse(),
                    isSubmitted = response.isSubmitted.orFalse(),
                    userEntity = user,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GroupProfileEntity>(it)
        }.compose(singleIo())
    }


    override fun saveToDraft(userId: String, data: GroupProfileEntity): Single<ResultState<GroupProfileEntity>> {
        return repo.saveDraft(userId, data).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val user = UserEntity(
                    userId = response.groupProfile?.user?.id,
                    email = response.groupProfile?.user?.email,
                    role = response.groupProfile?.user?.role
                )
                GroupProfileEntity(
                    name = response.name,
                    type = response.type,
                    sk = response.sk,
                    establishmentDate = response.establishmentDate,
                    administrationDeadlineDate = response.administrationDeadlineDate,
                    amountOfMember = response.amountOfMember,
                    amountOfMemberSubmit = response.amountOfMemberSubmit,
                    amountOfMemberSubmitLand = response.amountOfMemberSubmitLand,
                    amountOfMemberSubmitLandArea = response.amountOfMemberSubmitLandArea,
                    functionality = response.functionality,
                    province = response.province,
                    city = response.city,
                    district = response.district,
                    village = response.village,
                    address = response.address,
                    contactPersonName = response.contactPersonName,
                    contactPersonKtp = response.contactPersonKtp,
                    contactPersonPosition = response.contactPersonPosition,
                    contactPersonEmail = response.contactPersonEmail,
                    contactPersonPhoneNumber = response.contactPersonPhoneNumber,
                    kupsName = response.kupsName,
                    kupsSk = response.kupsSk,
                    leaderName = response.leaderName,
                    leaderKtp = response.leaderKtp,
                    leaderGender = response.leaderGender,
                    leaderPhoneNumber = response.leaderPhoneNumber,
                    secretaryName = response.secretaryName,
                    secretaryPhoneNumber = response.secretaryPhoneNumber,
                    secretaryGender = response.secretaryGender,
                    treasurerName = response.treasurerName,
                    treasurerPhoneNumber = response.treasurerPhoneNumber,
                    treasurerGender = response.treasurerGender,
                    companionName = response.companionName,
                    companionPhoneNumber = response.companionPhoneNumber,
                    companionAffiliate = response.companionAffiliate,
                    companionGender = response.companionGender,
                    status = response.status,
                    role = response.groupProfile?.user?.role,
                    isSubmittable = response.isSubmittable.orFalse(),
                    isSubmitted = response.isSubmitted.orFalse(),
                    userEntity = user
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GroupProfileEntity>(it)
        }.compose(singleIo())
    }

    override fun fetchFungsiKawasan(): Single<ResultState<List<FungsiKawasanEntity>>> {
        return repo.fetchFungsiKawasan().map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listResult = mutableListOf<FungsiKawasanEntity>()
                response.forEach { fungsiKawasan ->
                    val item = FungsiKawasanEntity(
                        id = fungsiKawasan.id.orEmpty(),
                        name = fungsiKawasan.name.orEmpty(),
                        description = fungsiKawasan.description.orEmpty()
                    )
                    listResult.add(item)
                }
                listResult.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<FungsiKawasanEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchAfiliasiPendamping(): Single<ResultState<List<AfiliasiPendampingEntity>>> {
        return repo.fetchAfiliasiPendamping().map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listResult = mutableListOf<AfiliasiPendampingEntity>()
                response.forEach { afiliasi ->
                    val item = AfiliasiPendampingEntity(
                        id = afiliasi.id.orEmpty(),
                        name = afiliasi.name.orEmpty(),
                        description = afiliasi.description.orEmpty()
                    )
                    listResult.add(item)
                }
                listResult.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<AfiliasiPendampingEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchKategoriKegiatan(): Single<ResultState<List<KategoriKegiatanEntity>>> {
        return repo.fetchKategoriKegiatan().map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listResult = mutableListOf<KategoriKegiatanEntity>()
                response.forEach { afiliasi ->
                    val item = KategoriKegiatanEntity(
                        id = afiliasi.id.orEmpty(),
                        name = afiliasi.name.orEmpty(),
                        description = afiliasi.description.orEmpty()
                    )
                    listResult.add(item)
                }
                listResult.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<KategoriKegiatanEntity>>(it)
        }.compose(singleIo())
    }

    override fun fetchKategoriUsahaDibiayai(): Single<ResultState<List<KategoriKegiatanEntity>>> {
        return repo.fetchKategoriUsahaDibiayai().map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val listResult = mutableListOf<KategoriKegiatanEntity>()
                response.forEach { afiliasi ->
                    val item = KategoriKegiatanEntity(
                        id = afiliasi.id.orEmpty(),
                        name = afiliasi.name.orEmpty(),
                        description = afiliasi.description.orEmpty()
                    )
                    listResult.add(item)
                }
                listResult.toImmutableList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<KategoriKegiatanEntity>>(it)
        }.compose(singleIo())
    }
}