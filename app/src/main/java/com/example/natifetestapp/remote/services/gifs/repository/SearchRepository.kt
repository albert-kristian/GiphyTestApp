package com.example.natifetestapp.remote.services.gifs.repository

import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse

interface SearchRepository {

    suspend fun getGifs(
        query: String,
        limit: Int? = null,
        offset: Int = 0
    ): Result<GifsResponse>
}