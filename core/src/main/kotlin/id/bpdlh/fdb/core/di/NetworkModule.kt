package id.bpdlh.fdb.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import id.bpdlh.fdb.core.data.sources.remote.AlamatApi
import id.bpdlh.fdb.core.data.sources.remote.DaftarPermohonanApi
import id.bpdlh.fdb.core.data.sources.remote.FileServiceApi
import id.bpdlh.fdb.core.data.sources.remote.FormulirKelompokNonSosialApi
import id.bpdlh.fdb.core.data.sources.remote.FormulirPendaftaranKelompokApi
import id.bpdlh.fdb.core.data.sources.remote.GroupApplicationApi
import id.bpdlh.fdb.core.data.sources.remote.IdentityApi
import id.bpdlh.fdb.core.data.sources.remote.MemberApplicationApi
import id.bpdlh.fdb.core.data.sources.remote.MasterDataApi
import id.bpdlh.fdb.core.data.sources.remote.PermohonanPembiayaanApi
import id.bpdlh.fdb.core.data.sources.remote.RegistrasiApi
import id.bpdlh.fdb.core.data.sources.remote.UserApi
import id.bpdlh.fdb.core.network.NetworkHelper

@Module
class NetworkModule {
    @Provides
    @AppScope
    fun provideIdentityService(application: Application): IdentityApi {
        return NetworkHelper.retrofitIdentityClient(application).create(IdentityApi::class.java)
    }

    @Provides
    @AppScope
    fun provideUserService(application: Application): UserApi {
        return NetworkHelper.retrofitClient(application).create(UserApi::class.java)
    }

    @Provides
    @AppScope
    fun provideRegistrasiAPi(application: Application): RegistrasiApi =
        NetworkHelper.retrofitClient(application).create(RegistrasiApi::class.java)

    @Provides
    @AppScope
    fun provideFormulirKelompokNonSosialApi(application: Application): FormulirKelompokNonSosialApi =
        NetworkHelper.retrofitClient(application).create(FormulirKelompokNonSosialApi::class.java)

    @Provides
    @AppScope
    fun provideGroupApplicationApi(application: Application): GroupApplicationApi =
        NetworkHelper.retrofitClient(application).create(GroupApplicationApi::class.java)

    @Provides
    @AppScope
    fun provideDaftarPermohonanApi(application: Application): DaftarPermohonanApi =
        NetworkHelper.retrofitClient(application).create(DaftarPermohonanApi::class.java)

    @Provides
    @AppScope
    fun provideFormulirPendaftaranKelompokApi(application: Application): FormulirPendaftaranKelompokApi =
        NetworkHelper.retrofitClient(application).create(FormulirPendaftaranKelompokApi::class.java)

    @Provides
    @AppScope
    fun providePermohonanPembiayaanApi(application: Application): PermohonanPembiayaanApi =
        NetworkHelper.retrofitClient(application).create(PermohonanPembiayaanApi::class.java)

    @Provides
    @AppScope
    fun provideFileServiceApi(application: Application): FileServiceApi =
        NetworkHelper.retrofitFileServiceClient(application).create(FileServiceApi::class.java)

    @Provides
    @AppScope
    fun provideMemberApplicationApi(application: Application): MemberApplicationApi =
        NetworkHelper.retrofitClient(application).create(MemberApplicationApi::class.java)

    @Provides
    @AppScope
    fun provideMasterDataApi(application: Application): MasterDataApi =
        NetworkHelper.retrofitClient(application).create(MasterDataApi::class.java)

    @Provides
    @AppScope
    fun provideAlamatApi(application: Application): AlamatApi =
        NetworkHelper.retrofitClient(application).create(AlamatApi::class.java)
}