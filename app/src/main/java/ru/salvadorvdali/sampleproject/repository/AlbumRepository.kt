package ru.salvadorvdali.sampleproject.repository

import android.util.Log
import ru.salvadorvdali.sampleproject.datasource.AlbumDataSource
import ru.salvadorvdali.sampleproject.datasource.AlbumDataSourceOffline
import ru.salvadorvdali.sampleproject.datasource.AlbumDataSourceOnline
import ru.salvadorvdali.sampleproject.di.OffLine
import ru.salvadorvdali.sampleproject.di.OnLine
import ru.salvadorvdali.sampleproject.models.Album
import ru.salvadorvdali.sampleproject.models.User
import javax.inject.Inject

interface AlbumRepository {
   suspend fun getAlbums(userId: String):List<Album>
   suspend fun getAlbum(albumId: String):Album
   suspend fun  deleteAlbum(album: Album)
   suspend fun updateAll(userId: String): List<Album>

}

class AlbumRepositoryImpl @Inject constructor(): AlbumRepository{

   @OffLine
   @Inject
   lateinit var albumDataSourceOffline: AlbumDataSource

   @OnLine
   @Inject
   lateinit var albumDataSourceOnline: AlbumDataSource

   override suspend fun getAlbums(userId: String): List<Album> {
      var albums = albumDataSourceOffline.getAlbums()
      if (albums.isNullOrEmpty()) {
         albums = albumDataSourceOnline.getAlbums()
         albumDataSourceOffline.insertAlbums(albums)
      }
      return albums.filter { it.userId.toString() == userId }
   }

   override suspend fun getAlbum(albumId: String): Album {
      return albumDataSourceOffline.getAlbum(albumId)
   }

   override suspend fun deleteAlbum(album: Album) {
      albumDataSourceOffline.deleteAlbum(album = album)
   }

   override suspend fun updateAll(userId: String): List<Album> {
      albumDataSourceOffline.deleteAll()
      return getAlbums(userId = userId)
   }
}

class AlbumRepositoryTest @Inject constructor(): AlbumRepository{

   val album1 = Album(userId=1, id=1, title="quidem molestiae enim")
   val album2 = Album(userId=1, id=2, title="sunt qui excepturi placeat culpa")
    val list = listOf(album1, album2)
   override suspend fun getAlbums(userId: String): List<Album> {
      return list
   }

   override suspend fun getAlbum(albumId: String): Album {
      return album1
   }

   override suspend fun deleteAlbum(album: Album) {
      Log.d("log", "delete Album")
   }

   override suspend fun updateAll(userId: String): List<Album> {
     return list
   }

}