package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.PostRepository
import javax.inject.Inject

class GetRecentPostUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    operator fun invoke(onError: suspend (CheonghaError) -> Unit) =
        postRepository.getRecentPost(onError)
}