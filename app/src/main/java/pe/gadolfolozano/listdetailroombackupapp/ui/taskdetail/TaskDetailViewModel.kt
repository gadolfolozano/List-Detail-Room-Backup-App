package pe.gadolfolozano.listdetailroombackupapp.ui.taskdetail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.gadolfolozano.listdetailroombackupapp.data.dao.TaskDetailDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.TaskDetailEntity
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskDetailModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.UUIDGenerator

class TaskDetailViewModel(
    private val taskDetailDAO: TaskDetailDAO,
    private val uuidGenerator: UUIDGenerator
) : ViewModel() {

    val tasDetailListLiveData = MediatorLiveData<List<TaskDetailModel>>()

    fun fetchTasks(taskId: String) {
        tasDetailListLiveData.addSource(taskDetailDAO.fetchByTask(taskId)) { entityTaskDetails ->
            tasDetailListLiveData.postValue(entityTaskDetails.map {
                TaskDetailModel(uuid = it.uuid, name = it.detail)
            })
        }
    }

    fun saveTaskDetail(taskId: String, detail: String) {
        viewModelScope.launch {
            val taskDetail =
                TaskDetailEntity(uuid = uuidGenerator.generate(), taskId = taskId, detail = detail)
            taskDetailDAO.save(taskDetail)
        }
    }
}