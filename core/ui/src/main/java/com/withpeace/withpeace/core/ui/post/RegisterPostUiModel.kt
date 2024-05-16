package com.withpeace.withpeace.core.ui.post

import android.os.Parcelable
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterPostUiModel(
    val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val topic: PostTopicUiModel? = null,
    val imageUrls: List<String> = listOf(),
) : Parcelable


fun RegisterPostUiModel.toDomain() = RegisterPost(
    id = id,
    title = title,
    content = content,
    topic = topic?.toDomain(),
    images = LimitedImages(imageUrls),
)

fun RegisterPost.toUi() = RegisterPostUiModel(
    id = id,
    title = title,
    content = content,
    topic = topic?.toUi(),
    imageUrls = images.urls,
)
