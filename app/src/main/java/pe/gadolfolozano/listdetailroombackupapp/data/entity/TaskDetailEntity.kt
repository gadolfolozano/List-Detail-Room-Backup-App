package pe.gadolfolozano.listdetailroombackupapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "task_detail", foreignKeys =
    [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskDetailEntity(
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "taskId", index = true) val taskId: String,
    val detail: String
)