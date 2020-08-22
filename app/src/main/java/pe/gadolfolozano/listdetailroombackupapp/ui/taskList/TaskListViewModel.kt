package pe.gadolfolozano.listdetailroombackupapp.ui.taskList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskModel

class TaskListViewModel : ViewModel() {

    val taskListLiveData = MutableLiveData<List<TaskModel>>()

    fun updateTasks(tasks: List<TaskModel>) {
        taskListLiveData.postValue(tasks)
    }
}