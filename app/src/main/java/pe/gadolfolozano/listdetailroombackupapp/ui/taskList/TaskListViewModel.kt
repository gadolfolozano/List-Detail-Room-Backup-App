package pe.gadolfolozano.listdetailroombackupapp.ui.taskList

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDAO
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskModel

class TaskListViewModel(private val taskDAO: TaskDAO) : ViewModel() {

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
}