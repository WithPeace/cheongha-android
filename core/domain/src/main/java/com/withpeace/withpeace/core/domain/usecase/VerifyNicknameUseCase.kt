package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(
        nickname: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> {
        if (!Nickname.verifyFormat(nickname)) {
            return flow { onError(ClientError.NicknameError.FormatInvalid) }
        }
        return userRepository.verifyNicknameDuplicated(Nickname(nickname), onError = onError)
    }
}
