package id.bpdlh.fdb.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import id.bpdlh.fdb.core.data.sources.local.room.DataDebiturDao
import id.bpdlh.fdb.core.data.sources.local.room.FdbDatabase
import id.bpdlh.fdb.core.data.sources.local.room.FileDao
import id.bpdlh.fdb.core.data.sources.local.room.FormKelompokNonSosialDao
import id.bpdlh.fdb.core.data.sources.local.room.FormulirPembiayaanNonPerhutananSosialDao
import id.bpdlh.fdb.core.data.sources.local.room.JaminanDao
import id.bpdlh.fdb.core.data.sources.local.room.KegiatanDao
import id.bpdlh.fdb.core.data.sources.local.room.MitraDao
import id.bpdlh.fdb.core.data.sources.local.room.RegistrasiPeroranganDao

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(application: Application): FdbDatabase = Room.databaseBuilder(
        application,
        FdbDatabase::class.java, "Fdb.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideRegistrasiPeroranganDao(database: FdbDatabase): RegistrasiPeroranganDao =
        database.registrationDao()

    @Provides
    fun provideFormKelompokNonSosialDao(database: FdbDatabase): FormKelompokNonSosialDao =
        database.formKelompokNonSosialDao()

    @Provides
    fun provideKegiatanDao(database: FdbDatabase): KegiatanDao =
        database.kegiatanDao()

    @Provides
    fun provideFormulirPembiayaanNonPerhutananSosialDao(database: FdbDatabase): FormulirPembiayaanNonPerhutananSosialDao =
        database.formulirPembiayaanNonPerhutananSosialDao()

    @Provides
    fun provideMitraDao(database: FdbDatabase): MitraDao =
        database.mitraDao()

    @Provides
    fun provideFileDao(database: FdbDatabase): FileDao =
        database.fileDao()

    @Provides
    fun provideJaminanDao(database: FdbDatabase): JaminanDao =
        database.jaminanDao()

    @Provides
    fun provideDataDebiturDao(database: FdbDatabase): DataDebiturDao =
        database.getDataDebiturDao()
}