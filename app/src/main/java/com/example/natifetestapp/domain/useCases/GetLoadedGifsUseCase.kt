package com.example.natifetestapp.domain.useCases

import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.domain.models.GifDomain

interface GetLoadedGifsUseCase {

    fun execute(): List<GifDomain>
}

class GetLoadedGifsUseCaseImpl(
    private val repository: GifsRepository
): GetLoadedGifsUseCase {

    override fun execute(): List<GifDomain> {
        return repository.getAlreadyLoadedItems()
    }
}