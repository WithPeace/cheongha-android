package com.withpeace.withpeace.core.domain.model.post

import com.withpeace.withpeace.core.domain.model.date.Date
import java.time.Duration
import java.time.LocalDateTime

data class PostDetail(
    val user: PostUser,
    val id: Long,
    val title: PostTitle,
    val content: PostContent,
    val postTopic: PostTopic,
    val imageUrls: List<String>,
    val createDate: Date,
    val nowDate: LocalDateTime,
)

data class PostUser(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
)
