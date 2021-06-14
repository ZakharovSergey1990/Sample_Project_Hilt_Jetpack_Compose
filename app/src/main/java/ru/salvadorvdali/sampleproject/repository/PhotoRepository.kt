package ru.salvadorvdali.sampleproject.repository

import android.util.Log
import ru.salvadorvdali.sampleproject.datasource.*
import ru.salvadorvdali.sampleproject.di.OffLine
import ru.salvadorvdali.sampleproject.di.OnLine
import ru.salvadorvdali.sampleproject.models.Photo
import ru.salvadorvdali.sampleproject.models.User
import javax.inject.Inject

interface PhotoRepository {
        suspend fun getPhoto(albumId: String):List<Photo>
        suspend fun  deletePhoto(photo: Photo)
        suspend fun updateAll(albumId: String):List<Photo>
}

class PhotoRepositoryImpl @Inject constructor(): PhotoRepository{
        @OffLine
        @Inject
        lateinit var photoDataSourceOffline: PhotoDataSource

        @OnLine
        @Inject
        lateinit var photoDataSourceOnline: PhotoDataSource
        override suspend fun getPhoto(albumId: String): List<Photo> {
                var photos = photoDataSourceOffline.getPhoto()
                if (photos.isNullOrEmpty()) {
                        photos = photoDataSourceOnline.getPhoto()
                        photoDataSourceOffline.insertPhotos(photos)
                }
                Log.d("log", "photos = $photos")
                Log.d("log", "photos filter = ${photos.filter { it.albumId.toString() == albumId  }}")
                return photos.filter { it.albumId.toString() == albumId  }
        }

        override suspend fun deletePhoto(photo: Photo) {
               photoDataSourceOffline.deletePhoto(photo)
        }

        override suspend fun updateAll(albumId: String): List<Photo> {
                photoDataSourceOffline.deleteAll()
                return getPhoto(albumId)
        }


}