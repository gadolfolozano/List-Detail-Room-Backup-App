package pe.gadolfolozano.listdetailroombackupapp.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.gadolfolozano.listdetailroombackupapp.data.dao.UserDAO
import pe.gadolfolozano.listdetailroombackupapp.data.entity.UserEntity
import pe.gadolfolozano.listdetailroombackupapp.ui.model.UserModel
import pe.gadolfolozano.listdetailroombackupapp.ui.util.UUIDGenerator

class MainViewModel(
    private val userDAO: UserDAO,
    private val uuidGenerator: UUIDGenerator
) : ViewModel() {

    val userLiveData = MediatorLiveData<UserModel>()

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
}