package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.BackupTaskEntity

@Dao
interface BackupTaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: BackupTaskEntity)

    @Query("SELECT * FROM task")
    fun fetchAll(): LiveData<List<BackupTaskEntity>>

    @Query("SELECT * FROM task")
    suspend fun listAll(): List<BackupTaskEntity>

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}