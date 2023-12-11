package id.bpdlh.fdb.core.data.common

import id.bpdlh.fdb.core.data.entities.BaseDataSourceApi
import id.bpdlh.fdb.core.data.entities.MemberApplicationSourceApi
import id.bpdlh.fdb.core.data.post.MemberApplicationPost
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.entities.MemberApplicationDataEntity
import io.reactivex.Maybe
import io.reactivex.Single

interface RegistrasiPeroranganRepositoryContract {
    fun getDraft(): Maybe<RegistrasiPerorangan>
    fun insert(registrasiPerorangan: RegistrasiPerorangan)
    fun update(registrasiPerorangan: RegistrasiPerorangan)
    fun getMemberApplication(userId: String): Single<BaseDataSourceApi<MemberApplicationDataEntity>>
    fun updateNonSocialForestry(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun updateNonSocialForestryDraft(
        userId: String,
        body: RegistrasiPerorangan
    ): Single<BaseDataSourceApi<MemberApplicationSourceApi>>

    fun delayCuttingDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<MemberApplicationDataEntity>>

    fun delayCuttingUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>>

    fun nonForestyComodityDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>>

    fun nonForestyComodityUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>>

    fun nonWoodForestProductDraft(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>>

    fun nonWoodForestProductUpdate(
        userId: String,
        body: MemberApplicationPost
    ): Single<BaseDataSourceApi<Any>>
}