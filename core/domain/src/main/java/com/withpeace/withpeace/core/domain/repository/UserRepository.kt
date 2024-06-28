package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.profile.ChangedProfile
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(onError: suspend (CheonghaError) -> Unit): Flow<ProfileInfo>

    fun updateProfileImage(
        profileImage: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile>

    fun updateNickname(
        nickname: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile>

    fun updateProfile(
        nickname: String, profileImage: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile>

    fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit>

    fun logout(onError: suspend (CheonghaError) -> Unit): Flow<Unit>

    suspend fun signUp(
        signUpInfo: SignUpInfo,
        onError: suspend (CheonghaError) -> Unit
    ): Flow<Unit>

    suspend fun getCurrentUserId(): Long
    fun withdraw(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit>
}
