package pe.gadolfolozano.listdetailroombackupapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_detail")
data class TaskDetailEntity(@PrimaryKey val uuid: String, val detail: String)