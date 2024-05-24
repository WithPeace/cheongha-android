package com.withpeace.withpeace.core.analytics

data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList(),
) {
    enum class Type(val eventTitle: String) {
        SCREEN_VIEW("screen_view"),
        BUTTON_CLICK("button_click"),
        TOPIC_CLICK("topic_click"),
        SIGN_UP("sign_up"),
        LOGIN("login"),
        LOGOUT("logout"),
        WITHDRAW("withdraw"),
        REGISTER_POST("register_post");
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