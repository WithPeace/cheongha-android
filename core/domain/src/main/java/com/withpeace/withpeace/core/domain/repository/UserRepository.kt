package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ChangedProfile
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
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ChangedProfile>

    fun updateNickname(
        nickname: String,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ChangedProfile>

    fun updateProfile(
        nickname: String, profileImage: String,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ChangedProfile>

    fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Unit>

    fun logout(onError: suspend (WithPeaceError) -> Unit): Flow<Unit>
}
