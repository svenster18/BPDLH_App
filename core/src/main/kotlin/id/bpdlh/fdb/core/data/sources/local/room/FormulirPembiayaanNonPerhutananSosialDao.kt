package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.data.sources.local.entity.FormulirPembiayaanNonPerhutananSosial
import io.reactivex.Maybe

@Dao
interface FormulirPembiayaanNonPerhutananSosialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial)

    @Update
    fun update(formulirPembiayaanNonPerhutananSosial: FormulirPembiayaanNonPerhutananSosial)

    @Query("SELECT * from formulir_pembiayaan_non_perhutanan_sosial WHERE status = :status ORDER BY id ASC LIMIT 1")
    fun getData(status: Int): Maybe<FormulirPembiayaanNonPerhutananSosial>
}