package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.data.sources.local.entity.Mitra
import io.reactivex.Flowable

@Dao
interface MitraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mitra: List<Mitra>)

    @Update
    fun update(mitra: Mitra)

    @Query("SELECT * from mitra WHERE formulir_id = :formulirId ORDER BY id")
    fun getMitra(formulirId: Int): Flowable<List<Mitra>>

    @Query("DELETE FROM mitra WHERE formulir_id = :formulirId")
    fun deleteAll(formulirId: Int)
}