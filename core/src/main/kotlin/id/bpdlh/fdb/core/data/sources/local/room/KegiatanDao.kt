package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.data.sources.local.entity.Kegiatan
import io.reactivex.Maybe

@Dao
interface KegiatanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(kegiatan: List<Kegiatan>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(kegiatan: Kegiatan)

    @Query("SELECT * from kegiatan")
    fun getKegiatan(): Maybe<List<Kegiatan>>

    @Query("DELETE from kegiatan")
    fun deleteAll()
}