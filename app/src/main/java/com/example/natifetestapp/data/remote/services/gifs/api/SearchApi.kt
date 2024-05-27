package com.example.natifetestapp.data.remote.services.gifs.api

import com.example.natifetestapp.data.remote.services.gifs.responses.GifsResponse
import com.example.natifetestapp.data.remote.responses.GiphyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("gifs/search")
    suspend fun getGifs(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String = "g",
        @Query("lang") lang: String = "en",
    ): GiphyResponse<GifsResponse>
}