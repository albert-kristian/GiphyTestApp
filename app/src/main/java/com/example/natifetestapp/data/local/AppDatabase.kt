package com.example.natifetestapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.natifetestapp.data.local.daos.GifDao
import com.example.natifetestapp.data.local.entities.GifEntity

@Database(entities = [GifEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun gifDao(): GifDao
}