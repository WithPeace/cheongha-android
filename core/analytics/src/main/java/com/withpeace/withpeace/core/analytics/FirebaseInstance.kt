package com.withpeace.withpeace.core.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

object FirebaseInstance {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }

    fun screenEvent(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }
}