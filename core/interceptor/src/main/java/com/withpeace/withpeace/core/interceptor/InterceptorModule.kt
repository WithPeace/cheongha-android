package com.withpeace.withpeace.core.interceptor

import android.content.Context
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    ): Interceptor =
        AuthInterceptor(tokenPreferenceDataSource)


}