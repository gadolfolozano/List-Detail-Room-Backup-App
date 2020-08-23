package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskEntity

@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: TaskEntity)

    @Query("SELECT * FROM task")
    fun fetchAll(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task")
    suspend fun listAll(): List<TaskEntity>

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}