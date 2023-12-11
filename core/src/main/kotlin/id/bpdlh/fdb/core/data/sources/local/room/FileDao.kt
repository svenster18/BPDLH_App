package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.data.sources.local.entity.FileEntity
import io.reactivex.Flowable

@Dao
interface FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fileEntity: List<FileEntity>)

    @Update
    fun update(fileEntity: FileEntity)

    @Query("SELECT * from file WHERE parent_id = :parentId AND type = :type ORDER BY id")
    fun getFile(parentId: Int, type: Int): Flowable<List<FileEntity>>

    @Query("DELETE FROM file WHERE parent_id = :parentId AND type = :type")
    fun deleteAll(parentId: Int, type: Int)
}