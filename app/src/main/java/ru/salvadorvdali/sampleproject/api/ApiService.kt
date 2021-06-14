package ru.salvadorvdali.sampleproject.api

import retrofit2.http.GET
import ru.salvadorvdali.sampleproject.models.Album
import ru.salvadorvdali.sampleproject.models.Photo
import ru.salvadorvdali.sampleproject.models.User

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    @GET("albums")
    suspend fun getAlbums(): List<Album>
    @GET("photos")
    suspend fun getPhotos(): List<Photo>
}