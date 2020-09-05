package pe.gadolfolozano.listdetailroombackupapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class BackupTaskEntity(@PrimaryKey val uuid: String, val name: String)