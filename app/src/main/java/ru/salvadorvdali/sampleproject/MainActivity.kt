package ru.salvadorvdali.sampleproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.salvadorvdali.sampleproject.ui.pages.albumpage.AlbumPage
import ru.salvadorvdali.sampleproject.ui.pages.photopage.PhotoPage
import ru.salvadorvdali.sampleproject.ui.pages.userpage.UserPage
import ru.salvadorvdali.sampleproject.ui.theme.SampleProjectTheme


object Page {
    const val Users = "/users"
    const val Albums = "/albums/{id}"
    const val Photos = "/photos/{id}"}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
val prefs = Prefs(LocalContext.current)
val theme = remember{ mutableStateOf(prefs.myTheme)}
            Log.d("log", "mainactivity theme = ${theme.value}")
            SampleProjectTheme(darkTheme = theme.value) {
                    val navController = rememberNavController()
                    val actions = NavActions(navController)

                    NavHost(navController = navController, startDestination = Page.Users) {
                        composable(Page.Albums) {
                            val id = it.arguments?.getString("id").orEmpty()
                            AlbumPage(vm = hiltViewModel(it), navAction = actions, id = id)
                        }
                        composable(Page.Photos) {
                            val id = it.arguments?.getString("id").orEmpty()
                            PhotoPage(vm = hiltViewModel(it), navAction = actions, id = id)
                        }
                        composable(Page.Users) {
                            UserPage(vm = hiltViewModel(it), navAction = actions)
                        }
                    }

            }
        }
    }
}

class Prefs (context: Context)
{
    private val THEME = "theme"
    private val ALBUM = "album"
private val PREF_NAME = "settings"

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var myTheme: Boolean
        get() = preferences.getBoolean(THEME, false)
        set(value) = preferences.edit().putBoolean(THEME, value).apply()

    var lastAlbum: String
        get() = preferences.getString(ALBUM, "").toString()
        set(value) = preferences.edit().putString(ALBUM, value).apply()
}