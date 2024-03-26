package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(onError: (WithPeaceError) -> Unit): Flow<ProfileInfo>
    fun registerProfile(
        nickname: String, profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit>

    fun updateProfileImage(
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<String>

    fun updateNickname(
        nickname: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<String>

    fun updateProfile(
        nickname: String, profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<ProfileInfo>

    fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Boolean>
}
