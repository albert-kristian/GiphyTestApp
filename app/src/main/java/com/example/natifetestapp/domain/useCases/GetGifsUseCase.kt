package com.example.natifetestapp.domain.useCases

import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.domain.models.GifDomain

interface GetGifsUseCase {

    suspend fun execute(query: String): Result<List<GifDomain>>
}

class GetGifsUseCaseImpl(private val repository: GifsRepository): GetGifsUseCase {

    override suspend fun execute(query: String): Result<List<GifDomain>> {
        return try {
            val gifs = repository.getGifs(query = query)
            Result.success(gifs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}