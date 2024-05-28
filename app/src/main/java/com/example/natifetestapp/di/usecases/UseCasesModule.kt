package com.example.natifetestapp.di.usecases

import com.example.natifetestapp.data.repository.GifsRepository
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCase
import com.example.natifetestapp.domain.useCases.GetGifsPagingFlowUseCaseImpl
import com.example.natifetestapp.domain.useCases.GetGifsUseCase
import com.example.natifetestapp.domain.useCases.GetGifsUseCaseImpl
import com.example.natifetestapp.domain.useCases.GetLoadedGifsUseCase
import com.example.natifetestapp.domain.useCases.GetLoadedGifsUseCaseImpl
import com.example.natifetestapp.domain.useCases.SetGifDeletedUseCase
import com.example.natifetestapp.domain.useCases.SetGifDeletedUseCaseImpl
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
        repository: GifsRepository
    ): GetGifsUseCase {
        return GetGifsUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetGifsPagingFlowUseCase(
        repository: GifsRepository
    ): GetGifsPagingFlowUseCase {
        return GetGifsPagingFlowUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetGifDeletedUseCase(
        repository: GifsRepository
    ): SetGifDeletedUseCase {
        return SetGifDeletedUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetLoadedGifsUseCase(
        repository: GifsRepository
    ): GetLoadedGifsUseCase {
        return GetLoadedGifsUseCaseImpl(repository)
    }
}