package com.example.natifetestapp.di.database

import android.content.Context
import androidx.room.Room
import com.example.natifetestapp.data.local.AppDatabase
import com.example.natifetestapp.data.local.daos.GifDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "gifs-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGifDao(database: AppDatabase): GifDao {
        return database.gifDao()
    }
}