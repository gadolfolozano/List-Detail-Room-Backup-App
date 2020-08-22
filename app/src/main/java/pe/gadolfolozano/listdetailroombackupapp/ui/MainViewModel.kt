package pe.gadolfolozano.listdetailroombackupapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.gadolfolozano.listdetailroombackupapp.data.dao.UserDAO

class MainViewModel(private val userDAO: UserDAO) : ViewModel () {

    fun getUser(){
        viewModelScope.launch {
            val user = userDAO.listAll().firstOrNull()
            Log.d("MainViewModel","Testing user $user")
        }
    }
}