package ru.salvadorvdali.sampleproject.ui.pages.photopage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.salvadorvdali.sampleproject.NavActions
import ru.salvadorvdali.sampleproject.models.Photo
import ru.salvadorvdali.sampleproject.ui.pages.LoadPicture
import ru.salvadorvdali.sampleproject.ui.pages.ParentPage
import ru.salvadorvdali.sampleproject.ui.pages.swipeToDismiss
import ru.salvadorvdali.sampleproject.ui.theme.SampleProjectTheme

@Composable
fun PhotoPage (vm: PhotoPageViewModel, navAction: NavActions, id: String){
    Log.d("log", "id = $id")
    vm.getData(id)
    val search = remember{ mutableStateOf("")}
    ParentPage(isRefreshing =false, onRefresh = { vm.updateAll(id) }, title = "Photos",
        navActions = navAction, search = {search.value = it}) {
        LazyColumn() {
            if (vm.photos.isNullOrEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .fillMaxHeight(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(100.dp))
                    }
                }
            } else {
                    items( vm.photos?.filter { it.title?.contains(search.value) == true }) {
              PhotoCard(photo = it){
                  vm.deletePhoto(it)
              }
                    }
                }
            }
        }
    }


@Composable
fun PhotoCard(photo: Photo, delete: () -> Unit){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { },
        elevation = 5.dp
    ) {
        Column(modifier = Modifier. swipeToDismiss {
            Log.d("log", "delete Photo")
            delete() }) {
            photo.url?.let { it1 -> LoadPicture(url = it1) }
            Text(
                text = photo.title.toString(),
                modifier = Modifier.padding(10.dp),
                fontSize = 18.sp
            )
        }
    }
}