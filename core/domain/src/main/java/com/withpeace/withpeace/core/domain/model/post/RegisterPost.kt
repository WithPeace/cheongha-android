package com.withpeace.withpeace.core.domain.model.post

import com.withpeace.withpeace.core.domain.model.LimitedImages

data class RegisterPost(
    val title: String,
    val content: String,
    val topic: PostTopic?,
    val images: LimitedImages,
)
