package pe.gadolfolozano.listdetailroombackupapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDetailDAO
import pe.gadolfolozano.listdetailroombackupapp.data.dao.UserDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskDetailEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskEntity
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity
import pe.gadolfolozano.listdetailroombackupapp.ui.util.UUIDGenerator

class MainViewModel(
    private val userDAO: UserDAO,
    private val taskDAO: TaskDAO,
    private val taskDetailDAO: TaskDetailDAO,
    private val uuidGenerator: UUIDGenerator
) : ViewModel() {

    fun getUser() {
        viewModelScope.launch {
            val user = userDAO.listAll().firstOrNull()
            Log.d("MainViewModel", "Testing user $user")
        }
    }

    fun saveUser(userName: String) {
        viewModelScope.launch {
            val user = UserEntity(uuid = uuidGenerator.generate(), username = userName)
            userDAO.save(user)
        }
    }

    fun saveTask(taskName: String) {
        viewModelScope.launch {
            val task = TaskEntity(uuid = uuidGenerator.generate(), name = taskName)
            taskDAO.save(task)
        }
    }

    fun saveTaskDetail(taskId: String, detail: String) {
        viewModelScope.launch {
            val taskDetail = TaskDetailEntity(uuid = uuidGenerator.generate(), taskId = taskId, detail = detail)
            taskDetailDAO.save(taskDetail)
        }
    }
}