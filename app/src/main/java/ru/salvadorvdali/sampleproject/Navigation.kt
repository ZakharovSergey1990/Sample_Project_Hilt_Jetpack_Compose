package ru.salvadorvdali.sampleproject

import androidx.navigation.NavController



class NavActions(val navController: NavController) {

    val goBack: () -> Unit = {
        navController.popBackStack()
    }
    val showUsers: () -> Unit = {
        navController.navigate(Page.Users)
    }
    val showAlbums: (id: String) -> Unit = {
        navController.navigate(Page.Albums.replace("{id}", it))
    }
    val showPhotos: (id: String) -> Unit = {
        navController.navigate(Page.Photos.replace("{id}", it))
    }
}