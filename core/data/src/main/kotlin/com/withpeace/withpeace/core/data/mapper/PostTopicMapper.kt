package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse.ECONOMY
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse.FREEDOM
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse.HOBBY
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse.INFORMATION
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse.LIVING
import com.withpeace.withpeace.core.network.di.response.post.PostTopicResponse.QUESTION

fun PostTopicResponse.toDomain(): PostTopic {
    return when (this) {
        FREEDOM -> PostTopic.FREEDOM
        INFORMATION -> PostTopic.INFORMATION
        QUESTION -> PostTopic.QUESTION
        LIVING -> PostTopic.LIVING
        HOBBY -> PostTopic.HOBBY
        ECONOMY -> PostTopic.ECONOMY
    }
}
