package com.withpeace.withpeace.core.domain.model

data class LimitedImages(
    val urls: List<String>,
    val maxCount: Int = DEFAULT_MAX_COUNT,
) {
    init {
        check(urls.size <= maxCount) { "이미지의 최대 개수는 ${maxCount}장입니다" }
    }

    val additionalCount: Int
        get() = maxCount - urls.size

    fun contains(url: String)=urls.contains(url)

    fun canAddImage() = urls.size < maxCount

    fun addImage(url: String) = copy(urls = urls + listOf(url))

    fun addImages(inputUrls: List<String>) = copy(urls = urls + inputUrls)

    fun deleteImage(url: String) = copy(urls = urls.filter { it != url })

    companion object {
        const val DEFAULT_MAX_COUNT = 5
    }
}
