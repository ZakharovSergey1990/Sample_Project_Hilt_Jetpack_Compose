package ru.salvadorvdali.sampleproject.ui.pages


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.squareup.picasso.Picasso
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.salvadorvdali.sampleproject.NavActions
import ru.salvadorvdali.sampleproject.Prefs
import ru.salvadorvdali.sampleproject.ui.theme.SampleProjectTheme
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun LoadPicture(url: String){

    val image = remember{ mutableStateOf<Bitmap?>(null)}

    val target = object: com.squareup.picasso.Target{
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            image.value = bitmap
        }
    }

    Picasso.get().load(url).into(target)
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (image.value == null) {
            CircularProgressIndicator()
        } else {
            image.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}


@Composable
fun ParentPage(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    title: String,
    backIcon: Boolean = true,
    navActions: NavActions,
    search: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    val prefs = Prefs(LocalContext.current)
    val theme = remember { mutableStateOf(prefs.myTheme) }
    val search = remember { mutableStateOf("") }
    Log.d("log", "user theme = ${theme.value}")
    val scaffoldState = rememberScaffoldState()

    SampleProjectTheme(darkTheme = theme.value) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                Column() {
                    if(backIcon) {
                    TopAppBar(title = { Text(text = title)},
                        backgroundColor= MaterialTheme.colors.primary,
                        navigationIcon = {
                            IconButton(onClick = {
                                navActions.goBack()
                            }) {
                                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                            }
                    },
                 )}
                    else{
                        TopAppBar(title = { Text(text = title)},
                            backgroundColor= MaterialTheme.colors.primary,)
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primary),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = search.value, onValueChange = {search.value = it; search(it)}, modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(8.dp),
                            textStyle = TextStyle(color = MaterialTheme.colors.background),
                            label = { Text(text = "Search", color = MaterialTheme.colors.background) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "", tint = MaterialTheme.colors.background
                                )
                            },
                            keyboardActions = KeyboardActions()
                        )
                        IconButton(onClick = {
                            prefs.myTheme = !theme.value
                            theme.value = !theme.value
                            Log.d("log", "user prefs.myTheme = ${prefs.myTheme}")
                        }, modifier = Modifier.padding(5.dp)) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "", tint = MaterialTheme.colors.background)
                        }
                    }
                }

            },
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
//            drawerContent = {
//                Text(text = "User")
//                Text(text = "Albums")
//                Text(text = "Photos")
//            },
        ) {

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = onRefresh,
            ) {
                content()
            }
        }
    }
}

@Composable
fun DeleteDialog(
    title: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit

){

    AlertDialog(
        onDismissRequest = {
            onDismissClick()
        },
        title = {
            Text(text = title)
        },
        text = {
            Text("Are you sure?")
        },
        confirmButton = {
            Button(

                onClick = {
           onConfirmClick()
                }) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(

                onClick = {
                   onDismissClick()
                }) {
                Text("No")
            }
        }
    )
}

@Composable
 fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    // Этот " Анимируемый` сохраняет горизонтальное смещение для элемента.
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        // Используется для расчета положения установки анимации броска.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                // Дождитесь события приземления.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Прервать любую текущую анимацию.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                // Дождитесь событий перетаскивания.
                awaitPointerEventScope {
                  //  horiz
                    horizontalDrag(pointerId) { change ->
                        launch {
                            // Overwrite the `Animatable` value while the element is dragged.
                            // Перезапишите значение "Animatable" во время перетаскивания элемента.
                            offsetX.snapTo(offsetX.value + change.positionChange().x)

                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetX.updateBounds(
                    lowerBound = (-size.width).toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    Log.d("log", "targetOffsetX.absoluteValue = ${targetOffsetX.absoluteValue}")
                    Log.d("log", "size.width/2 = ${size.width/2}")
                    if (targetOffsetX.absoluteValue <= size.width/2) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                      //  offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                        offsetX.animateTo(targetValue = size.width.toFloat(), initialVelocity = velocity)
                        offsetX.snapTo(0f)
                    }
                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}
