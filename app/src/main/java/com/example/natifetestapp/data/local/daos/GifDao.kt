package com.example.natifetestapp.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.natifetestapp.data.local.entities.GifEntity

@Dao
interface GifDao {

    @Query("SELECT * FROM gifentity ORDER BY addedTime ASC LIMIT :limit OFFSET :offset")
    fun getGifs(limit: Int, offset: Int): List<GifEntity>

    @Query("SELECT * FROM gifentity WHERE id == :id")
    fun getGifById(id: String): GifEntity

    @Query("SELECT * FROM gifentity WHERE id == :id")
    fun getOptionalGifById(id: String): GifEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gif: GifEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gifs: List<GifEntity>)
}