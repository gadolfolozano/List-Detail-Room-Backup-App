package pe.gadolfolozano.listdetailroombackupapp.domain

import android.content.Context
import android.os.Environment
import pe.gadolfolozano.listdetailroombackupapp.data.BackupTaskDatabase
import pe.gadolfolozano.listdetailroombackupapp.data.dao.BackupTaskDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.BackupTaskDetailDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDetailDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.mapToBackup
import pe.gadolfolozano.listdetailroombackupapp.ui.util.zip
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupUtil(
    private val context: Context,
    private val taskDAO: TaskDAO,
    private val taskDetailDAO: TaskDetailDAO,
    private val backupTaskDAO: BackupTaskDAO,
    private val backupTaskDetailDAO: BackupTaskDetailDAO
) {
    suspend fun createBackup(): File {
        cleanBackupDataBase()
        fillBackupDataBase()
        val backupFile = createBackupFile()
        cleanBackupDataBase()
        return backupFile
    }

    suspend fun restoreBackup(backupFile: File) {

    }

    suspend fun cleanDataBase() {
        taskDAO.deleteAll()
        taskDetailDAO.deleteAll()
    }

    private suspend fun cleanBackupDataBase() {
        backupTaskDAO.deleteAll()
        backupTaskDetailDAO.deleteAll()
    }

    private suspend fun fillBackupDataBase() {
        val allTasks = taskDAO.listAll()
        allTasks.forEach { backupTaskDAO.save(it.mapToBackup()) }

        val allTaskDetails = taskDetailDAO.listAll()
        allTaskDetails.forEach { backupTaskDetailDAO.save(it.mapToBackup()) }
    }

    private suspend fun createBackupFile(): File {
        //Clean backupFolder before creating backup files
        getBackupFolder().listFiles()?.forEach { it.deleteRecursively() }

        copyBackupDataBase()

        val simpleDateFormat = SimpleDateFormat(BACKUP_DATE_FORMAT, Locale.getDefault())
        val formattedDate = simpleDateFormat.format(Date())
        val fileName = BACKUP_FILE_NAME_FORMAT.format(formattedDate)

        val zippedFile = File(getDownloadsFolder(), fileName)
        getBackupFolder().zip(zippedFile)

        //Delete backup folder
        getBackupFolder().deleteRecursively()

        require(zippedFile.exists()) { "Failed creating backup file" }

        return zippedFile
    }

    private fun getBackupFolder(): File {
        val backupFolder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "Backup"
        )

        if(!backupFolder.exists()){
            backupFolder.mkdirs()
        }
        return backupFolder
    }

    private fun getDownloadsFolder(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    private fun copyBackupDataBase() {
        context.getDatabasePath(BackupTaskDatabase.NAME)
            .copyTo(File(getBackupFolder(), BACKUP_DATA_BASE_NAME))
        context.getDatabasePath(BackupTaskDatabase.NAME+ "-shm")
            .copyTo(File(getBackupFolder(), BACKUP_DATA_BASE_SHM_NAME))
        context.getDatabasePath(BackupTaskDatabase.NAME+ "-wal")
            .copyTo(File(getBackupFolder(), BACKUP_DATA_BASE_WAL_NAME))
    }

    companion object {
        private const val BACKUP_DATE_FORMAT = "MMM dd, YYYY hh:mm a"
        private const val BACKUP_FILE_NAME_FORMAT = "Backup %s.zip"
        private const val BACKUP_DATA_BASE_NAME = "backup_data"
        private const val BACKUP_DATA_BASE_SHM_NAME = "backup_data-shm"
        private const val BACKUP_DATA_BASE_WAL_NAME = "backup_data-wal"
    }
}