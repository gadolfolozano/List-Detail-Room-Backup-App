package pe.gadolfolozano.listdetailroombackupapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(@PrimaryKey val uuid: String, val username: String)