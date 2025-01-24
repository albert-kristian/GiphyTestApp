package com.example.natifetestapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.natifetestapp.data.local.daos.GifDao
import com.example.natifetestapp.data.local.mapping.toDomain
import com.example.natifetestapp.data.local.mapping.toEntity
import com.example.natifetestapp.data.paging.GifsPagingSource
import com.example.natifetestapp.data.remote.mapping.toDomain
import com.example.natifetestapp.data.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.di.coroutines.IoDispatcher
import com.example.natifetestapp.domain.models.GifDomain
import com.example.natifetestapp.utils.NetworkConnectionStatusHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GifsRepositoryImpl(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val connectionHelper: NetworkConnectionStatusHelper,
    private val searchApi: SearchApi,
    private val gifDao: GifDao
): GifsRepository {

    private var loadedItems: Set<GifDomain> = mutableSetOf()

    override suspend fun getGifs(
        query: String,
        limit: Int,
        offset: Int
    ): List<GifDomain> {
        val result = if (connectionHelper.isOnline) {
            val response = searchApi.getGifs(
                q = query,
                limit = limit,
                offset = offset
            )
            response.data?.toDomain()?.let { gifDomains ->
                saveGifs(gifDomains)
                gifDomains
            } ?: throw Exception(response.meta.message)
        } else {
            val gifDomains = withContext(dispatcher) {
                gifDao.getGifs(
                    limit = limit,
                    offset = offset
                ).map { gifEntity ->
                    gifEntity.toDomain()
                }
            }
            gifDomains
        }
        return result.handleDeletedGifs().also {
            loadedItems += it
        }
    }

    override fun getGifsPaginated(
        initialValues: List<GifDomain>,
        query: String
    ): Flow<PagingData<GifDomain>> {
        loadedItems = initialValues.toSet()
        return Pager(
            config = PagingConfig(GifsRepository.LIMIT),
            pagingSourceFactory = {
                GifsPagingSource(
                    initialValues = initialValues,
                    query = query,
                    gifsRepository = this
                )
            }
        ).flow
    }

    override suspend fun setGifIsDeleted(id: String) {
        withContext(dispatcher) {
            val updatedGifEntity = gifDao.getGifById(id).copy(
                isDeleted = true
            )
            gifDao.insert(updatedGifEntity)
        }
    }

    override fun getAlreadyLoadedItems(): List<GifDomain> = loadedItems.toList()

    private suspend fun saveGifs(gifs: List<GifDomain>) {
        withContext(dispatcher) {
            gifs.forEach { gifDomain ->
                val existingEntity = gifDao.getOptionalGifById(gifDomain.id)
                gifDao.insert(
                    gifDomain.toEntity(
                        isDeleted = existingEntity?.isDeleted == true
                    )
                )
            }
        }
    }

    private suspend fun List<GifDomain>.handleDeletedGifs(): List<GifDomain> {
        return map { gifDomain ->
            withContext(dispatcher) {
                val existingEntity = gifDao.getOptionalGifById(gifDomain.id)
                gifDomain.copy(shouldBeShown = existingEntity?.isDeleted == false)
            }
        }
    }
}