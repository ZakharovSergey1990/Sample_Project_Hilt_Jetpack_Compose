package ru.salvadorvdali.sampleproject

import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.salvadorvdali.sampleproject.api.ApiService
import ru.salvadorvdali.sampleproject.database.MyRoomDatabase
import ru.salvadorvdali.sampleproject.datasource.*
import ru.salvadorvdali.sampleproject.di.OffLine
import ru.salvadorvdali.sampleproject.di.OnLine
import ru.salvadorvdali.sampleproject.di.PrimaryModule
import ru.salvadorvdali.sampleproject.repository.*
import javax.inject.Qualifier
import javax.inject.Singleton



@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PrimaryModule::class]
)
@Module
abstract class PrimaryModuleTest {
    @Singleton
    @Binds
    abstract fun bindAlbumRepository(albumRepository: AlbumRepositoryTest): AlbumRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: UserRepositoryTest): UserRepository

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
