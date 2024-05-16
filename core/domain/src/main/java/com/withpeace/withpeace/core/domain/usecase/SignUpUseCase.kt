package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        signUpInfo: SignUpInfo,
        onError: suspend (CheonghaError) -> Unit,
    ) = userRepository.signUp(
        signUpInfo,
        onError = onError,
    )
}
