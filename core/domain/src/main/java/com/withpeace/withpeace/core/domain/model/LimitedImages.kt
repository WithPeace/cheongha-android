package com.withpeace.withpeace.core.domain.model

data class LimitedImages(
    val urls: List<String>,
    val maxCount: Int,
) {
    init {
        check(urls.size <= maxCount) { "이미지의 최대 개수는 ${maxCount}장입니다" }
    }

    fun contains(url: String)=urls.contains(url)

    fun canAddImage() = urls.size < maxCount

    fun addImage(url: String) = copy(urls = urls + listOf(url))

    fun deleteImage(url: String) = copy(urls = urls.filter { it != url })
}
