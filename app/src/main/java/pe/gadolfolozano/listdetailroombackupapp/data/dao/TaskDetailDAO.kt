package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskDetailEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity

@Dao
interface TaskDetailDAO {
    @Query("SELECT * FROM task_detail")
    suspend fun listAll(): List<TaskDetailEntity>

    @Query("DELETE FROM task_detail")
    suspend fun deleteAll()
}