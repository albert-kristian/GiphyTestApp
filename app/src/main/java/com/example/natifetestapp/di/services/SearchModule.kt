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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object SearchModule {

    @Provides
    @ViewModelScoped
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @ViewModelScoped
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