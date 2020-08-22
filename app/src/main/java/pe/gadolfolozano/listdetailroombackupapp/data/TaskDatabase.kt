package pe.gadolfolozano.listdetailroombackupapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDetailDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.UserDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskDetailEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        TaskDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun taskDAO(): TaskDAO
    abstract fun taskDetailDAO(): TaskDetailDAO

    companion object {
        const val NAME = "taskDatabase.db"
    }

}