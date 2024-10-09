package com.withpeace.withpeace.feature.home.uistate

import com.withpeace.withpeace.core.domain.model.post.RecentPost
import com.withpeace.withpeace.core.ui.post.toUi

fun RecentPost.toUiModel(): RecentPostUiModel {
    return RecentPostUiModel(
        id = id, title = title, type = type.toUi(),
    )
}