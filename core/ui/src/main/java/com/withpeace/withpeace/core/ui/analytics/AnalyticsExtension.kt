package com.withpeace.withpeace.core.ui.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.withpeace.withpeace.core.analytics.AnalyticsEvent
import com.withpeace.withpeace.core.analytics.AnalyticsHelper
import com.withpeace.withpeace.core.analytics.LocalAnalyticsHelper

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Type.SCREEN_VIEW.eventTitle,
            extras = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}



fun AnalyticsHelper.buttonClick(screenName: String, buttonId: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Type.BUTTON_CLICK.eventTitle,
            extras = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screenName),
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.BUTTON_ID, buttonId),
            ),
        ),
    )
}

@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}

