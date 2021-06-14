package ru.salvadorvdali.sampleproject.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.salvadorvdali.sampleproject.api.ApiService
import ru.salvadorvdali.sampleproject.database.MyRoomDatabase
import ru.salvadorvdali.sampleproject.datasource.*
import ru.salvadorvdali.sampleproject.repository.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OnLine

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OffLine


@Module
@InstallIn(SingletonComponent::class)
object myModule {

    @Singleton
    @Provides
    fun provideRetrofit(): ApiService {
        val okHttpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl("http://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient.build())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun getGson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context): MyRoomDatabase {
        return Room.databaseBuilder(
            context,
            MyRoomDatabase::class.java, "my-storage"
        ).fallbackToDestructiveMigration().build()
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class PrimaryModule {
    @Singleton
    @Binds
    abstract fun bindAlbumRepository(albumRepository: AlbumRepositoryImpl): AlbumRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindPhotoRepository(photoRepository: PhotoRepositoryImpl): PhotoRepository

    @Singleton
    @OnLine
    @Binds
    abstract fun bindAlbumDatasourceOnline(albumDataSourceOnline: AlbumDataSourceOnline): AlbumDataSource

    @Singleton
    @OffLine
    @Binds
    abstract fun bindAlbumDatasourceOffline(albumDataSourceOffline: AlbumDataSourceOffline): AlbumDataSource

    @Singleton
    @OnLine
    @Binds
    abstract fun bindPhotoDatasourceOnline(photoDataSourceOnline: PhotoDataSourceOnline): PhotoDataSource

    @Singleton
    @OffLine
    @Binds
    abstract fun bindPhotoDatasourceOffline(photoDataSourceOffline: PhotoDataSourceOffline): PhotoDataSource

    @Singleton
    @OnLine
    @Binds
    abstract fun bindUserDatasourceOnline(userDataSourceOnline: UserDataSourceOnline): UserDataSource

    @Singleton
    @OffLine
    @Binds
    abstract fun bindUserDatasourceOffline(userDataSourceOffline: UserDataSourceOffline): UserDataSource
}
