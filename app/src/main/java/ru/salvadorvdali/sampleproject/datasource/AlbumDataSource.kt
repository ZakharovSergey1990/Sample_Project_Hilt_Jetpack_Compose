package ru.salvadorvdali.sampleproject.datasource

import ru.salvadorvdali.sampleproject.api.ApiService
import ru.salvadorvdali.sampleproject.database.MyRoomDatabase
import ru.salvadorvdali.sampleproject.models.Album
import javax.inject.Inject

interface AlbumDataSource {
    suspend fun getAlbums():List<Album>
    suspend fun getAlbum(albumId: String): Album
    suspend fun deleteAlbum(album: Album)
    suspend fun insertAlbums(albums: List<Album>)
    suspend fun deleteAll()
}

class AlbumDataSourceOnline @Inject constructor(
    var apiService: ApiService
): AlbumDataSource{
    override suspend fun getAlbums(): List<Album> {
      return apiService.getAlbums()
    }

    override suspend fun getAlbum(albumId: String): Album {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlbum(album: Album) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlbums(albums: List<Album>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

}

class AlbumDataSourceOffline @Inject constructor(
    var db: MyRoomDatabase
): AlbumDataSource{
    override suspend fun getAlbums(): List<Album> {
       return db.myDao().getAlbums()
    }

    override suspend fun getAlbum(albumId: String): Album {
       return db.myDao().getAlbum(albumId)
    }

    override suspend fun deleteAlbum(album: Album) {
        db.myDao().deleteAlbum(album = album)
    }

    override suspend fun insertAlbums(albums: List<Album>) {
        db.myDao().insertAlbums(albums)
    }

    override suspend fun deleteAll() {
        db.myDao().deleteAllAlbums()
    }

}