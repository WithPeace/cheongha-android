package com.withpeace.withpeace.core.data.di

import com.withpeace.withpeace.core.data.repository.DefaultImageRepository
import com.withpeace.withpeace.core.data.repository.DefaultPostRepository
import com.withpeace.withpeace.core.data.repository.DefaultTokenRepository
import com.withpeace.withpeace.core.data.repository.DefaultUserRepository
import com.withpeace.withpeace.core.data.repository.DefaultYouthPolicyRepository
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import com.withpeace.withpeace.core.domain.repository.PostRepository
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
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

    @Binds
    @Singleton
    fun bindsPostRepository(defaultPostRepository: DefaultPostRepository): PostRepository

    @Binds
    @Singleton
    fun bindsUserRepository(
        defaultUserRepository: DefaultUserRepository,
    ): UserRepository

    @Binds
    @Singleton
    fun bindsYouthPolicyRepository(
        defaultYouthPolicyRepository: DefaultYouthPolicyRepository
    ): YouthPolicyRepository
}
