package com.example.natifetestapp.remote.services.gifs.api

import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse
import com.example.natifetestapp.remote.responses.GiphyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("gifs/search")
    suspend fun getGifs(
        @Query("q") q: String,
        @Query("limit") limit: Int = 5,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
    ): GiphyResponse<GifsResponse>
}