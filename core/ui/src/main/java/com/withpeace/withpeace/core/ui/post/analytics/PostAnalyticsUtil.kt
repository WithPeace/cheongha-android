package com.withpeace.withpeace.core.ui.post.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.withpeace.withpeace.core.analytics.AnalyticsEvent
import com.withpeace.withpeace.core.analytics.AnalyticsHelper
import com.withpeace.withpeace.core.analytics.LocalAnalyticsHelper
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel

fun AnalyticsHelper.topicClick(topic: PostTopicUiModel) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Type.TOPIC_CLICK.eventTitle,
            extras = listOf(
                AnalyticsEvent.Param(
                    AnalyticsEvent.ParamKeys.POST_TOPIC,
                    topic.convertToAnalyticsTopic(),
                ),
            ),
        ),
    )
}

private fun AnalyticsHelper.logPostDetailScreenView(
    screenName: String,
    postId: String,
    postTopic: String,
) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Type.SCREEN_VIEW.eventTitle,
            extras = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.POST_TOPIC, postTopic),
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.POST_ID, postId),
                AnalyticsEvent.Param(AnalyticsEvent.ParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

private fun PostTopicUiModel.convertToAnalyticsTopic() = when (this) {
    PostTopicUiModel.FREEDOM -> "free"
    PostTopicUiModel.INFORMATION -> "info"
    PostTopicUiModel.QUESTION -> "question"
    PostTopicUiModel.LIVING -> "lifestyle"
    PostTopicUiModel.HOBBY -> "hobby"
    PostTopicUiModel.ECONOMY -> "economy"
}

@Composable
fun TrackPostDetailScreenViewEvent(
    screenName: String,
    postId: Long,
    postTopicUiModel: PostTopicUiModel,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logPostDetailScreenView(
        screenName,
        postId.toString(),
        postTopic = postTopicUiModel.convertToAnalyticsTopic(),
    )
    onDispose {}
}