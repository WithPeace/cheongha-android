package com.withpeace.withpeace.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.ui.PostTopicUiState.ECONOMY
import com.withpeace.withpeace.core.ui.PostTopicUiState.FREE
import com.withpeace.withpeace.core.ui.PostTopicUiState.HOBBY
import com.withpeace.withpeace.core.ui.PostTopicUiState.INFORMATION
import com.withpeace.withpeace.core.ui.PostTopicUiState.LIFE
import com.withpeace.withpeace.core.ui.PostTopicUiState.QUESTION

enum class PostTopicUiState(
    val topic: PostTopic,
    @StringRes val textResId: Int,
    @DrawableRes val iconResId: Int,
    val index: Int,
) {
    FREE(PostTopic.FREEDOM, R.string.free, R.drawable.ic_freedom, 0),
    INFORMATION(PostTopic.INFORMATION, R.string.information, R.drawable.ic_information, 1),
    QUESTION(PostTopic.QUESTION, R.string.question, R.drawable.ic_question, 2),
    LIFE(PostTopic.LIVING, R.string.life, R.drawable.ic_life, 3),
    HOBBY(PostTopic.HOBBY, R.string.hobby, R.drawable.ic_hobby, 4),
    ECONOMY(PostTopic.ECONOMY, R.string.economy, R.drawable.ic_economy, 5),
    ;

    companion object {
        fun create(topic: PostTopic) =
            PostTopicUiState.entries.find {
                it.topic == topic
            } ?: throw IllegalStateException("UI에 정의하지 않은 Topic이 있어요!")
    }
}

fun PostTopicUiState.toDomain(): PostTopic {
    return when (this) {
        FREE -> PostTopic.FREEDOM
        INFORMATION -> PostTopic.INFORMATION
        QUESTION -> PostTopic.QUESTION
        LIFE -> PostTopic.LIVING
        HOBBY -> PostTopic.HOBBY
        ECONOMY -> PostTopic.ECONOMY
    }
}
