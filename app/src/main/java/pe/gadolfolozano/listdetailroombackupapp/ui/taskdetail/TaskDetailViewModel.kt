package pe.gadolfolozano.listdetailroombackupapp.ui.taskdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pe.gadolfolozano.listdetailroombackupapp.ui.model.TaskDetailModel

class TaskDetailViewModel : ViewModel() {

    val tasDetailListLiveData = MutableLiveData<List<TaskDetailModel>>()

    fun updateTaskDetails(tasksDetails: List<TaskDetailModel>) {
        tasDetailListLiveData.postValue(tasksDetails)
    }

}