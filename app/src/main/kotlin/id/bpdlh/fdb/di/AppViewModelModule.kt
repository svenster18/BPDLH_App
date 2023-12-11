package id.bpdlh.fdb.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import id.bpdlh.fdb.core.common.utils.ViewModelFactory
import id.bpdlh.fdb.core.common.utils.ViewModelKey
import id.bpdlh.fdb.core.di.AppScope
import id.bpdlh.fdb.features.daftar_anggota_pemohon.viewmodel.DaftarAnggotaPemohonViewModel
import id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial.DaftarAnggotaPemohonNonSosialViewModel
import id.bpdlh.fdb.features.fdk.FormulirPendaftaranKelompokViewModel
import id.bpdlh.fdb.features.fdkns.FormKelompokNonSosialViewModel
import id.bpdlh.fdb.features.fpns.FormPermohonanNonSosialViewModel
import id.bpdlh.fdb.features.home.HomeViewModel
import id.bpdlh.fdb.features.login.LoginViewModel
import id.bpdlh.fdb.features.login.request_forget_password.RequestForgetPasswordViewModel
import id.bpdlh.fdb.features.login.update_password.UpdatePasswordViewModel
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan.PengajuanDaftarPermohonanViewModel
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial.PengajuanDaftarPermohonanNonSosialViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanViewModel
import id.bpdlh.fdb.features.permohonan_pembiayaan.PembiayaanPerhutananViewModel
import id.bpdlh.fdb.features.profile.ProfileViewModel
import id.bpdlh.fdb.features.profile.edit_account.EditAccountViewModel
import id.bpdlh.fdb.features.registration.RegistrasiPeroranganViewModel
import id.bpdlh.fdb.features.registration_kelompok.RegistrasiKelompokViewModel
import id.bpdlh.fdb.features.self_assesment.SelfAssesmentViewModel

@Module
abstract class AppViewModelModule {

    @AppScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    internal abstract fun bindEditAkunViewModel(viewModel: EditAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RequestForgetPasswordViewModel::class)
    internal abstract fun bindForgetPasswordViewModel(viewModel: RequestForgetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdatePasswordViewModel::class)
    internal abstract fun bindUpdatePasswordViewModel(viewModel: UpdatePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrasiPeroranganViewModel::class)
    internal abstract fun bindRegistrasiPeroranganViewModel(viewModel: RegistrasiPeroranganViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelfAssesmentViewModel::class)
    internal abstract fun bindSelfAssesmentViewModel(viewModel: SelfAssesmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FormulirPendaftaranKelompokViewModel::class)
    internal abstract fun bindFormulirPendaftaranKelompokViewModel(viewModel: FormulirPendaftaranKelompokViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PengajuanDaftarPermohonanViewModel::class)
    internal abstract fun bindPengajuanDaftarPermohonanViewModel(viewModel: PengajuanDaftarPermohonanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FormKelompokNonSosialViewModel::class)
    internal abstract fun bindFormKelompokNonSosialViewModel(viewModel: FormKelompokNonSosialViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DaftarAnggotaPemohonViewModel::class)
    internal abstract fun bindDaftarAnggotaPemohonViewModel(viewModel: DaftarAnggotaPemohonViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PengajuanDaftarPermohonanNonSosialViewModel::class)
    internal abstract fun bindPengajuanDaftarPermohonanNonSosialViewModel(viewModel: PengajuanDaftarPermohonanNonSosialViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PembiayaanPerhutananViewModel::class)
    internal abstract fun bindPembiayaanPerhutananViewModel(viewModel: PembiayaanPerhutananViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FormulirPermohonanViewModel::class)
    internal abstract fun bindFormulirPermohonanViewModel(viewModel: FormulirPermohonanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrasiKelompokViewModel::class)
    internal abstract fun bindRegistrasiKelompokViewModel(viewModel: RegistrasiKelompokViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DaftarAnggotaPemohonNonSosialViewModel::class)
    internal abstract fun bindDaftarAnggotaPemohonNonSosialViewModel(viewModel: DaftarAnggotaPemohonNonSosialViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FormPermohonanNonSosialViewModel::class)
    internal abstract fun bindFormPermohonanNonSosialViewModel(viewModel: FormPermohonanNonSosialViewModel): ViewModel

}