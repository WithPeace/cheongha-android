package com.withpeace.withpeace.core.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

object FirebaseInitializer {

    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return
        FirebaseAnalytics.getInstance(context)
        FirebaseCrashlytics.getInstance()
        isInitialized = true
        FirebaseInstance.init(context)
    }
}