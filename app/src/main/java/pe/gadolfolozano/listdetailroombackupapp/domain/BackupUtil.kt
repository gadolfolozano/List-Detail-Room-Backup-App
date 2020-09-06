package pe.gadolfolozano.listdetailroombackupapp.domain

import android.content.Context
import android.os.Environment
import pe.gadolfolozano.listdetailroombackupapp.data.BackupTaskDatabase
import pe.gadolfolozano.listdetailroombackupapp.data.dao.*
import pe.gadolfolozano.listdetailroombackupapp.data.entity.mapToBackup
import pe.gadolfolozano.listdetailroombackupapp.data.entity.mapToEntity
import pe.gadolfolozano.listdetailroombackupapp.ui.util.unzip
import pe.gadolfolozano.listdetailroombackupapp.ui.util.zip
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BackupUtil(
    private val context: Context,
    private val userDAO: UserDAO,
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
        cleanBackupDataBase()
        copyRestoredFiles(backupFile)
        cleanDataBase(false)
        fillDataBase()
        cleanBackupDataBase()
    }

    suspend fun cleanDataBase(shouldCleanUserTable: Boolean = true) {
        if (shouldCleanUserTable) {
            taskDetailDAO.deleteAll()
        }
        taskDAO.deleteAll()
        userDAO.deleteAll()
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

    private fun copyBackupDataBase() {
        context.getDatabasePath(BackupTaskDatabase.NAME)
            .copyTo(target = File(getBackupFolder(), BACKUP_DATA_BASE_NAME), overwrite = true)
        context.getDatabasePath(BackupTaskDatabase.NAME + "-shm")
            .copyTo(target = File(getBackupFolder(), BACKUP_DATA_BASE_SHM_NAME), overwrite = true)
        context.getDatabasePath(BackupTaskDatabase.NAME + "-wal")
            .copyTo(target = File(getBackupFolder(), BACKUP_DATA_BASE_WAL_NAME), overwrite = true)
    }

    private fun copyRestoredFiles(backupFile: File) {
        getBackupFolder().deleteRecursively()
        require(backupFile.exists()) { "backup file at ${backupFile.absolutePath} does not exists" }

        backupFile.unzip(getBackupFolder().parentFile)

        require(validateBackupFolderStructure()) { "Invalid backup structure" }

        File(getBackupFolder(), BACKUP_DATA_BASE_NAME).copyTo(
            target = context.getDatabasePath(BackupTaskDatabase.NAME), overwrite = true
        )
        File(getBackupFolder(), BACKUP_DATA_BASE_SHM_NAME).copyTo(
            target = context.getDatabasePath(BackupTaskDatabase.NAME + "-shm"), overwrite = true
        )
        File(getBackupFolder(), BACKUP_DATA_BASE_WAL_NAME).copyTo(
            target = context.getDatabasePath(BackupTaskDatabase.NAME + "-wal"), overwrite = true
        )

        getBackupFolder().deleteRecursively()
    }

    private suspend fun fillDataBase() {
        val allBackupTasks = backupTaskDAO.listAll()
        allBackupTasks.forEach { taskDAO.save(it.mapToEntity()) }

        val allBackupTaskDetails = backupTaskDetailDAO.listAll()
        allBackupTaskDetails.forEach { taskDetailDAO.save(it.mapToEntity()) }
    }

    private fun validateBackupFolderStructure(): Boolean {
        val backupFolder = getBackupFolder()
        if (backupFolder.exists() && backupFolder.isDirectory) {
            val backupDatabaseFile = File(getBackupFolder(), BACKUP_DATA_BASE_NAME)
            val backupDatabaseSHMFile = File(getBackupFolder(), BACKUP_DATA_BASE_SHM_NAME)
            val backupDatabaseWALFile = File(getBackupFolder(), BACKUP_DATA_BASE_WAL_NAME)

            return backupDatabaseFile.exists() && backupDatabaseFile.isFile &&
                    backupDatabaseSHMFile.exists() && backupDatabaseSHMFile.isFile &&
                    backupDatabaseWALFile.exists() && backupDatabaseWALFile.isFile
        }
        return false
    }

    private fun getBackupFolder(): File {
        val backupFolder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "Backup"
        )

        if (!backupFolder.exists()) {
            backupFolder.mkdirs()
        }
        return backupFolder
    }

    private fun getDownloadsFolder(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    companion object {
        private const val BACKUP_DATE_FORMAT = "MMM dd, YYYY hh:mm a"
        private const val BACKUP_FILE_NAME_FORMAT = "Backup %s.zip"
        private const val BACKUP_DATA_BASE_NAME = "backup_data"
        private const val BACKUP_DATA_BASE_SHM_NAME = "backup_data-shm"
        private const val BACKUP_DATA_BASE_WAL_NAME = "backup_data-wal"
    }
}