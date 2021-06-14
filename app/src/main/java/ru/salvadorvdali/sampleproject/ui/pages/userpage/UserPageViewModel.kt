package ru.salvadorvdali.sampleproject.ui.pages.userpage

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import ru.salvadorvdali.sampleproject.models.User
import ru.salvadorvdali.sampleproject.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserPageViewModel  @Inject constructor( val userRepository: UserRepository ): ViewModel() {

    var users : List<User> by mutableStateOf(listOf())
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    val theme: Boolean by mutableStateOf(false)


init {
    viewModelScope.launch {
        users = userRepository.getUsers()
    }


}
    fun deleteUser(user: User){
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
        users = users.toMutableList().also { it.remove(user) }
        Log.d("log", "users = ${users.size}")
    }

  fun updateAll(){
       viewModelScope.launch {
           _isRefreshing.emit(true)
           users = userRepository.updateAll()
           _isRefreshing.emit(false)
           Log.d("log", " update users = ${users.size}")
       }
    }
}

