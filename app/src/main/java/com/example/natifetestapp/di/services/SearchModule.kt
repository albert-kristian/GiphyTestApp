package com.example.natifetestapp.di.services

import com.example.natifetestapp.remote.services.gifs.api.SearchApi
import com.example.natifetestapp.remote.services.gifs.repository.SearchRepository
import com.example.natifetestapp.remote.services.gifs.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // TODO: should be viewModel scoped
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(searchApi: SearchApi): SearchRepository {
        return SearchRepositoryImpl(searchApi)
    }
}