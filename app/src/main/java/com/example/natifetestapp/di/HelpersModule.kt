package com.example.natifetestapp.di

import android.content.Context
import com.example.natifetestapp.utils.NetworkConnectionStatusHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelpersModule {

    @Provides
    @Singleton
    fun provideNetworkConnectionHelper(
        @ApplicationContext context: Context
    ): NetworkConnectionStatusHelper {
        return NetworkConnectionStatusHelper(
            context = context
        )
    }
}