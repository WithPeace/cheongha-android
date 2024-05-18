package com.withpeace.withpeace

import android.app.Application
import com.withpeace.withpeace.core.analytics.FirebaseInitializer
import com.withpeace.withpeace.core.analytics.FirebaseInstance
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WithPeaceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseInitializer.initialize(this)
    }
}