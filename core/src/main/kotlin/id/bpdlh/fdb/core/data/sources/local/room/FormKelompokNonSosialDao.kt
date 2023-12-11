package id.bpdlh.fdb.core.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.bpdlh.fdb.core.data.sources.local.entity.GroupProfileNonSosial
import io.reactivex.Maybe

@Dao
interface FormKelompokNonSosialDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(groupProfileNonSosial: GroupProfileNonSosial)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(groupProfileNonSosial: GroupProfileNonSosial)

    @Query("SELECT * from group_profile_non_sosial LIMIT 1")
    fun getDraft(): Maybe<GroupProfileNonSosial>

    @Query("DELETE from group_profile_non_sosial")
    fun delete()
}