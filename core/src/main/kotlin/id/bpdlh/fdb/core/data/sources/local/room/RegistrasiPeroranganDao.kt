package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.data.sources.local.entity.RegistrasiPerorangan
import io.reactivex.Maybe

@Dao
interface RegistrasiPeroranganDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(registrasiPerorangan: RegistrasiPerorangan)

    @Update
    fun update(registrasiPerorangan: RegistrasiPerorangan)

    @Query("SELECT * from registrasi_perorangan WHERE id = 1")
    fun getDraft(): Maybe<RegistrasiPerorangan>
}