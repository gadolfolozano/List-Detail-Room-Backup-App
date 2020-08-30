package pe.gadolfolozano.listdetailroombackupapp.domain

import java.io.File

class BackupUtil{
    suspend fun createBackup(): File = TODO("must implement this")

    suspend fun restoreBackup(backupFile: File){

    }

    suspend fun cleanDataBase(){

    }
}