package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun listAll(): List<UserEntity>

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}