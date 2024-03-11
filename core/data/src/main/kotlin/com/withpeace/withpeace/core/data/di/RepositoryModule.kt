package com.withpeace.withpeace.core.data.di

import com.withpeace.withpeace.core.data.repository.DefaultImageRepository
import com.withpeace.withpeace.core.data.repository.DefaultTokenRepository
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsTokenRepository(defaultTokenRepository: DefaultTokenRepository): TokenRepository

    @Binds
    @Singleton
    fun bindsImageRepository(defaultImageRepository: DefaultImageRepository): ImageRepository
}
