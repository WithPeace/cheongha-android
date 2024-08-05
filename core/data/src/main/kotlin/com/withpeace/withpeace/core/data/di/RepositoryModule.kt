package com.withpeace.withpeace.core.data.di

import com.withpeace.withpeace.core.data.repository.DefaultAppUpdateRepository
import com.withpeace.withpeace.core.data.repository.DefaultImageRepository
import com.withpeace.withpeace.core.data.repository.DefaultPostRepository
import com.withpeace.withpeace.core.data.repository.DefaultTokenRepository
import com.withpeace.withpeace.core.data.repository.DefaultUserRepository
import com.withpeace.withpeace.core.data.repository.DefaultYouthPolicyRepository
import com.withpeace.withpeace.core.domain.repository.AppUpdateRepository
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import com.withpeace.withpeace.core.domain.repository.PostRepository
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    @ViewModelScoped
    fun bindsTokenRepository(defaultTokenRepository: DefaultTokenRepository): TokenRepository

    @Binds
    @ViewModelScoped
    fun bindsImageRepository(defaultImageRepository: DefaultImageRepository): ImageRepository

    @Binds
    @ViewModelScoped
    fun bindsPostRepository(defaultPostRepository: DefaultPostRepository): PostRepository

    @Binds
    @ViewModelScoped
    fun bindsUserRepository(
        defaultUserRepository: DefaultUserRepository,
    ): UserRepository

    @Binds
    @ViewModelScoped
    fun bindsYouthPolicyRepository(
        defaultYouthPolicyRepository: DefaultYouthPolicyRepository
    ): YouthPolicyRepository

    @Binds
    @ViewModelScoped
    fun bindsAppUpdateRepository(
        appUpdateRepository: DefaultAppUpdateRepository,
    ): AppUpdateRepository
}
