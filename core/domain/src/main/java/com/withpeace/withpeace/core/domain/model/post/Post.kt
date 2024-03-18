package com.withpeace.withpeace.core.domain.model.post

import java.time.LocalDateTime

data class Post(
    val postId: Long,
    val title: String,
    val content: String,
    val postTopic: PostTopic,
    val createDate: LocalDateTime,
    val postImageUrl: String,
)
