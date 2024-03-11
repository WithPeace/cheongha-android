package com.withpeace.withpeace.feature.registerpost

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.withpeace.withpeace.core.domain.model.post.PostTopic

enum class PosterTopicUiState(
    val topic: PostTopic,
    @StringRes val textResId: Int,
    @DrawableRes val iconResId: Int,
) {
    FREE(PostTopic.FREEDOM, R.string.free, R.drawable.ic_cate_free),
    INFORMATION(PostTopic.INFORMATION, R.string.information, R.drawable.ic_cate_info),
    QUESTION(PostTopic.QUESTION, R.string.question, R.drawable.ic_cate_question),
    LIFE(PostTopic.LIVING, R.string.life, R.drawable.ic_cate_living),
    HOBBY(PostTopic.HOBBY, R.string.hobby, R.drawable.ic_cate_hobby),
    ECONOMY(PostTopic.ECONOMY, R.string.economy, R.drawable.ic_cate_eco);

    companion object {
        fun create(topic: PostTopic) = PosterTopicUiState.entries.find {
            it.topic == topic
        } ?: throw IllegalStateException("UI에 정의하지 않은 Topic이 있어요!")
    }
}
