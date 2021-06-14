package ru.salvadorvdali.sampleproject.ui.pages.snackbar

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.salvadorvdali.sampleproject.NavActions
import ru.salvadorvdali.sampleproject.Prefs

@Composable
fun MySnackbar(
    navActions: NavActions,
    vm: SnackbarViewModel
) {
    vm.getAlbum(LocalContext.current)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column() {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                FloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(10.dp)) {
                    Text(text = "+")
                }
            }
if(vm.album!=null){
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "",
                    actionLabel = "",
                    duration = SnackbarDuration.Indefinite
                )
            }
            SnackbarHost(hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(modifier = Modifier.pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount < -50f) {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                            if (dragAmount > 50f) {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        }
                    }, action = {
                        Button(onClick = { vm.album?.let { navActions.showPhotos(it.id.toString()) } }) {
                            Text(text = "open")
                        }
                    }) {
                        Log.d("log", " vm.album?.title? = ${ vm.album?.title?.let { Text(text = it) }}")
                            vm.album?.title?.let { Text(text = it) }
                    }
                })
        }
        }
    }
}
