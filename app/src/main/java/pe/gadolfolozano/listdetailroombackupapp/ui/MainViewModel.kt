package pe.gadolfolozano.listdetailroombackupapp.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.gadolfolozano.listdetailroombackupapp.data.dao.UserDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity
import pe.gadolfolozano.listdetailroombackupapp.domain.ClearDataBaseUseCase
import pe.gadolfolozano.listdetailroombackupapp.domain.CreateBackupUseCase
import pe.gadolfolozano.listdetailroombackupapp.domain.RestoreBackupUseCase
import pe.gadolfolozano.listdetailroombackupapp.ui.model.UserModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.SingleLiveEvent
import pe.gadolfolozano.listdetailroombackupapp.ui.util.UUIDGenerator
import java.io.File

class MainViewModel(
    private val userDAO: UserDAO,
    private val createBackupUseCase: CreateBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val clearDataBaseUseCase: ClearDataBaseUseCase,
    private val uuidGenerator: UUIDGenerator
) : ViewModel() {

    val userLiveData = MediatorLiveData<UserModel>()

    val createBackupFileResult = SingleLiveEvent<File>()

    fun fetchUser() {
        userLiveData.addSource(userDAO.fetchUser()) { userEntityList ->
            userEntityList.firstOrNull()?.let { user ->
                userLiveData.postValue(UserModel(user.username))
            }
        }
    }

    fun saveUser(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val loggedUser = userDAO.getLoggedUser()
            if (loggedUser != null) {
                userDAO.save(loggedUser.copy(username = userName))
            } else {
                val user = UserEntity(uuid = uuidGenerator.generate(), username = userName)
                userDAO.save(user)
            }
        }
    }

    fun createBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            val backupFile = createBackupUseCase.execute()
            createBackupFileResult.postValue(backupFile)
        }
    }

    fun restoreBackup(backupFile: File) {
        viewModelScope.launch(Dispatchers.IO) {
            restoreBackupUseCase.execute(backupFile)
        }
    }

    fun clearDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDataBaseUseCase.execute()
        }
    }
}