package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.post.RecentPost
import com.withpeace.withpeace.core.network.di.response.post.RecentPostResponse

fun RecentPostResponse.toDomain(): RecentPost {
    return RecentPost(
        id = postId, title = title, type = type.toDomain(),
    )
}