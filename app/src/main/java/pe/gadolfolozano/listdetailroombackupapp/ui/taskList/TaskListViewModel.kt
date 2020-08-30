package pe.gadolfolozano.listdetailroombackupapp.ui.taskList

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskEntity
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.UUIDGenerator

class TaskListViewModel(private val taskDAO: TaskDAO, private val uuidGenerator: UUIDGenerator) :
    ViewModel() {

    val taskListLiveData = MediatorLiveData<List<TaskModel>>()

    fun fetchTasks() {
        taskListLiveData.addSource(taskDAO.fetchAll()) { entityTasks ->
            taskListLiveData.postValue(entityTasks.map {
                TaskModel(
                    uuid = it.uuid,
                    name = it.name
                )
            })
        }
    }

    fun saveTask(taskName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = TaskEntity(uuid = uuidGenerator.generate(), name = taskName)
            taskDAO.save(task)
        }
    }
}