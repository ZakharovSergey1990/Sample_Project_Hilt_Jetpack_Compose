package ru.salvadorvdali.sampleproject.datasource

import ru.salvadorvdali.sampleproject.api.ApiService
import ru.salvadorvdali.sampleproject.database.MyRoomDatabase
import ru.salvadorvdali.sampleproject.models.Photo
import javax.inject.Inject

interface PhotoDataSource {
    suspend fun getPhoto():List<Photo>
    suspend fun  deletePhoto(photo: Photo)
    suspend fun insertPhotos(photos: List<Photo>)
    suspend fun  deleteAll()
}

class PhotoDataSourceOnline @Inject constructor(
    var apiService: ApiService
) : PhotoDataSource{
    override suspend fun getPhoto(): List<Photo> {
       return apiService.getPhotos()
    }

    override suspend fun deletePhoto(photo: Photo) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPhotos(photos: List<Photo>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

}



class PhotoDataSourceOffline @Inject constructor(
    var db: MyRoomDatabase
): PhotoDataSource{
    override suspend fun getPhoto(): List<Photo> {
        return db.myDao().getPhotos()
    }

    override suspend fun deletePhoto(photo: Photo) {
        db.myDao().deletePhoto(photo = photo)
    }

    override suspend fun insertPhotos(photos: List<Photo>) {
        db.myDao().insertPhotos(photos = photos)
    }

    override suspend fun deleteAll() {
      db.myDao().deleteAllPhotos()
    }

}