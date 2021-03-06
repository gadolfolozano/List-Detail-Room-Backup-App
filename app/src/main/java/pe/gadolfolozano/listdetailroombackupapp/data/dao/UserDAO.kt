package pe.gadolfolozano.listdetailroombackupapp.data.dao

import androidx.lifecycle.LiveData
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
    fun fetchUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user LIMIT 1")
    fun getLoggedUser(): UserEntity?

    @Query("SELECT * FROM user")
    suspend fun listAll(): List<UserEntity>

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}