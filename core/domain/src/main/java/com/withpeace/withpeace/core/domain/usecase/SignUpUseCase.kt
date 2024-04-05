package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        signUpInfo: SignUpInfo,
        onError: suspend (WithPeaceError) -> Unit,
    ) = userRepository.signUp(
        signUpInfo,
        onError = onError,
    )
}
