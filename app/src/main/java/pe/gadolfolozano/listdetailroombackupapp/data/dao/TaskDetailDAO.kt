package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskDetailEntity

@Dao
interface TaskDetailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: TaskDetailEntity)

    @Query("SELECT * FROM task_detail")
    suspend fun listAll(): List<TaskDetailEntity>

    @Query("DELETE FROM task_detail")
    suspend fun deleteAll()
}