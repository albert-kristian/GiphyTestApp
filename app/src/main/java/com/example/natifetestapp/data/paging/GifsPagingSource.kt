package com.example.natifetestapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.natifetestapp.data.repository.SearchRepository
import com.example.natifetestapp.domain.models.GifDomain

class GifsPagingSource(
    private val initialValues: List<GifDomain>,
    private val query: String,
    private val searchRepository: SearchRepository
): PagingSource<Int, GifDomain>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifDomain> {
        val currentOffset = params.key ?: 0
        if (currentOffset == 0) { // We should show initialValues if offset is zero
            return LoadResult.Page(
                data = initialValues,
                prevKey = null,
                nextKey = if (initialValues.isEmpty()) null else initialValues.count()
            )
        } else { // We make another request when user scrolls down
            try {
                val gifs = searchRepository.getGifs(
                    query = query,
                    limit = SearchRepository.LIMIT,
                    offset = currentOffset
                )
                return LoadResult.Page(
                    data = gifs,
                    prevKey = currentOffset - SearchRepository.LIMIT,
                    nextKey = if (gifs.isEmpty()) null else currentOffset + SearchRepository.LIMIT
                )
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifDomain>): Int? {
        return state.anchorPosition
    }
}