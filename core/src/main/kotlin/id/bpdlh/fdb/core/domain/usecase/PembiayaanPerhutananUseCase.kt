package id.bpdlh.fdb.core.domain.usecase

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.common.utils.Constants
import id.bpdlh.fdb.core.common.utils.convertISOTimeToDate
import id.bpdlh.fdb.core.common.utils.singleIo
import id.bpdlh.fdb.core.common.widget.FdbBadge
import id.bpdlh.fdb.core.data.common.PermohonanPembiayaanRepositoryContract
import id.bpdlh.fdb.core.data.entities.GetFileSourceApi
import id.bpdlh.fdb.core.data.post.OtherDocumentPost
import id.bpdlh.fdb.core.data.post.PermohonanPembiayaanPerhutananSosialPost
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanNonPerhutananSosial
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.domain.common.PembiayaanPerhutananUseCaseContract
import id.bpdlh.fdb.core.domain.entities.DaftarPemohonEntity
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import id.bpdlh.fdb.core.domain.entities.OtherDocumentEntity
import id.bpdlh.fdb.core.domain.entities.PembiayaanPerhutananEntity
import id.bpdlh.fdb.core.domain.mapper.responseBaseDataSourceApiToResultState
import id.bpdlh.fdb.core.domain.mapper.responseErrorToResultStateError
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber
import java.io.File

class PembiayaanPerhutananUseCase(private val repo: PermohonanPembiayaanRepositoryContract) :
    PembiayaanPerhutananUseCaseContract {

    override fun fetchDataPembiayaanPerhutanan(
        userId: String,
        param: HashMap<String, String>,
        type: Int
    ): Single<ResultState<List<PembiayaanPerhutananEntity>>> {
        return repo.fetchMemberApplicationByUserId(userId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val memberApplicationDetail = response.memberApplicationDetail
                val memberApplication = response.memberApplicationDetail.memberApplication
                listOf(
                    PembiayaanPerhutananEntity(
                        id = memberApplicationDetail.memberApplicationId.orEmpty(),
                        date = response.createdAt.orEmpty().convertISOTimeToDate("dd MMM yyyy"),
                        requestDate = memberApplication?.requestDate.orEmpty()
                            .convertISOTimeToDate("dd MMM yyyy"),
                        status = when (response.financingStatus) {
                            Constants.STATUS_MENUNGGU_VERIFIKASI -> if (response.isFinancingSubmitted) FdbBadge.ON_REVIEW else FdbBadge.DRAFT
                            Constants.STATUS_DISETUJUI -> FdbBadge.APPROVED
                            Constants.STATUS_DITOLAK -> FdbBadge.REJECTED
                            else -> -1
                        },
                        serviceType = when (response.serviceType) {
                            "Tunda Tebang" -> FdbBadge.TUNDA_TEBANG
                            "Hasil Hutan Bukan Kayu" -> FdbBadge.HHBK
                            "Komoditas Non Kehutanan" -> FdbBadge.REFINANCING_KEHUTANAN
                            else -> 0
                        },
                        amountOfLoan = memberApplicationDetail.amountOfLoan,
                        memberApplication = PermohonanPembiayaanPerhutananSosialPost(
                            groupName = memberApplication?.groupProfile?.name.orEmpty(),
                            ktp = response.ktp.orEmpty(),
                            name = response.name.orEmpty(),
                            placeOfBirth = response.placeOfBirth.orEmpty(),
                            dateOfBirth = response.dateOfBirth.orEmpty()
                                .convertISOTimeToDate("dd/MM/yyyy"),
                            gender = response.gender.orEmpty(),
                            kk = response.kk.orEmpty(),
                            job = response.job.orEmpty(),
                            otherJob = response.otherJob.orEmpty(),
                            coupleKtp = response.coupleKtp.orEmpty(),
                            coupleName = response.coupleName.orEmpty(),
                            couplePlaceOfBirth = response.couplePlaceOfBirth.orEmpty(),
                            coupleDateOfBirth = if (response.coupleDateOfBirth.orEmpty()
                                    .isNotEmpty()
                            ) response.coupleDateOfBirth.orEmpty()
                                .convertISOTimeToDate("dd/MM/yyyy") else "",
                            ktpProvince = response.ktpProvince.orEmpty(),
                            ktpCity = response.ktpCity.orEmpty(),
                            ktpDistrict = response.ktpDistrict.orEmpty(),
                            ktpVillage = response.ktpVillage.orEmpty(),
                            ktpRt = response.ktpRt.orEmpty(),
                            ktpRw = response.ktpRw.orEmpty(),
                            ktpAddress = response.ktpAddress.orEmpty(),
                            domicileProvince = response.domicileProvince.orEmpty(),
                            domicileCity = response.domicileCity.orEmpty(),
                            domicileDistrict = response.domicileDistrict.orEmpty(),
                            domicileVillage = response.domicileVillage.orEmpty(),
                            domicileRt = response.domicileRt.orEmpty(),
                            domicileRw = response.domicileRw.orEmpty(),
                            domicileAddress = response.domicileAddress.orEmpty(),
                            domicileLatitude = response.domicileLatitude.orEmpty(),
                            domicileLongitude = response.domicileLongitude.orEmpty(),
                            domicileSinceYear = response.domicileSinceYear.orEmpty(),
                            income = response.income.toString().toLong(),
                            incomeCycle = response.incomeCycle.toString().toInt(),
                            incomeCycleUnit = response.incomeCycleUnit.orEmpty(),
                            expense = response.expense.toString().toLong(),
                            largestExpense = response.largestExpense.toString().toLong(),
                            largestUseExpense = response.largestUseExpense.orEmpty(),
                            businessType = response.businessType.orEmpty(),
                            businessCommodity = response.businessCommodity.orEmpty(),
                            businessDuration = response.businessDuration.toString().toInt(),
                            businessDurationUnit = response.businessDurationUnit.orEmpty(),
                            sourceOfProduction = response.sourceOfProduction.orEmpty(),
                            businessCapacity = response.businessCapacity.orEmpty(),
                            businessEconomicValue = response.businessEconomicValue.toString()
                                .toLong(),
                            marketingObjective = response.marketingObjective.orEmpty(),
                            usagePlan = response.usagePlan.orEmpty(),
                            estimatedTurnover = response.estimatedTurnover.toString().toLong(),
                            estimatedProductionCost = response.estimatedProductionCost.toString()
                                .toLong(),
                            estimatedNetIncome = response.estimatedNetIncome.toString().toLong(),
                            productionBusinessCycle = response.productionBusinessCycle.toString()
                                .toInt(),
                            productionBusinessCycleUnit = response.productionBusinessCycleUnit.orEmpty(),
                            amountOfLoan = response.amountOfLoan.toString().toLong(),
                            timePeriod = response.timePeriod.toString().toInt(),
                            timePeriodUnit = response.timePeriodUnit.orEmpty(),
                            amountOfInstallment = response.amountOfInstallment.toString().toLong(),
                            createdIn = response.createdIn.orEmpty(),
                            createdAt = response.createdAt.orEmpty(),
                            ktpFileId = response.ktpFile.orEmpty(),
                            kkFileId = response.kkFile.orEmpty(),
                            coupleKtpFileId = response.coupleKtpFile.orEmpty(),
                            businessPartners = emptyList(),
                            guarantee = emptyList()
                        )
                    )
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<PembiayaanPerhutananEntity>>(
                it
            )
        }.compose(singleIo())
    }

    override fun fetchDataMitra(
        userId: String,
        param: HashMap<String, String>,
        type: Int
    ): Single<ResultState<List<Mitra>>> {
        val data = when (type) {
            FdbBadge.DRAFT -> repo.fetchBusinessPartnerDraftByUserId(userId)
            else -> repo.fetchBusinessPartnerByUserId(userId)
        }
        return data.map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                val result = mutableListOf<Mitra>()
                response.forEach { data ->
                    val item = Mitra(
                        nama = data.name.toString()
                    )
                    result.add(item)
                }
                result.toList()
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<List<Mitra>>(
                it
            )
        }.compose(singleIo())
    }

    override fun fetchDataPembiayaanNonPerhutanan(
        param: HashMap<String, String>,
        type: Int
    ): ResultState<List<PembiayaanPerhutananEntity>> {
        val data = when (type) {
            FdbBadge.DRAFT -> {
                listOf(
                    PembiayaanPerhutananEntity(
                        "id1",
                        "10 Feb 2023",
                        "10 Feb 2023",
                        FdbBadge.DRAFT,
                        5000000,
                        FdbBadge.TUNDA_TEBANG
                    ),
                    PembiayaanPerhutananEntity(
                        "id2",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.DRAFT,
                        5000000,
                        FdbBadge.REFINANCING_KEHUTANAN
                    ),
                    PembiayaanPerhutananEntity(
                        "id3",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.DRAFT,
                        5000000,
                        FdbBadge.HHBK
                    ),
                )
            }

            FdbBadge.ON_REVIEW -> {
                listOf(
                    PembiayaanPerhutananEntity(
                        "id3",
                        "10 Feb 2023",
                        "10 Feb 2023",
                        FdbBadge.ON_REVIEW,
                        50000000,
                        FdbBadge.TUNDA_TEBANG
                    ),
                    PembiayaanPerhutananEntity(
                        "id4",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.ON_REVIEW,
                        50000000,
                        FdbBadge.REFINANCING_KEHUTANAN
                    ),
                    PembiayaanPerhutananEntity(
                        "id5",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.ON_REVIEW,
                        50000000,
                        FdbBadge.HHBK
                    )
                )
            }

            FdbBadge.APPROVED -> {
                listOf(
                    PembiayaanPerhutananEntity(
                        "id3",
                        "10 Feb 2023",
                        "10 Feb 2023",
                        FdbBadge.APPROVED,
                        50000000,
                        FdbBadge.TUNDA_TEBANG
                    ),
                    PembiayaanPerhutananEntity(
                        "id4",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.APPROVED,
                        50000000,
                        FdbBadge.REFINANCING_KEHUTANAN
                    ),
                    PembiayaanPerhutananEntity(
                        "id5",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.APPROVED,
                        50000000,
                        FdbBadge.HHBK
                    )
                )
            }

            FdbBadge.REJECTED -> {
                listOf(
                    PembiayaanPerhutananEntity(
                        "id3",
                        "10 Feb 2023",
                        "10 Feb 2023",
                        FdbBadge.REJECTED,
                        50000000,
                        FdbBadge.TUNDA_TEBANG
                    ),
                    PembiayaanPerhutananEntity(
                        "id4",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.REJECTED,
                        50000000,
                        FdbBadge.REFINANCING_KEHUTANAN
                    ),
                    PembiayaanPerhutananEntity(
                        "id5",
                        "2 Feb 2023",
                        "2 Feb 2023",
                        FdbBadge.REJECTED,
                        50000000,
                        FdbBadge.HHBK
                    )
                )
            }

            else -> emptyList()
        }
        return ResultState.Success(data = data)
    }

    override fun submitFormulirPembiayaanPerhutananSosial(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<ResultState<MemberApplicationEntity>> {
        Timber.d(body.toString())
        return repo.updateSocialForestry(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userId,
                    response.email,
                    response.status,
                    "Anggota",
                    response.isSubmittable,
                    response.isSubmitted,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun submitPermohonanPembiayaan(memberApplicationId: String): Single<ResultState<DaftarPemohonEntity>> {
        return repo.submitApplication(memberApplicationId).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                DaftarPemohonEntity(
                    response.id.orEmpty(),
                    response.requestDate.orEmpty(),
                    dataType = getDataType(response.status.orEmpty()),
                    status = response.status.orEmpty(),
                    totalAnggota = response.totalMember ?: 0,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<DaftarPemohonEntity>(it)
        }.compose(singleIo())
    }

    private fun getDataType(input: String): Int {
        return when (input) {
            "Draft" -> FdbBadge.DRAFT
            "On Progress" -> FdbBadge.ON_PROGRESS
            "Menunggu Verifikasi" -> FdbBadge.ON_VERIFY
            "Terverifikasi" -> FdbBadge.SUCCESS
            else -> FdbBadge.DRAFT
        }
    }

    override fun saveFormulirPembiayaanPerhutananSosialDraft(
        userId: String,
        body: PermohonanPembiayaanPerhutananSosialPost
    ): Single<ResultState<MemberApplicationEntity>> {
        Timber.d(body.toString())
        return repo.updateSocialForestryDraft(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userId,
                    response.email,
                    response.status,
                    "Anggota",
                    response.isSubmittable,
                    response.isSubmitted,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun submitFormulirPembiayaanNonPerhutananSosial(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<ResultState<MemberApplicationEntity>> {
        return repo.updateNonSocialForestry(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userId,
                    response.email,
                    response.status,
                    "Anggota",
                    response.isSubmittable,
                    response.isSubmitted,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun saveFormulirPembiayaanNonPerhutananSosialDraft(
        userId: String,
        body: FormulirPembiayaanNonPerhutananSosial
    ): Single<ResultState<MemberApplicationEntity>> {
        return repo.updateNonSocialForestryDraft(userId, body).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                MemberApplicationEntity(
                    userId,
                    response.email,
                    response.status,
                    "Anggota",
                    response.isSubmittable,
                    response.isSubmitted,
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<MemberApplicationEntity>(it)
        }.compose(singleIo())
    }

    override fun uploadFile(file: File): Single<ResultState<String>> {
        return repo.uploadFile(file).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response.id
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<String>(it)
        }.compose(singleIo())
    }

    override fun getSingleFile(id: String): Single<ResultState<GetFileSourceApi>> {
        return repo.getSingleFile(id).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                response
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<GetFileSourceApi>(it)
        }.compose(singleIo())
    }

    override fun fetchFormulirPembiayaanNonPerhutananSosial(status: Int): Maybe<FormulirPembiayaanNonPerhutananSosial> {
        return repo.getFormulirPembiayaanNonPerhutananSosial(status)
    }

    override fun insertFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial) {
        repo.insertFormulirPembiayaanNonPerhutananSosial(
            formulirPembiayaanNonPerhutananSosial
        )
    }

    override fun updateFormulirPembiayaanNonPerhutananSosial(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial) {
        repo.updateFormulirPembiayaanNonPerhutananSosial(
            formulirPembiayaanNonPerhutananSosial
        )
    }

    override fun fetchMitra(formulirId: Int): Flowable<List<Mitra>> {
        return repo.getMitra(formulirId)
    }

    override fun insertMitra(mitra: List<Mitra>) {
        repo.insertMitra(mitra)
    }

    override fun updateMitra(mitra: Mitra) {
        repo.updateMitra(mitra)
    }

    override fun createOtherDocumentDraft(otherDocumentPost: OtherDocumentPost): Single<ResultState<OtherDocumentEntity>> {
        return repo.createOtherDocumentDraft(otherDocumentPost).map {
            return@map responseBaseDataSourceApiToResultState(it) { response ->
                OtherDocumentEntity(
                    response.id,
                    response.name,
                    response.description,
                    response.file,
                    response.memberApplicationDetailDraftId,
                    response.createdAt,
                    response.fileUrl
                )
            }
        }.onErrorReturn {
            return@onErrorReturn responseErrorToResultStateError<OtherDocumentEntity>(it)
        }.compose(singleIo())
    }

    override fun fetchFile(parentId: Int, type: Int): Flowable<List<FileEntity>> {
        return repo.getFile(parentId, type)
    }

    override fun insertFile(fileEntity: List<FileEntity>) {
        repo.insertFile(fileEntity)
    }

    override fun updateFile(fileEntity: FileEntity) {
        repo.updateFile(fileEntity)
    }

    override fun fetchJaminan(formulirId: Int): Flowable<List<JaminanEntity>> {
        return repo.getJaminan(formulirId)
    }

    override fun insertJaminan(jaminan: List<JaminanEntity>) {
        repo.insertJaminan(jaminan)
    }

    override fun updateJaminan(jaminan: JaminanEntity) {
        repo.updateJaminan(jaminan)
    }
}