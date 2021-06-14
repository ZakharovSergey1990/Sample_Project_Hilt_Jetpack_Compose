package ru.salvadorvdali.sampleproject.repository

import android.util.Log
import ru.salvadorvdali.sampleproject.datasource.UserDataSource
import ru.salvadorvdali.sampleproject.datasource.UserDataSourceOffline
import ru.salvadorvdali.sampleproject.datasource.UserDataSourceOnline
import ru.salvadorvdali.sampleproject.di.OffLine
import ru.salvadorvdali.sampleproject.di.OnLine
import ru.salvadorvdali.sampleproject.models.*
import javax.inject.Inject

interface UserRepository {
    suspend fun getUsers():List<User>
    suspend fun  deleteUser(user: User)
    suspend fun updateAll():List<User>
}

class UserRepositoryImpl @Inject constructor(): UserRepository{

    @Inject
    @OffLine
    lateinit var userDataSourceOffline: UserDataSource

    @Inject
    @OnLine
    lateinit var userDataSourceOnline: UserDataSource


    override suspend fun getUsers(): List<User> {
      var users =  userDataSourceOffline.getUsers()
        if(users.isNullOrEmpty())
        {
            users = userDataSourceOnline.getUsers()
            userDataSourceOffline.insertUsers(users)
        }
        return users
    }

    override suspend fun deleteUser(user: User) {
            userDataSourceOffline.deleteUser(user)
    }

    override suspend fun updateAll(): List<User> {
        userDataSourceOffline.deleteAll()
        return getUsers()
    }

}

class UserRepositoryTest @Inject constructor(): UserRepository{
    val user1 = User(id=1, name="Leanne Graham", username="Bret", email="Sincere@april.biz",
        address= Address(street="Kulas Light", suite="Apt. 556", city="Gwenborough",
            zipcode="92998-3874", geo= Geo(lat="-37.3159", lng="81.1496")
        ), phone="1-770-736-8031 x56442",
        website="hildegard.org", company= Company(name="Romaguera-Crona", catchPhrase="Multi-layered client-server neural-net",
            bs="harness real-time e-markets")
    )
    val user2 = User(id=2, name="Ervin Howell", username="Antonette", email="Shanna@melissa.tv",
        address=Address(street="Victor Plains", suite="Suite 879", city="Wisokyburgh",
            zipcode="90566-7771", geo=Geo(lat="-43.9509", lng="-34.4618")),
        phone="010-692-6593 x09125", website="anastasia.net",
        company=Company(name="Deckow-Crist", catchPhrase="Proactive didactic contingency",
            bs="synergize scalable supply-chains"))

    override suspend fun getUsers(): List<User> {
        return listOf(user1, user2)
    }

    override suspend fun deleteUser(user: User) {
        Log.d("log", "delete User")
    }

    override suspend fun updateAll(): List<User> {
        return listOf(user1, user2)
    }
}