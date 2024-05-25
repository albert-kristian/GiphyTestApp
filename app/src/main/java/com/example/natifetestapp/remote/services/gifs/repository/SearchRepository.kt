package com.example.natifetestapp.remote.services.gifs.repository

import androidx.paging.PagingData
import com.example.natifetestapp.remote.services.gifs.responses.GifResponse
import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getGifs(
        query: String,
        limit: Int? = null,
        offset: Int = 0
    ): Result<GifsResponse>

    fun getGifsPaginated(
        initialValues: GifsResponse,
        query: String,
    ): Flow<PagingData<GifResponse>>
}