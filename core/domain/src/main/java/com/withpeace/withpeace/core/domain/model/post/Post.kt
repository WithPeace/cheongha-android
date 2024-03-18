package com.withpeace.withpeace.core.domain.model.post

import java.time.LocalDate

data class Post(
    val postId: Long,
    val title: String,
    val content: String,
    val postTopic: PostTopic,
    val createDate: LocalDate,
    val postImageUrl: String,
)
