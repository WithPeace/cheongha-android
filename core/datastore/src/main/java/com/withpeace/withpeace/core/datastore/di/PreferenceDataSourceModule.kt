package com.withpeace.withpeace.core.datastore.di

import com.withpeace.withpeace.core.datastore.dataStore.DefaultTokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceDataSourceModule {

    @Binds
    @Singleton
    fun bindsTokenPreferenceDataSource(
        defaultTokenPreferenceDataSource: DefaultTokenPreferenceDataSource,
    ): TokenPreferenceDataSource
}