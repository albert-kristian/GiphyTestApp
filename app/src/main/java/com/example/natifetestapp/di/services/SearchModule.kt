package com.example.natifetestapp.di.services

import com.example.natifetestapp.data.local.daos.GifDao
import com.example.natifetestapp.data.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.data.repository.GifsRepositoryImpl
import com.example.natifetestapp.di.coroutines.IoDispatcher
import com.example.natifetestapp.utils.NetworkConnectionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        @IoDispatcher dispatcher: CoroutineDispatcher,
        connectionHelper: NetworkConnectionHelper,
        searchApi: SearchApi,
        gifDao: GifDao
    ): GifsRepository {
        return GifsRepositoryImpl(
            dispatcher = dispatcher,
            connectionHelper = connectionHelper,
            searchApi = searchApi,
            gifDao = gifDao
        )
    }
}