package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.domain.entities.DataDebiturEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by hahn on 08/11/23.
 * Project: BPDLH App
 */

@Dao
interface DataDebiturDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DataDebiturEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: DataDebiturEntity)

    @Update
    suspend fun update(entity: DataDebiturEntity)

    @Update
    suspend fun updateAll(vararg entity: DataDebiturEntity)

    @Delete
    suspend fun deleteData(entity: DataDebiturEntity)

    @Query("DELETE FROM data_debitur")
    suspend fun deleteAll()

    @Query("select * from data_debitur ORDER BY id ASC")
    fun getData(): Flow<List<DataDebiturEntity>>

}