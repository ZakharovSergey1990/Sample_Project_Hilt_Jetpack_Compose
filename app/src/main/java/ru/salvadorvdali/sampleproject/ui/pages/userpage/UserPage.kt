package ru.salvadorvdali.sampleproject.ui.pages.userpage

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavAction
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.salvadorvdali.sampleproject.NavActions
import ru.salvadorvdali.sampleproject.Prefs
import ru.salvadorvdali.sampleproject.models.User
import ru.salvadorvdali.sampleproject.ui.pages.DeleteDialog

import ru.salvadorvdali.sampleproject.ui.pages.ParentPage
import ru.salvadorvdali.sampleproject.ui.pages.snackbar.MySnackbar
import ru.salvadorvdali.sampleproject.ui.theme.SampleProjectTheme
import kotlin.math.roundToInt


@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun UserPage ( vm: UserPageViewModel, navAction: NavActions) {


    Log.d("log", "vm.users = ${vm.users}")
    val openDialog = remember { mutableStateOf(false) }
    val userIndex = remember { mutableStateOf(0) }
    val isRefreshing by vm.isRefreshing.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val search = remember{ mutableStateOf("")}


    ParentPage(isRefreshing = isRefreshing, onRefresh = { vm.updateAll() }, title = "Users", backIcon = false, navActions = navAction,
    search = {search.value = it}) {
        LazyColumn() {
            item() {
                vm.users?.filter { it.name.contains(search.value) }.forEachIndexed() { index, user ->
                    Log.d("log", "user = ${user}")
                    UserCard(user = user, onClick = {
                        navAction.showAlbums(user.id.toString())
                    }, onLongClick = {
                        userIndex.value = index
                        openDialog.value = true
                    })
                    if (openDialog.value) {
                   DeleteDialog( title = "Delete User", onConfirmClick = {
                       openDialog.value = false
                       Log.d(
                           "log",
                           "delete user = ${vm.users[userIndex.value]}"
                       )
                       vm.deleteUser(vm.users[userIndex.value])
                   }, onDismissClick = {
                       openDialog.value = false
                   })


                    }
                }
            }
        }
                MySnackbar(navActions = navAction, hiltViewModel())
            }
}



@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun UserCard(user: User, onClick: () -> Unit, onLongClick: () -> Unit ){
    var showList by remember{ mutableStateOf(false)}
    var currentState by remember { mutableStateOf(false) }
    val value by animateFloatAsState(
        targetValue = if(currentState) 0f else -90f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        )
    )
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier.testTag("UserCard").combinedClickable(onLongClick = {
            onLongClick()
        },
            onClick = { onClick() })) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.name,
                    modifier = Modifier.padding(10.dp).testTag("name"),
                    fontSize = 20.sp
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .clickable {
                            currentState = !currentState
                            showList = !showList
                        }
                        .rotate(value))
            }

            AnimatedVisibility(visible = showList,
                enter = slideInVertically(initialOffsetY = { -40 })+ expandVertically(expandFrom = Alignment.Top
                )+ fadeIn(), exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Column() {
                    Text(text = user.email, modifier = Modifier.testTag("email"))
                    Text(text = user.phone, modifier = Modifier.testTag("phone"))
                    Text(text = user.website, modifier = Modifier.testTag("website"))
                    Text(text = user.address.toString(), modifier = Modifier.testTag("address"))
                }
            }


        }
    }
}

