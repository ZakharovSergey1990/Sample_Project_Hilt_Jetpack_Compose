package ru.salvadorvdali.sampleproject.ui.pages.albumpage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import ru.salvadorvdali.sampleproject.NavActions
import ru.salvadorvdali.sampleproject.Prefs
import ru.salvadorvdali.sampleproject.models.Album
import ru.salvadorvdali.sampleproject.ui.pages.DeleteDialog

import ru.salvadorvdali.sampleproject.ui.pages.ParentPage
import ru.salvadorvdali.sampleproject.ui.pages.snackbar.MySnackbar
import ru.salvadorvdali.sampleproject.ui.theme.SampleProjectTheme

@ExperimentalAnimationApi
@Composable
fun AlbumPage(vm: AlbumPageViewModel, navAction: NavActions, id: String) {
    vm.getData(id)

    val albumIndex = remember { mutableStateOf(0) }
    val number = remember { mutableStateOf(0) }
    val openDialog = remember { mutableStateOf(false) }
    val search = remember{ mutableStateOf("")}
    val prefs = Prefs(LocalContext.current)


    ParentPage(isRefreshing =false, onRefresh = { vm.updateAll(id) }, title = "Albums",
        navActions = navAction, search = {search.value = it}) {
        LazyColumn {
            if (vm.albums.isNullOrEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(100.dp))
                    }
                }
            } else {
                vm.albums?.filter { it.title.contains(search.value) }.forEachIndexed() { index, album ->
                    item {
                        Log.d("log", "album = $album")
                 AlbumCard(album = album, onDelete = { openDialog.value = true
                     albumIndex.value = index}) {
                     navAction.showPhotos(album.id.toString())
                     prefs.lastAlbum = album.id.toString()
                 }
                        if (openDialog.value) {
                            DeleteDialog( title = "Delete Album", onConfirmClick = {
                                openDialog.value = false
                                Log.d(
                                    "log",
                                    "delete album = ${vm.albums[albumIndex.value]}"
                                )
                                vm.deleteAlbum(vm.albums[albumIndex.value])
                            }, onDismissClick = {
                                openDialog.value = false
                            })


                        }

                    }
                }
            }
        }
        MySnackbar(navActions = navAction, hiltNavGraphViewModel())
    }
}


@ExperimentalAnimationApi
@Composable
fun AlbumCard(
    album: Album,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {

    var extended  =  remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (dragAmount < -50f) {
                        extended.value = true
                    }
                    if (dragAmount > 50f) {
                        extended.value = false
                    }
                }
            }
            ,
        elevation = 5.dp
    ) {

        Row(modifier = Modifier.clickable { onClick() }
            .fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween
            , verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = album.title,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.91f)
                    .fillMaxHeight(),
                fontSize = 18.sp
            )

            AnimatedVisibility(visible = extended.value,
                enter = slideInHorizontally(
                initialOffsetX = { offset -> offset },
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
            ), modifier = Modifier.fillMaxSize().fillMaxHeight(),
                exit = slideOutHorizontally(
                    targetOffsetX = { offset -> offset },
                    animationSpec = tween(durationMillis = 500, easing =  LinearOutSlowInEasing)
                ),
                content = {
                        IconButton(
                            onClick = {
                                onDelete()
                                extended.value = false
                            }, modifier = Modifier
                                .background(MaterialTheme.colors.primary).fillMaxHeight()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "",
                                tint = MaterialTheme.colors.background
                            )
                        }
                }
            )

        }
    }
}