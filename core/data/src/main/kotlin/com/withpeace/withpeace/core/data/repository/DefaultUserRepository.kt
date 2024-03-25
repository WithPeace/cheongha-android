package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.service.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val userService: UserService,
) : UserRepository {
    override fun getProfile(
        onError: (WithPeaceError) -> Unit,
    ): Flow<ProfileInfo> = flow {
        userService.getProfile().suspendMapSuccess {
            emit(this.data.toDomain())
        }.suspendOnError {
            if (statusCode.code == 401) onError(WithPeaceError.UnAuthorized())
            else onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun registerProfile(
        nickname: String,
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateProfileImage(
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun updateNickname(nickname: String, onError: (WithPeaceError) -> Unit): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun updateProfile(
        nickname: String,
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<ProfileInfo> {
        TODO("Not yet implemented")
    }

    override fun isNicknameDuplicated(
        nickname: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}