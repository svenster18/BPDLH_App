package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.domain.entities.JaminanEntity
import io.reactivex.Flowable

@Dao
interface JaminanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(jaminanEntity: List<JaminanEntity>)

    @Update
    fun update(jaminanEntity: JaminanEntity)

    @Query("SELECT * from jaminan WHERE formulir_id = :formulirId ORDER BY id")
    fun getJaminan(formulirId: Int): Flowable<List<JaminanEntity>>

    @Query("DELETE FROM jaminan WHERE formulir_id = :formulirId")
    fun deleteAll(formulirId: Int)
}