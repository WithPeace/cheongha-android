package com.withpeace.withpeace.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    companion object {
        @Provides
        @Singleton
        fun bindsAnalyticsHelper(
            prod: FirebaseAnalyticsHelper,
            debug: StubAnalyticsHelper,
        ): AnalyticsHelper {
            // return if (BuildConfig.DEBUG) debug
            // else
            //TODO("수정")
                return prod
        }

        @Provides
        @Singleton
        fun provideFirebaseAnalytics(): FirebaseAnalytics {
            return Firebase.analytics
        }
    }
}