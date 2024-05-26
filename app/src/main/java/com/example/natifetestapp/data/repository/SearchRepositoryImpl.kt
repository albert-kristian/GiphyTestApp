package com.example.natifetestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.natifetestapp.data.local.daos.GifDao
import com.example.natifetestapp.data.local.mapping.toDomain
import com.example.natifetestapp.data.local.mapping.toEntity
import com.example.natifetestapp.data.remote.mapping.toDomain
import com.example.natifetestapp.data.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.data.paging.GifsPagingSource
import com.example.natifetestapp.di.IoDispatcher
import com.example.natifetestapp.domain.models.GifDomain
import com.example.natifetestapp.utils.NetworkConnectionHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val connectionHelper: NetworkConnectionHelper,
    private val searchApi: SearchApi,
    private val gifDao: GifDao
): SearchRepository {

    override suspend fun getGifs(
        query: String,
        limit: Int,
        offset: Int
    ): List<GifDomain> {
        if (connectionHelper.isOnline) {
            val response = searchApi.getGifs(
                q = query,
                limit = limit,
                offset = offset
            )
            response.data?.toDomain()?.let { gifDomains ->
                saveGifs(gifDomains)
                return gifDomains
            } ?: throw Exception(response.meta.message)
        } else {
            val result = withContext(dispatcher) {
                gifDao.getGifs(
                    limit = limit,
                    offset = offset
                ).filter { gifEntity ->
                    gifEntity.isCached
                }.map { gifEntity ->
                    gifEntity.toDomain()
                }
            }
            return result
        }
    }

    override fun getGifsPaginated(
        initialValues: List<GifDomain>,
        query: String
    ): Flow<PagingData<GifDomain>> {
        return Pager(
            config = PagingConfig(SearchRepository.LIMIT),
            pagingSourceFactory = {
                GifsPagingSource(
                    initialValues = initialValues,
                    query = query,
                    searchRepository = this
                )
            }
        ).flow
    }

    private suspend fun saveGifs(gifs: List<GifDomain>) {
        withContext(dispatcher) {
            gifDao.insertAll(gifs = gifs.map { it.toEntity() })
        }
    }

    override suspend fun setGifIsCached(id: String) {
        withContext(dispatcher) {
            val updatedGifEntity = gifDao.getGifById(id).copy(
                isCached = true
            )
            gifDao.insert(updatedGifEntity)
        }
    }
}