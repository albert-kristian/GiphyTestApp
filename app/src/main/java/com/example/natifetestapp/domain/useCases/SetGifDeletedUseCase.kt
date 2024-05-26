package com.example.natifetestapp.domain.useCases

import com.example.natifetestapp.data.repository.GifsRepository

interface SetGifDeletedUseCase {

    suspend fun execute(id: String)
}

class SetGifDeletedUseCaseImpl(
    private val repository: GifsRepository
): SetGifDeletedUseCase {

    override suspend fun execute(id: String) {
        repository.setGifIsDeleted(id)
    }
}