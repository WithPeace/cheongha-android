package com.withpeace.withpeace.core.imagestorage

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageDataSourceModule {
    @Provides
    @Singleton
    fun provideImageDataSource(
        @ApplicationContext context: Context,
    ): ImageDataSource = DefaultImageDataSource(context)
}
