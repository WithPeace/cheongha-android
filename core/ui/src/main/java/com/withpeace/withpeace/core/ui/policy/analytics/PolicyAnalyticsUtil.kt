package com.withpeace.withpeace.core.ui.policy.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.withpeace.withpeace.core.analytics.AnalyticsEvent
import com.withpeace.withpeace.core.analytics.AnalyticsHelper
import com.withpeace.withpeace.core.analytics.LocalAnalyticsHelper

fun AnalyticsHelper.logPolicyDetailScreenView(
    screenName: String,
    policyId: String,
    policyTitle: String,
) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Type.SCREEN_VIEW.eventTitle,
            extras = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.POLICY_TITLE, policyTitle),
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.POLICY_ID, policyId),
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

@Composable
fun TrackPolicyDetailScreenViewEvent(
    screenName: String,
    policyId: String,
    policyTitle: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logPolicyDetailScreenView(
        screenName,
        policyId = policyId,
        policyTitle = policyTitle,
    )
    onDispose {}
}