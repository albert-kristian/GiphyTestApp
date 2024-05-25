package com.example.natifetestapp.remote.services.gifs.repository

import com.example.natifetestapp.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse

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
}