package com.withpeace.withpeace.core.ui.post

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel.ECONOMY
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel.FREEDOM
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel.HOBBY
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel.INFORMATION
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel.LIVING
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel.QUESTION
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PostTopicUiModel(
    @StringRes val textResId: Int,
    @DrawableRes val iconResId: Int,
    val index: Int,
) : Parcelable {
    FREEDOM(R.string.free, R.drawable.ic_freedom, 0),
    INFORMATION(R.string.information, R.drawable.ic_information, 1),
    QUESTION(R.string.question, R.drawable.ic_question, 2),
    LIVING(R.string.life, R.drawable.ic_life, 3),
    HOBBY(R.string.hobby, R.drawable.ic_hobby, 4),
    ECONOMY(R.string.economy, R.drawable.ic_economy, 5)
}

fun PostTopicUiModel.toDomain(): PostTopic {
    return when (this) {
        FREEDOM -> PostTopic.FREEDOM
        INFORMATION -> PostTopic.INFORMATION
        QUESTION -> PostTopic.QUESTION
        LIVING -> PostTopic.LIVING
        HOBBY -> PostTopic.HOBBY
        ECONOMY -> PostTopic.ECONOMY
    }
}

fun PostTopic.toUi(): PostTopicUiModel {
    return when (this) {
        PostTopic.FREEDOM -> FREEDOM
        PostTopic.INFORMATION -> INFORMATION
        PostTopic.QUESTION -> QUESTION
        PostTopic.LIVING -> LIVING
        PostTopic.HOBBY -> HOBBY
        PostTopic.ECONOMY -> ECONOMY
    }
}
