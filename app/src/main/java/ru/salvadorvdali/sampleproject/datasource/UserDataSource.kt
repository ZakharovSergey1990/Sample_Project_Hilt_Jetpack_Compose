package ru.salvadorvdali.sampleproject.datasource

import android.util.Log
import ru.salvadorvdali.sampleproject.api.ApiService
import ru.salvadorvdali.sampleproject.database.MyRoomDatabase
import ru.salvadorvdali.sampleproject.models.User
import javax.inject.Inject


interface UserDataSource {
    suspend fun getUsers():List<User>
    suspend fun  deleteUser(user: User)
    suspend fun  deleteAll()
    suspend fun insertUsers(users: List<User>)
}

class UserDataSourceOnline @Inject constructor(
var apiService: ApiService
): UserDataSource{
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }

    override suspend fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }

    override suspend fun insertUsers(users: List<User>) {
        TODO("Not yet implemented")
    }

}

class UserDataSourceOffline @Inject constructor(
    var db: MyRoomDatabase
): UserDataSource{
    override suspend fun getUsers(): List<User> {
        return db.myDao().getUsers()
    }

    override suspend fun deleteUser(user: User) {
        Log.d("log", "deleteUser")
        db.myDao().deleteUser(user)
    }

    override suspend fun deleteAll() {
      db.myDao().deleteAllUsers()
        Log.d("log", "users.size = ${db.myDao().getUsers().size}")

    }

    override suspend fun insertUsers(users: List<User>) {
        db.myDao().insertUsers(users)
    }
}