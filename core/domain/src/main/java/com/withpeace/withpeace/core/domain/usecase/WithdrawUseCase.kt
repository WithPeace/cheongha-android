package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> =
        userRepository.withdraw(
            onError = onError,
        )
}