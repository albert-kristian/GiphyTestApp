package com.example.natifetestapp.di.usecases

import com.example.natifetestapp.data.repository.SearchRepository
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCase
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCaseImpl
import com.example.natifetestapp.domain.useCases.GetGifsUseCase
import com.example.natifetestapp.domain.useCases.GetGifsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideGetGifsUseCase(
        repository: SearchRepository
    ): GetGifsUseCase {
        return GetGifsUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetGifsPagingFlowUseCase(
        repository: SearchRepository
    ): GetGifsPagingFlowUseCase {
        return GetGifsPagingFlowUseCaseImpl(repository)
    }
}