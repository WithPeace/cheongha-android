package com.withpeace.withpeace.core.network.di.di

import com.withpeace.withpeace.core.network.di.service.AppUpdateService
import com.withpeace.withpeace.core.network.di.service.AuthService
import com.withpeace.withpeace.core.network.di.service.PostService
import com.withpeace.withpeace.core.network.di.service.UserService
import com.withpeace.withpeace.core.network.di.service.YouthPolicyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesLoginService(@Named("auth") retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesPostService(@Named("general") retrofit: Retrofit): PostService =
        retrofit.create(PostService::class.java)

    @Provides
    @Singleton
    fun providesUserService(@Named("general") retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providesAppUpdateService(@Named("general") retrofit: Retrofit): AppUpdateService =
        retrofit.create(AppUpdateService::class.java)

    @Provides
    @Singleton
    fun providesYouthPolicyService(@Named("youth_policy") retrofit: Retrofit): YouthPolicyService =
        retrofit.create(YouthPolicyService::class.java)
}
