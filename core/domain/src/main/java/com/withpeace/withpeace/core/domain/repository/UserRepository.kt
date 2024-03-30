package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(onError: suspend (WithPeaceError) -> Unit): Flow<ProfileInfo>
    fun registerProfile(
        nickname: String, profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit>

    fun updateProfileImage(
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit>

    fun updateNickname(
        nickname: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit>

    fun updateProfile(
        nickname: String, profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit>

    fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit>
}
