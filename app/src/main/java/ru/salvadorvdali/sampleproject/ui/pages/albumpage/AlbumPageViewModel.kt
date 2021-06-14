package ru.salvadorvdali.sampleproject.ui.pages.albumpage

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.salvadorvdali.sampleproject.models.Album
import ru.salvadorvdali.sampleproject.models.User
import ru.salvadorvdali.sampleproject.repository.AlbumRepository
import javax.inject.Inject

@HiltViewModel
class AlbumPageViewModel @Inject constructor(
    val albumRepository: AlbumRepository
): ViewModel(){

    var albums: List<Album> by mutableStateOf(listOf())
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


fun getData(id: String){
    Log.d("log", "userId.value = ${id}")
        viewModelScope.launch {
            albums= albumRepository.getAlbums(userId = id) }
            Log.d("log", "albums.value = ${albums}")
    }

    fun deleteAlbum(album: Album){
        GlobalScope.launch(Dispatchers.IO) {
            albumRepository.deleteAlbum(album)
        }
        albums = albums.toMutableList().also { it.remove(album) }
        Log.d("log", "albums = ${albums.size}")
    }

    fun updateAll(userId: String){
        viewModelScope.launch {
            _isRefreshing.emit(true)
            albums = albumRepository.updateAll(userId)
            _isRefreshing.emit(false)
            Log.d("log", " update albums = ${albums.size}")
        }
    }
}
