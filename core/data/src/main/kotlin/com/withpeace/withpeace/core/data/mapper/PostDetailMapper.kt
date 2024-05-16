package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.Comment
import com.withpeace.withpeace.core.domain.model.post.CommentUser
import com.withpeace.withpeace.core.domain.model.post.PostContent
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTitle
import com.withpeace.withpeace.core.domain.model.post.PostUser
import com.withpeace.withpeace.core.network.di.response.post.CommentResponse
import com.withpeace.withpeace.core.network.di.response.post.PostDetailResponse

fun PostDetailResponse.toDomain() = PostDetail(
    user = PostUser(
        id = userId,
        name = nickname,
        profileImageUrl = profileImageUrl,
    ),
    id = postId,
    title = PostTitle(title),
    content = PostContent(content),
    postTopic = type.toDomain(),
    imageUrls = postImageUrls,
    createDate = Date(createDate.toLocalDateTime()),
    comments = comments.map { it.toDomain() },
)

fun CommentResponse.toDomain() = Comment(
    commentId = commentId,
    content = content,
    createDate = Date(createDate.toLocalDateTime()),
    commentUser = CommentUser(
        id = userId,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
    ),
)
