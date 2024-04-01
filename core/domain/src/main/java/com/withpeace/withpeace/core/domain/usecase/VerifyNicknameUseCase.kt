package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VerifyNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        nickname: String,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Unit> {
        if (!Nickname.verifyFormat(nickname)) {
            return flow { onError(WithPeaceError.GeneralError(code = 1)) }
        }
        return userRepository.verifyNicknameDuplicated(Nickname(nickname), onError = onError)
    }
}
