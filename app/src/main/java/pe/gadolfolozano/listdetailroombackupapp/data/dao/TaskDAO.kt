package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity

@Dao
interface TaskDAO {
    @Query("SELECT * FROM task")
    suspend fun listAll(): List<TaskEntity>

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}