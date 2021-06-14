package ru.salvadorvdali.sampleproject.ui.pages.photopage

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
import ru.salvadorvdali.sampleproject.models.Photo

import ru.salvadorvdali.sampleproject.repository.PhotoRepository
import javax.inject.Inject

@HiltViewModel
class PhotoPageViewModel @Inject constructor(val photoRepository: PhotoRepository): ViewModel(){

    var photos : List<Photo> by mutableStateOf(listOf())
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun getData(id: String){
            viewModelScope.launch {
                photos = photoRepository.getPhoto(albumId = id) }
            }

    fun deletePhoto(photo: Photo){
        GlobalScope.launch(Dispatchers.IO) {
            photoRepository.deletePhoto(photo)
        }
        photos = photos.toMutableList().also { it.remove(photo) }
        Log.d("log", "photos = ${photos.size}")
    }

    fun updateAll(albumId: String){
        viewModelScope.launch {
            _isRefreshing.emit(true)
            photos = photoRepository.updateAll(albumId)
            _isRefreshing.emit(false)
            Log.d("log", " update photos = ${photos.size}")
        }
    }
        }
