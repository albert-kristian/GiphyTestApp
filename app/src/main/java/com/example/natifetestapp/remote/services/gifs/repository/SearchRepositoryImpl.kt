package com.example.natifetestapp.remote.services.gifs.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.natifetestapp.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.remote.services.gifs.paging.GifsPagingSource
import com.example.natifetestapp.remote.services.gifs.responses.GifResponse
import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchApi: SearchApi
): SearchRepository {

    override suspend fun getGifs(
        query: String,
        limit: Int?,
        offset: Int
    ): Result<GifsResponse> {
        return try {
            val response = searchApi.getGifs(
                q = query,
                limit = limit ?: 50,
                offset = offset
            )
            response.data?.let {
                return Result.success(response.data)
            }
            Result.failure(Exception(response.meta.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getGifsPaginated(
        initialValues: GifsResponse,
        query: String
    ): Flow<PagingData<GifResponse>> {
        return Pager(
            config = PagingConfig(GifsPagingSource.STEP),
            pagingSourceFactory = {
                GifsPagingSource(initialValues = initialValues, query = query, searchApi = searchApi)
            }
        ).flow
    }
}