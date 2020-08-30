package pe.gadolfolozano.listdetailroombackupapp.domain

class ClearDataBaseUseCase(private val backupUtil: BackupUtil) {
    suspend fun execute() {
        backupUtil.cleanDataBase()
    }
}