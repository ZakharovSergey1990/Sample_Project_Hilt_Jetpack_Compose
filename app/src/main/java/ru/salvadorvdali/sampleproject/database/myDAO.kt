package ru.salvadorvdali.sampleproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.salvadorvdali.sampleproject.models.Album
import ru.salvadorvdali.sampleproject.models.Photo
import ru.salvadorvdali.sampleproject.models.User

@Dao
interface MyDao {

    @Insert
    suspend fun insertUsers(users: List<User>)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Delete
    suspend fun deleteUser( user: User )

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Insert
    suspend fun insertAlbums(albums: List<Album>)

    @Query("SELECT * FROM albums")
    suspend fun getAlbums(): List<Album>

    @Query("SELECT * FROM albums WHERE id=:id")
    suspend fun getAlbum(id: String): Album

    @Delete
    suspend fun deleteAlbum( album: Album )

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Query("DELETE FROM photos")
    suspend fun deleteAllPhotos()

    @Insert
    suspend fun insertPhotos( photos: List<Photo>)

    @Query("SELECT * FROM photos")
    suspend fun getPhotos(): List<Photo>

    @Delete
    suspend fun deletePhoto(photo: Photo)

}