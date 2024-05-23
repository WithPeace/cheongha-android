package com.withpeace.withpeace.core.analytics

data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList(),
) {
    class Types {
        companion object {
            const val SCREEN_VIEW = "screen_view"
            const val BUTTON_CLICK = "button_click"
            const val TOPIC_CLICK = "topic_click"
            const val SIGN_UP = "sign_up"
            const val LOGIN = "login"

        }
    }
    data class Param(val key: String, val value: String)
    class ParamKeys {
        companion object {
            const val SCREEN_NAME = "screen_name"
            const val BUTTON_ID = "button_id"
            const val POST_ID = "post_id"
            const val POST_TOPIC = "post_topic"
            const val POLICY_ID = "policy_id"
            const val POLICY_TITLE = "policy_title"
        }
    }
}