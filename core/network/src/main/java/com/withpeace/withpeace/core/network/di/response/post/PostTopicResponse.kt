package com.withpeace.withpeace.core.network.di.response.post;

import kotlinx.serialization.SerialName

enum class PostTopicResponse {
    @SerialName("FREEDOM")
    FREEDOM,

    @SerialName("INFORMATION")
    INFORMATION,

    @SerialName("QUESTION")
    QUESTION,

    @SerialName("LIVING")
    LIVING,

    @SerialName("HOBBY")
    HOBBY,

    @SerialName("ECONOMY")
    ECONOMY;
}
