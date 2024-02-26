package com.withpeace.withpeace.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
            isLenient = true
        }

        val jsonMediaType = "application/json".toMediaType()
        return json.asConverterFactory(jsonMediaType)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

//    @Provides
//    @Singleton
//    fun provideHeaderInterceptor(chain: Interceptor.Chain) {
//        val requestBuilder = chain.request().newBuilder()
//        var apiKey = BuildConfig.X_RIOT_TOKEN
//        requestBuilder.addHeader("X-Riot-Token", apiKey)
//        chain.proceed(requestBuilder.build())
//    }

    @Singleton
    @Provides
    fun provideOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            //                addInterceptor(AccessTokenInterceptor) TODO("토큰 인터셉터 할당")
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }


    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://asia.api.riotgames.com/") // TODO("BaseUrl 수정")
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }
}