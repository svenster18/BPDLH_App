package id.bpdlh.fdb.core.di

import dagger.Module
import dagger.Provides
import id.bpdlh.fdb.core.common.utils.AppExecutors
import id.bpdlh.fdb.core.data.common.AlamatRepositoryContract
import id.bpdlh.fdb.core.data.common.AuthRepositoryContract
import id.bpdlh.fdb.core.data.common.DaftarPermohonanRepositoryContract
import id.bpdlh.fdb.core.data.common.FormKelompokNonSosialRepositoryContract
import id.bpdlh.fdb.core.data.common.FormPermohonanNonSosialRepositoryContract
import id.bpdlh.fdb.core.data.common.FormulirPendaftaranKelompokRepoContract
import id.bpdlh.fdb.core.data.common.GroupApplicationRepositoryContract
import id.bpdlh.fdb.core.data.common.HomeRepositoryContract
import id.bpdlh.fdb.core.data.common.KegiatanRepositoryContract
import id.bpdlh.fdb.core.data.common.PermohonanPembiayaanRepositoryContract
import id.bpdlh.fdb.core.data.common.RegistrasiKelompokRepositoryContract
import id.bpdlh.fdb.core.data.common.RegistrasiPeroranganRepositoryContract
import id.bpdlh.fdb.core.data.repositories.AlamatRepository
import id.bpdlh.fdb.core.data.repositories.AuthRepository
import id.bpdlh.fdb.core.data.repositories.DaftarPermohonanRepository
import id.bpdlh.fdb.core.data.repositories.FormKelompokNonSosialRepository
import id.bpdlh.fdb.core.data.repositories.FormPermohonanNonSosialRepository
import id.bpdlh.fdb.core.data.repositories.FormulirPendaftaranKelompokRepository
import id.bpdlh.fdb.core.data.repositories.GroupApplicationRepository
import id.bpdlh.fdb.core.data.repositories.HomeRepository
import id.bpdlh.fdb.core.data.repositories.KegiatanRepository
import id.bpdlh.fdb.core.data.repositories.PermohonanPembiayaanRepository
import id.bpdlh.fdb.core.data.repositories.RegistrasiKelompokRepository
import id.bpdlh.fdb.core.data.repositories.RegistrasiPeroranganRepository
import id.bpdlh.fdb.core.data.sources.local.room.DataDebiturDao
import id.bpdlh.fdb.core.data.sources.local.room.FileDao
import id.bpdlh.fdb.core.data.sources.local.room.FormKelompokNonSosialDao
import id.bpdlh.fdb.core.data.sources.local.room.FormulirPembiayaanNonPerhutananSosialDao
import id.bpdlh.fdb.core.data.sources.local.room.JaminanDao
import id.bpdlh.fdb.core.data.sources.local.room.KegiatanDao
import id.bpdlh.fdb.core.data.sources.local.room.MitraDao
import id.bpdlh.fdb.core.data.sources.local.room.RegistrasiPeroranganDao
import id.bpdlh.fdb.core.data.sources.remote.AlamatApi
import id.bpdlh.fdb.core.data.sources.remote.DaftarPermohonanApi
import id.bpdlh.fdb.core.data.sources.remote.FileServiceApi
import id.bpdlh.fdb.core.data.sources.remote.FormulirKelompokNonSosialApi
import id.bpdlh.fdb.core.data.sources.remote.FormulirPendaftaranKelompokApi
import id.bpdlh.fdb.core.data.sources.remote.GroupApplicationApi
import id.bpdlh.fdb.core.data.sources.remote.IdentityApi
import id.bpdlh.fdb.core.data.sources.remote.MasterDataApi
import id.bpdlh.fdb.core.data.sources.remote.MemberApplicationApi
import id.bpdlh.fdb.core.data.sources.remote.PermohonanPembiayaanApi
import id.bpdlh.fdb.core.data.sources.remote.RegistrasiApi
import id.bpdlh.fdb.core.data.sources.remote.UserApi

@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun provideAuthRepository(identityApi: IdentityApi, userApi: UserApi): AuthRepositoryContract =
        AuthRepository(identityApi, userApi)

    @Provides
    @AppScope
    fun provideRegistrasiPeroranganRepository(
        registrasiPeroranganDao: RegistrasiPeroranganDao,
        appExecutors: AppExecutors,
        groupApplicationApi: GroupApplicationApi,
        permohonanPembiayaanApi: PermohonanPembiayaanApi
    ): RegistrasiPeroranganRepositoryContract =
        RegistrasiPeroranganRepository(
            registrasiPeroranganDao,
            appExecutors,
            groupApplicationApi,
            permohonanPembiayaanApi
        )

    @Provides
    @AppScope
    fun provideFormDafrtarNonSosialRepository(
        formKelompokNonSosialDao: FormKelompokNonSosialDao,
        appExecutors: AppExecutors,
        formulirKelompokNonSosialApi: FormulirKelompokNonSosialApi,
        fileServiceApi: FileServiceApi
    ): FormKelompokNonSosialRepositoryContract =
        FormKelompokNonSosialRepository(formKelompokNonSosialDao, appExecutors, formulirKelompokNonSosialApi, fileServiceApi)
        
    @Provides
    @AppScope
    fun provideKegiatanRepository(
        kegiatanDao: KegiatanDao,
        appExecutors: AppExecutors
    ): KegiatanRepositoryContract =
        KegiatanRepository(kegiatanDao, appExecutors)

    @Provides
    @AppScope
    fun provideRegistrasiKelompokRepository(api: RegistrasiApi): RegistrasiKelompokRepositoryContract = RegistrasiKelompokRepository(api)

    @Provides
    @AppScope
    fun provideHomeRepository(api: UserApi): HomeRepositoryContract = HomeRepository(api)

    @Provides
    @AppScope
    fun providePermohonanPembiayaanRepository(
        formulirPembiayaanNonPerhutananSosialDao: FormulirPembiayaanNonPerhutananSosialDao,
        mitraDao: MitraDao,
        fileDao: FileDao,
        jaminanDao: JaminanDao,
        appExecutors: AppExecutors,
        permohonanPembiayaanApi: PermohonanPembiayaanApi,
        userApi: UserApi,
        fileServiceApi: FileServiceApi,
    ): PermohonanPembiayaanRepositoryContract =
        PermohonanPembiayaanRepository(
            formulirPembiayaanNonPerhutananSosialDao,
            mitraDao,
            fileDao,
            jaminanDao,
            appExecutors,
            permohonanPembiayaanApi,
            userApi,
            fileServiceApi
        )

    @Provides
    @AppScope
    fun provideFormulirPendaftaranKelompokRepository(api: FormulirPendaftaranKelompokApi, masterApi: MasterDataApi): FormulirPendaftaranKelompokRepoContract =
        FormulirPendaftaranKelompokRepository(api, masterApi)

    @Provides
    @AppScope
    fun provideGroupApplicationRepository(api: GroupApplicationApi): GroupApplicationRepositoryContract = GroupApplicationRepository(api)

    @Provides
    @AppScope
    fun provideDaftarPermohonanRepository(api: DaftarPermohonanApi, dao: DataDebiturDao): DaftarPermohonanRepositoryContract =
        DaftarPermohonanRepository(api, dao)

    @Provides
    @AppScope
    fun provideFormPermohonanNonSosialRepository(
        api: MemberApplicationApi,
         mitraDao: MitraDao,
         fileDao: FileDao,
         jaminanDao: JaminanDao,
         appExecutors: AppExecutors,
         fileServiceApi: FileServiceApi,
        ): FormPermohonanNonSosialRepositoryContract =
        FormPermohonanNonSosialRepository(api, mitraDao, fileDao, jaminanDao, appExecutors, fileServiceApi)


    @Provides
    @AppScope
    fun provideAlamatRepository(api: AlamatApi): AlamatRepositoryContract = AlamatRepository(api)
}