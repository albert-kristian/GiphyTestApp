package com.example.natifetestapp.data.repository

import androidx.paging.PagingData
import com.example.natifetestapp.domain.models.GifDomain
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    companion object {
        const val LIMIT = 25
    }

    suspend fun getGifs(
        query: String,
        limit: Int = LIMIT,
        offset: Int = 0
    ): List<GifDomain>

    fun getGifsPaginated(
        initialValues: List<GifDomain>,
        query: String,
    ): Flow<PagingData<GifDomain>>

    suspend fun setGifIsCached(id: String)
}