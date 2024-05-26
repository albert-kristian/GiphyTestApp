package com.example.natifetestapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class GifEntity(
    @PrimaryKey val id: String,
    val addedTime: Long = Date().time,
    val isCached: Boolean = false,
    val title: String,
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "thumbnail_height") val thumbnailHeight: Int,
    @ColumnInfo(name = "thumbnail_width") val thumbnailWidth: Int,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    @ColumnInfo(name = "original_url") val originalUrl: String
)