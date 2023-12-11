package id.bpdlh.fdb.core.domain.common

import id.bpdlh.fdb.core.base.ResultState
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import id.bpdlh.fdb.core.domain.entities.MemberApplicationEntity
import io.reactivex.Maybe
import io.reactivex.Single

interface RegistrasiPeroranganUseCaseContract {
    fun fetchDraft(): Maybe<RegistrasiPerorangan>
    fun insertDraft(registrasiPerorangan: RegistrasiPerorangan)
    fun updateDraft(registrasiPerorangan: RegistrasiPerorangan)
    fun getMemberApplication(userId: String): Single<ResultState<MemberApplicationDataEntity>>
    fun submitFormulirPembiayaanNonPerhutananSosial(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<ResultState<MemberApplicationEntity>>

    fun saveFormulirPembiayaanNonPerhutananSosialDraft(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<ResultState<MemberApplicationEntity>>

    fun delayCuttingDraft(
        userId: String,
        memberApplicationPost: MemberApplicationPost
    ): Single<ResultState<MemberApplicationDataEntity>>

    fun delayCuttingUpdate(
        userId: String,
        memberApplicationPost: MemberApplicationPost
    ): Single<ResultState<Any>>

    fun nonForestyComodityDraft(
        userId: String,
        memberApplicationPost: MemberApplicationPost
    ): Single<ResultState<Any>>

    fun nonForestyComodityUpdate(
        userId: String,
        memberApplicationPost: MemberApplicationPost
    ): Single<ResultState<Any>>

    fun nonWoodForestProductDraft(
        userId: String,
        memberApplicationPost: MemberApplicationPost
    ): Single<ResultState<Any>>

    fun nonWoodForestProductUpdate(
        userId: String,
        memberApplicationPost: MemberApplicationPost
    ): Single<ResultState<Any>>
}