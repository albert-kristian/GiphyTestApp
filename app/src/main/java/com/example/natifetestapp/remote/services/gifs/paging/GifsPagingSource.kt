package com.example.natifetestapp.remote.services.gifs.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.natifetestapp.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.remote.services.gifs.responses.GifResponse
import com.example.natifetestapp.remote.services.gifs.responses.GifsResponse

class GifsPagingSource(
    private val initialValues: GifsResponse,
    private val query: String,
    private val searchApi: SearchApi
) : PagingSource<Int, GifResponse>() {

    companion object {
        const val STEP = 25
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifResponse> {
        val currentOffset = params.key ?: 0
        if (currentOffset == 0) { // We should show initialValues if offset is zero
            return LoadResult.Page(
                data = initialValues,
                prevKey = null,
                nextKey = if (initialValues.isEmpty()) null else initialValues.count()
            )
        } else { // We make another request when user scrolls down
            try {
                val gifsResponse = searchApi.getGifs(q = query, limit = STEP, offset = currentOffset)
                gifsResponse.data?.let {
                    return LoadResult.Page(
                        data = gifsResponse.data,
                        prevKey = currentOffset - STEP,
                        nextKey = if (gifsResponse.data.isEmpty()) null else currentOffset + STEP
                    )
                }
                return LoadResult.Error(Exception(gifsResponse.meta.message))
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int,  GifResponse>): Int? {
        return state.anchorPosition
    }
}