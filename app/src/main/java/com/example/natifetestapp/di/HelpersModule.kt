package com.example.natifetestapp.di

import android.content.Context
import com.example.natifetestapp.di.coroutines.MainDispatcher
import com.example.natifetestapp.utils.NetworkConnectionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelpersModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionHelper(
        @ApplicationContext context: Context,
        @MainDispatcher dispatcher: CoroutineDispatcher
    ): NetworkConnectionHelper {
        return NetworkConnectionHelper(
            context = context,
            dispatcher = dispatcher
        )
    }
}