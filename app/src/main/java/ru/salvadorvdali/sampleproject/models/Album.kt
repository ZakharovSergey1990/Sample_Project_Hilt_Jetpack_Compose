package ru.salvadorvdali.sampleproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "albums")
data class Album (

    @ColumnInfo(name = "userId")
    val userId: Int,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String
)