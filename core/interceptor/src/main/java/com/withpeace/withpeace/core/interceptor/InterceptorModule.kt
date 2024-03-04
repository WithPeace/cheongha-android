package com.withpeace.withpeace.core.interceptor

import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import com.withpeace.withpeace.core.network.di.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    @Singleton
    fun provideHeaderInterceptor(
        tokenPreferenceDataSource: TokenPreferenceDataSource,
        authService: AuthService,
    ): Interceptor =
        AuthInterceptor(tokenPreferenceDataSource, authService)
}
