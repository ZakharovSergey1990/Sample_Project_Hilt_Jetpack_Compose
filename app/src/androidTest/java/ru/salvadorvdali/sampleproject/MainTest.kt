package ru.salvadorvdali.sampleproject

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.salvadorvdali.sampleproject.ui.pages.userpage.UserPage
import ru.salvadorvdali.sampleproject.ui.pages.userpage.UserPageViewModel
import ru.salvadorvdali.sampleproject.ui.theme.SampleProjectTheme
import java.lang.ref.WeakReference


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@HiltAndroidTest
class MainTest {
    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun commonTest() {

        hiltRule.inject()
        composeTestRule.setContent {
            val navController = rememberNavController()
            val actions = NavActions(navController)

            SampleProjectTheme {
                UserPage(
                    vm = composeTestRule.activity.viewModels<UserPageViewModel>().value,
                    navAction = actions
                )
            }
        }
        composeTestRule.apply {
            Thread.sleep(1000)
            onAllNodesWithTag("UserCard").assertCountEquals(2)[0].assertTextEquals("Leanne Graham")
            onAllNodesWithContentDescription("Arrow").assertCountEquals(2)[0].performClick()
            onAllNodesWithTag("UserCard").assertCountEquals(2)[0]
                .assertTextEquals("Leanne Graham, Sincere@april.biz, 1-770-736-8031 x56442, hildegard.org, Address(street=Kulas Light, suite=Apt. 556, city=Gwenborough, zipcode=92998-3874, geo=Geo(lat=-37.3159, lng=81.1496))")



        }
    }
}