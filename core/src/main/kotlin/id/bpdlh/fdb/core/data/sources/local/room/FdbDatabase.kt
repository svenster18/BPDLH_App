package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanNonPerhutananSosial
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanPerhutananSosial
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import id.bpdlh.fdb.core.data.sources.local.entity.Kegiatan
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import id.bpdlh.fdb.core.domain.entities.JaminanEntity

@Database(
    entities = [
        RegistrasiPerorangan::class,
        GroupProfileNonSosial::class,
        FormulirPembiayaanPerhutananSosial::class,
        FormulirPembiayaanNonPerhutananSosial::class,
        Kegiatan::class,
        Mitra::class,
        FileEntity::class,
        JaminanEntity::class,
        DataDebiturEntity::class
    ],
    version = 10,
    exportSchema = false
)
abstract class FdbDatabase : RoomDatabase() {
    abstract fun registrationDao(): RegistrasiPeroranganDao
    abstract fun formKelompokNonSosialDao(): FormKelompokNonSosialDao
    abstract fun kegiatanDao(): KegiatanDao
    abstract fun formulirPembiayaanNonPerhutananSosialDao(): FormulirPembiayaanNonPerhutananSosialDao
    abstract fun mitraDao(): MitraDao
    abstract fun fileDao(): FileDao
    abstract fun jaminanDao(): JaminanDao
    abstract fun getDataDebiturDao(): DataDebiturDao
}