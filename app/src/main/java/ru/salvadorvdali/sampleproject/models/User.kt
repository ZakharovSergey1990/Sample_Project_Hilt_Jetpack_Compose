package ru.salvadorvdali.sampleproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.salvadorvdali.sampleproject.database.AddressConverter
import ru.salvadorvdali.sampleproject.database.CompanyConverter

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "email")
    val email: String,
    @TypeConverters(AddressConverter::class)
    @ColumnInfo(name = "address")
    val address: Address,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "website")
    val website: String,
    @TypeConverters(CompanyConverter::class)
    @ColumnInfo(name = "company")
    val company: Company
)

@TypeConverters(AddressConverter::class)
data class Address (
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
)


data class Geo (
    val lat: String,
    val lng: String
)
@TypeConverters(CompanyConverter::class)
data class Company (
    val name: String,
    val catchPhrase: String,
    val bs: String
)

