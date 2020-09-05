package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.BackupTaskDetailEntity

@Dao
interface BackupTaskDetailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: BackupTaskDetailEntity)

    @Query("SELECT * FROM task_detail WHERE taskId = :taskId")
    fun fetchByTask(taskId: String): LiveData<List<BackupTaskDetailEntity>>

    @Query("SELECT * FROM task_detail")
    suspend fun listAll(): List<BackupTaskDetailEntity>

    @Query("DELETE FROM task_detail")
    suspend fun deleteAll()
}