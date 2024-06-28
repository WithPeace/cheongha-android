package com.withpeace.withpeace.core.data.analytics

import com.withpeace.withpeace.core.analytics.AnalyticsEvent
import com.withpeace.withpeace.core.analytics.AnalyticsHelper

fun AnalyticsHelper.event(type: AnalyticsEvent.Type) {
    logEvent(
        AnalyticsEvent(
            type = type.eventTitle,
        ),
    )
}