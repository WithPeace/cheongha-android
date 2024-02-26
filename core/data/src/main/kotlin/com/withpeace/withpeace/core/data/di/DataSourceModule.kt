package com.withpeace.withpeace.core.data.di

import com.withpeace.withpeace.core.datastore.dataStore.DefaultTokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsTokenDataSource(tokenPreferenceDataSource: DefaultTokenPreferenceDataSource): TokenPreferenceDataSource
}