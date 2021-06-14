package ru.salvadorvdali.sampleproject.ui.pages.snackbar

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.salvadorvdali.sampleproject.Prefs
import ru.salvadorvdali.sampleproject.models.Album
import ru.salvadorvdali.sampleproject.repository.AlbumRepository
import javax.inject.Inject

@HiltViewModel
class SnackbarViewModel
    @Inject constructor(val albumRepository: AlbumRepository): ViewModel(){
    var album: Album? by mutableStateOf(null)

   fun getAlbum(context: Context){
       viewModelScope.launch {
           val prefs = Prefs(context = context)
           album = albumRepository.getAlbum(prefs.lastAlbum)
       }
    }
}