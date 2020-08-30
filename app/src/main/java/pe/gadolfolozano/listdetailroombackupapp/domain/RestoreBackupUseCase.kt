package pe.gadolfolozano.listdetailroombackupapp.domain

import java.io.File

class RestoreBackupUseCase(private val backupUtil: BackupUtil) {
    suspend fun execute(backupFile: File){
        backupUtil.restoreBackup(backupFile)
    }
}