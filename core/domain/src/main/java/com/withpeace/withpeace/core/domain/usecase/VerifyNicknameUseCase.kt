package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(nickname: String, onError: (WithPeaceError) -> Unit): Flow<Unit> {
        if (!Nickname.verifyNickname(nickname)) {
            onError(WithPeaceError.GeneralError(code = 1))
            return flow {  }
        }
        return userRepository.verifyNicknameDuplicated(Nickname.create(nickname), onError = onError)
    }
}
