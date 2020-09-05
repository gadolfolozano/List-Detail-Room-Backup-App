package pe.gadolfolozano.listdetailroombackupapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.gadolfolozano.listdetailroombackupapp.data.dao.BackupTaskDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.BackupTaskDetailDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.BackupTaskDetailEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.BackupTaskEntity

@Database(
    entities = [
        BackupTaskEntity::class,
        BackupTaskDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BackupTaskDatabase : RoomDatabase() {
    abstract fun backupTaskDAO(): BackupTaskDAO
    abstract fun backupTaskDetailDAO(): BackupTaskDetailDAO

    companion object {
        const val NAME = "backupTaskDatabase.db"
    }

}