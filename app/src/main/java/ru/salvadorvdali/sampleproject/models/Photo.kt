package ru.salvadorvdali.sampleproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photos")
data class Photo (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "databaseId")
    val databaseId: Long,

    @ColumnInfo(name = "albumID")
    val albumId: Long,
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "thumbnailURL")
    val thumbnailURL: String?
)