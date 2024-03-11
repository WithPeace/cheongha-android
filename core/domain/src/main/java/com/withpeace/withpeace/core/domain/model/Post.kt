package com.withpeace.withpeace.core.domain.model

data class Post(
    val title: String,
    val content: String,
    val topic: PostTopic?,
    val images: LimitedImages,
)

enum class PostTopic {
    FREE, INFORMATION, QUESTION, LIFE, HOBBY, ECONOMY
}
