package id.bpdlh.fdb.core.di

import dagger.Module
import dagger.Provides
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
import id.bpdlh.fdb.core.domain.common.AlamatUseCaseContract
import id.bpdlh.fdb.core.domain.common.AuthUseCaseContract
import id.bpdlh.fdb.core.domain.common.DaftarAnggotaPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.common.DaftarPemohonNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.common.DaftarPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.common.DetailDaftarAnggotaPemohonUseCaseContract
import id.bpdlh.fdb.core.domain.common.EditAkunUseCaseContract
import id.bpdlh.fdb.core.domain.common.FormKelompokNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.common.FormPermohonanNonSosialUseCaseContract
import id.bpdlh.fdb.core.domain.common.FormulirPendaftaranKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.common.GroupApplicationUseCaseContract
import id.bpdlh.fdb.core.domain.common.HomeUseCaseContract
import id.bpdlh.fdb.core.domain.common.KegiatanUseCaseContract
import id.bpdlh.fdb.core.domain.common.PembiayaanPerhutananUseCaseContract
import id.bpdlh.fdb.core.domain.common.RegistrasiKelompokUseCaseContract
import id.bpdlh.fdb.core.domain.common.RegistrasiPeroranganUseCaseContract
import id.bpdlh.fdb.core.domain.common.SelfAssesmentUseCaseContract
import id.bpdlh.fdb.core.domain.usecase.AlamatUseCase
import id.bpdlh.fdb.core.domain.usecase.AuthUseCase
import id.bpdlh.fdb.core.domain.usecase.DaftarAnggotaPemohonUseCase
import id.bpdlh.fdb.core.domain.usecase.DaftarPemohonNonSosialUseCase
import id.bpdlh.fdb.core.domain.usecase.DaftarPemohonUseCase
import id.bpdlh.fdb.core.domain.usecase.DetailDaftarAnggotaPemohonUseCase
import id.bpdlh.fdb.core.domain.usecase.EditAkunUseCase
import id.bpdlh.fdb.core.domain.usecase.FormKelompokNonSosialUseCase
import id.bpdlh.fdb.core.domain.usecase.FormPermohonanNonSosialUseCase
import id.bpdlh.fdb.core.domain.usecase.FormulirPendaftaranKelompokUseCase
import id.bpdlh.fdb.core.domain.usecase.GroupApplicationUseCase
import id.bpdlh.fdb.core.domain.usecase.HomeUseCase
import id.bpdlh.fdb.core.domain.usecase.KegiatanUseCase
import id.bpdlh.fdb.core.domain.usecase.PembiayaanPerhutananUseCase
import id.bpdlh.fdb.core.domain.usecase.RegistrasiKelompokUseCase
import id.bpdlh.fdb.core.domain.usecase.RegistrasiPeroranganUseCase
import id.bpdlh.fdb.core.domain.usecase.SelfAssesmentUseCase

@Module
class UseCaseModule {
    @Provides
    @AppScope
    fun provideAuthUseCase(repository: AuthRepositoryContract): AuthUseCaseContract =
        AuthUseCase(repository)

    @Provides
    @AppScope
    fun provideEditAkunUseCase(): EditAkunUseCaseContract = EditAkunUseCase()

    @Provides
    @AppScope
    fun provideRegistrasiPeroranganUseCase(repository: RegistrasiPeroranganRepositoryContract): RegistrasiPeroranganUseCaseContract =
        RegistrasiPeroranganUseCase(repository)

    @Provides
    @AppScope
    fun provideAlamatUseCase(repository: AlamatRepositoryContract): AlamatUseCaseContract = AlamatUseCase(repository)

    @Provides
    @AppScope
    fun provideSelfAssesmentUseCase(): SelfAssesmentUseCaseContract = SelfAssesmentUseCase()

    @Provides
    @AppScope
    fun provideDaftarPemohonUseCase(repository: DaftarPermohonanRepositoryContract): DaftarPemohonUseCaseContract =
        DaftarPemohonUseCase(repository)

    @Provides
    @AppScope
    fun provideFormKelompokNonSosialUseCase(repository: FormKelompokNonSosialRepositoryContract): FormKelompokNonSosialUseCaseContract =
        FormKelompokNonSosialUseCase(repository)

    @Provides
    @AppScope
    fun provideDaftarAnggotaPemohonUseCase(repo: DaftarPermohonanRepositoryContract): DaftarAnggotaPemohonUseCaseContract = DaftarAnggotaPemohonUseCase(repo)

    @Provides
    @AppScope
    fun provideDetailDaftarAnggotaPemohonUseCase(repo: DaftarPermohonanRepositoryContract): DetailDaftarAnggotaPemohonUseCaseContract =
        DetailDaftarAnggotaPemohonUseCase(repo)

    @Provides
    @AppScope
    fun provideHomeUseCase(repo: HomeRepositoryContract): HomeUseCaseContract = HomeUseCase(repo)

    @Provides
    @AppScope
    fun provideDaftarPemohonNonSosialUseCase(): DaftarPemohonNonSosialUseCaseContract = DaftarPemohonNonSosialUseCase()

    @Provides
    @AppScope
    fun providePembiayaanPerhutananUseCase(repository: PermohonanPembiayaanRepositoryContract): PembiayaanPerhutananUseCaseContract =
        PembiayaanPerhutananUseCase(repository)

    @Provides
    @AppScope
    fun provideKegiatanUseCase(repository: KegiatanRepositoryContract): KegiatanUseCaseContract =
        KegiatanUseCase(repository)

    @Provides
    @AppScope
    fun provideRegistrasiKelompokUseCase(repository: RegistrasiKelompokRepositoryContract): RegistrasiKelompokUseCaseContract = RegistrasiKelompokUseCase(repository)

    @Provides
    @AppScope
    fun provideFormulirPendaftaranKelompokUseCase(repository: FormulirPendaftaranKelompokRepoContract
    ): FormulirPendaftaranKelompokUseCaseContract = FormulirPendaftaranKelompokUseCase(repository)

    @Provides
    @AppScope
    fun provideGroupApplicationUseCase(repository: GroupApplicationRepositoryContract):
            GroupApplicationUseCaseContract = GroupApplicationUseCase(repository)
    @Provides
    @AppScope
    fun provideFormPermohonanNonSosialUseCase(repository: FormPermohonanNonSosialRepositoryContract):
            FormPermohonanNonSosialUseCaseContract = FormPermohonanNonSosialUseCase(repository)


}