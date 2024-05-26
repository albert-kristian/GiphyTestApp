package com.example.natifetestapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.domain.models.GifDomain

class GifsPagingSource(
    private val initialValues: List<GifDomain>,
    private val query: String,
    private val gifsRepository: GifsRepository
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
                val gifs = gifsRepository.getGifs(
                    query = query,
                    limit = GifsRepository.LIMIT,
                    offset = currentOffset
                )
                return LoadResult.Page(
                    data = gifs,
                    prevKey = currentOffset - GifsRepository.LIMIT,
                    nextKey = if (gifs.isEmpty()) null else currentOffset + GifsRepository.LIMIT
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