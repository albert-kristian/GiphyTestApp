package com.example.natifetestapp.domain.useCases

import androidx.paging.PagingData
import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.domain.models.GifDomain
import kotlinx.coroutines.flow.Flow

interface GetGifsPagingFlowUseCase {

    suspend fun execute(
        initialValues: List<GifDomain>,
        query: String,
        refreshPager: Boolean = false
    ): Flow<PagingData<GifDomain>>
}

class GetGifsPagingFlowUseCaseImpl(
    private val repository: GifsRepository
): GetGifsPagingFlowUseCase {

    override suspend fun execute(
        initialValues: List<GifDomain>,
        query: String,
        refreshPager: Boolean
    ): Flow<PagingData<GifDomain>> {
        return repository.getGifsPaginated(initialValues, query)
    }
}