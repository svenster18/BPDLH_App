package id.bpdlh.fdb.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.bpdlh.fdb.core.di.AppScope
import id.bpdlh.fdb.features.daftar_anggota_pemohon.DaftarAnggotaPemohonActivity
import id.bpdlh.fdb.features.daftar_anggota_pemohon.DaftarAnggotaPemohonSubmitActivity
import id.bpdlh.fdb.features.daftar_anggota_pemohon.DetailDaftarAnggotaPemohonActivity
import id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial.DaftarAnggotaPemohonNonSosialActivity
import id.bpdlh.fdb.features.daftar_anggota_pemohon_non_sosial.DetailDaftarAnggotaPemohonNonSosialActivity
import id.bpdlh.fdb.features.fdk.DetailKegiatanKelompokFragment
import id.bpdlh.fdb.features.fdk.FormulirPendaftaranKelompokActivity
import id.bpdlh.fdb.features.fdk.InformasiKelompokFragment
import id.bpdlh.fdb.features.fdk.InformasiKepengurusanKelompokFragment
import id.bpdlh.fdb.features.fdk.step3.DetailKegiatanKelompokBottomSheet
import id.bpdlh.fdb.features.fdk.step3.GambaranUmumFragment
import id.bpdlh.fdb.features.fdk.step3.KegiatanKelompokFragment
import id.bpdlh.fdb.features.fdk.step3.MitraUsahaFragment
import id.bpdlh.fdb.features.fdkns.DetailKegiatanKelompokNonSosialFragment
import id.bpdlh.fdb.features.fdkns.DokumenLegalitasKelompokNonSosialFragment
import id.bpdlh.fdb.features.fdkns.FormulirPendaftaranKelompokNonSosialActivity
import id.bpdlh.fdb.features.fdkns.InformasiKelompokNonSosialFragment
import id.bpdlh.fdb.features.fdkns.InformasiKepengurusanKelompokNonSosialFragment
import id.bpdlh.fdb.features.fdkns.KegiatanListFragment
import id.bpdlh.fdb.features.fpns.DataJaminanNonSosialFragment
import id.bpdlh.fdb.features.fpns.DataPermohonanNonSosialFragment
import id.bpdlh.fdb.features.fpns.DokumenLegalitasNonSosialFragment
import id.bpdlh.fdb.features.fpns.FormPermohonanNonSosialActivity
import id.bpdlh.fdb.features.fpns.UsahaYangDibiayaiNonSosialFragment
import id.bpdlh.fdb.features.home.HomeActivity
import id.bpdlh.fdb.features.home.HomeFragment
import id.bpdlh.fdb.features.login.GeneralSuccessBottomSheet
import id.bpdlh.fdb.features.login.LoginActivity
import id.bpdlh.fdb.features.login.request_forget_password.RequestForgetPasswordActivity
import id.bpdlh.fdb.features.login.update_password.UpdatePasswordActivity
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan.DaftarPemohonFragment
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan.PengajuanDaftarPermohonanListActivity
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial.PengajuanDaftarPemohonNonSosialFragment
import id.bpdlh.fdb.features.pengajuan_daftar_permohonan_non_sosial.PengajuanDaftarPermohonanNonSosialActivity
import id.bpdlh.fdb.features.permohonan_pembiayaan.FormulirPermohonanActivity
import id.bpdlh.fdb.features.permohonan_pembiayaan.PembiayaanPerhutananFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.PermohonanPembiayaanActivity
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DataJaminanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DataPermohonanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.DokumenLahanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.JenisLayananBottomSheet
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.UsahaHasilHutanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.non_perhutanan_sosial.UsahaTundaTebangFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.DataPermohonanPinjamanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.DataPermohonanPinjamanLanjutanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.InformasiKeuanganPembiayaanFragment
import id.bpdlh.fdb.features.permohonan_pembiayaan.perhutanan_sosial.JaminanFragment
import id.bpdlh.fdb.features.profile.ChangePasswordActivity
import id.bpdlh.fdb.features.profile.ProfileFragment
import id.bpdlh.fdb.features.profile.edit_account.EditAccountActivity
import id.bpdlh.fdb.features.registration.DataPasanganFragment
import id.bpdlh.fdb.features.registration.GeneralConfirmationBottomSheet
import id.bpdlh.fdb.features.registration.GeneralInformationFragment
import id.bpdlh.fdb.features.registration.RegistrasiPeroranganActivity
import id.bpdlh.fdb.features.registration.alamat.AlamatFragment
import id.bpdlh.fdb.features.registration.dokumen.DokumenLegalitasFragment
import id.bpdlh.fdb.features.registration.dokumen.UploadDokumenDialogFragment
import id.bpdlh.fdb.features.registration.keuangan.InformasiKeuanganFragment
import id.bpdlh.fdb.features.registration.keuangan.TambahUsahaBottomSheet
import id.bpdlh.fdb.features.registration_kelompok.RegistrasiKelompokActivity
import id.bpdlh.fdb.features.registration_kelompok.RegistrasiKelompokStep1Fragment
import id.bpdlh.fdb.features.registration_kelompok.RegistrasiKelompokStep2Fragment
import id.bpdlh.fdb.features.registration_kelompok.RegistrasiKelompokStep3Fragment
import id.bpdlh.fdb.features.self_assesment.SelfAssesmentActivity

@Module
abstract class AppBuilder {
    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindLoginActivity(): LoginActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindProfileFragment(): ProfileFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindEditAccountActivity(): EditAccountActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindChangePasswordActivity(): ChangePasswordActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindForgetPasswordActivity(): RequestForgetPasswordActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindUpdatePasswordActivity(): UpdatePasswordActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindUpdateSelfAssemntActivity(): SelfAssesmentActivity

    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindHomeActivity(): HomeActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindRegistrasiPeroranganActivity(): RegistrasiPeroranganActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindGeneralInformationFragment(): GeneralInformationFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataPasanganFragment(): DataPasanganFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindAlamatFragment(): AlamatFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindInformasiKeuanganFragment(): InformasiKeuanganFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDokumenLegalitasFragment(): DokumenLegalitasFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindUploadDokumenDialogFragment(): UploadDokumenDialogFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindGeneralConfirmationBottomSheet(): GeneralConfirmationBottomSheet

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindTambahUsahaBottomSheet(): TambahUsahaBottomSheet

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindFormulirPendaftaranKelompokActivity(): FormulirPendaftaranKelompokActivity

    @AppScope
    @ContributesAndroidInjector
    internal abstract fun bindFormulirPendaftaranKelompokNonSosialActivity(): FormulirPendaftaranKelompokNonSosialActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindInformasiKelompokSosialNonFragment(): InformasiKelompokNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindInformasiKepengurusanKelompokNonSosialFragment(): InformasiKepengurusanKelompokNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDetailKegiatanKelompokNonSosialFragment(): DetailKegiatanKelompokNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDokumenLegalitasKelompokNonSosialFragment(): DokumenLegalitasKelompokNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindPengajuanDaftarPermohonanListActivity(): PengajuanDaftarPermohonanListActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDaftarPemohonFragment(): DaftarPemohonFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindInformasiKelompokFragment(): InformasiKelompokFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDetailKegiatanKelompokFragment(): DetailKegiatanKelompokFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindKegiatanKelompokFragment(): KegiatanKelompokFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindMitraUsahaFragment(): MitraUsahaFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindGambaranUmumFragment(): GambaranUmumFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDaftarAnggotaPemohonActivity(): DaftarAnggotaPemohonActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindInformasiKepengurusanKelompokFragment(): InformasiKepengurusanKelompokFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDetailDaftarAnggotaPemohonActivity(): DetailDaftarAnggotaPemohonActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindHomeFragment(): HomeFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindPengajuanDaftarPermohonanNonSosialActivity(): PengajuanDaftarPermohonanNonSosialActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindPengajuanDaftarPermohonanNonSosialFragment(): PengajuanDaftarPemohonNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDaftarAnggotaPemohonNonSosialActivity(): DaftarAnggotaPemohonNonSosialActivity

    @AppScope
    @ContributesAndroidInjector
    internal abstract fun binDetailDaftarAnggotaPemohonNonSosialActivity(): DetailDaftarAnggotaPemohonNonSosialActivity

    @AppScope
    @ContributesAndroidInjector
    internal abstract fun bindDaftarPermohonanFragment(): PembiayaanPerhutananFragment

    @AppScope
    @ContributesAndroidInjector
    internal abstract fun bindPermohonanPembiayaanPerhutananActivity(): PermohonanPembiayaanActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindFormulirPermohonanActivity(): FormulirPermohonanActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindInformasiKeuanganPembiayaanFragment(): InformasiKeuanganPembiayaanFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataPermohonanPinjamanFragment(): DataPermohonanPinjamanFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataPermohonanPinjamanLanjutanFragment(): DataPermohonanPinjamanLanjutanFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindJaminanFragment(): JaminanFragment

    @AppScope
    @ContributesAndroidInjector
    internal abstract fun bindKegiatanListFragment(): KegiatanListFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindRegistrasiKelompokActivity(): RegistrasiKelompokActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindRegistrasiKelompokStep1Fragment(): RegistrasiKelompokStep1Fragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindRegistrasiKelompokStep2Fragment(): RegistrasiKelompokStep2Fragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindRegistrasiKelompokStep3Fragment(): RegistrasiKelompokStep3Fragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindJenisLayananBottomSheet(): JenisLayananBottomSheet

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataPermohonanFragment(): DataPermohonanFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindUsahaTundaTebangFragment(): UsahaTundaTebangFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindUsahaHasilHutanFragment(): UsahaHasilHutanFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataJaminanFragment(): DataJaminanFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDokumenLahanFragment(): DokumenLahanFragment

    @AppScope
    @ContributesAndroidInjector
    internal abstract fun bindGeneralSuccessBottomSheet(): GeneralSuccessBottomSheet

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindFormPermohonanNonSosialActivity(): FormPermohonanNonSosialActivity

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataJaminanNonSosialFragment(): DataJaminanNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDataPermohonanNonSosialFragment(): DataPermohonanNonSosialFragment
    
    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDokumenPenguasaanLahanNonSosialFragment(): DokumenLegalitasNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindUsahaYangDibiayaiNonSosialFragment(): UsahaYangDibiayaiNonSosialFragment

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDetailKetiaganKelompokBottomSheet(): DetailKegiatanKelompokBottomSheet

    @AppScope
    @ContributesAndroidInjector(modules = [AppViewModelModule::class])
    internal abstract fun bindDaftarAnggotaPemohonSubmitActivity(): DaftarAnggotaPemohonSubmitActivity
}