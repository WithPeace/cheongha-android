package com.withpeace.withpeace.core.datastore.di

import com.withpeace.withpeace.core.datastore.dataStore.token.DefaultTokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.token.TokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.user.DefaultUserPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.user.UserPreferenceDataSource
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

    @Binds
    @Singleton
    fun bindsUserPreferenceDataSource(
        defaultUserPreferenceDataSource: DefaultUserPreferenceDataSource,
    ): UserPreferenceDataSource
}