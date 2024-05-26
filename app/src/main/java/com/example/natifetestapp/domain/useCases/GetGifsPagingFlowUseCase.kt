package com.example.natifetestapp.domain.useCases

import androidx.paging.PagingData
import com.example.natifetestapp.data.repository.SearchRepository
import com.example.natifetestapp.domain.models.GifDomain
import kotlinx.coroutines.flow.Flow

interface GetGifsPagingFlowUseCase {

    suspend fun execute(
        initialValues: List<GifDomain>,
        query: String
    ): Flow<PagingData<GifDomain>>
}

class GetGifsPagingFlowUseCaseImpl(
    private val repository: SearchRepository
): GetGifsPagingFlowUseCase {

    override suspend fun execute(
        initialValues: List<GifDomain>,
        query: String
    ): Flow<PagingData<GifDomain>> {
        return repository.getGifsPaginated(initialValues, query)
    }
}