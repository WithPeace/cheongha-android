package com.withpeace.withpeace.core.domain.model.post

import com.withpeace.withpeace.core.domain.model.date.Date

data class Comment(
    val commentId: Long,
    val content: String,
    val createDate: Date,
    val commentUser: CommentUser,
)

data class CommentUser(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
)
