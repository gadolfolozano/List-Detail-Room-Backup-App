package pe.gadolfolozano.listdetailroombackupapp.domain

import java.io.File

class CreateBackupUseCase(private val backupUtil: BackupUtil) {
    suspend fun execute(): File {
        return backupUtil.createBackup()
    }
}