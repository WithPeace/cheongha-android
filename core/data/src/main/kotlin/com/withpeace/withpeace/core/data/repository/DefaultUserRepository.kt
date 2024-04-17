package com.withpeace.withpeace.core.data.repository

import android.content.Context
import android.net.Uri
import com.skydoves.sandwich.suspendMapSuccess
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.util.convertToFile
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.datastore.dataStore.token.TokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.user.UserPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.profile.ChangedProfile
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.request.NicknameRequest
import com.withpeace.withpeace.core.network.di.service.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userService: UserService,
    private val tokenPreferenceDataSource: TokenPreferenceDataSource,
    private val userPreferenceDataSource: UserPreferenceDataSource,
) : UserRepository {
    override fun getProfile(
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ProfileInfo> = flow {
        userService.getProfile().suspendMapSuccess {
            emit(this.data.toDomain())
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override suspend fun signUp(
        signUpInfo: SignUpInfo,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> = flow {
        val nicknameRequestBody =
            signUpInfo.nickname.toRequestBody("text/plain".toMediaTypeOrNull())
        val request =
            if (signUpInfo.profileImage.isNullOrEmpty()) {
                userService.signUp(
                    nicknameRequestBody,
                )
            } else {
                val profileImagePart = getImagePart(signUpInfo.profileImage!!)
                userService.signUp(nicknameRequestBody, profileImagePart)
            }

        request.suspendMapSuccess {
            val data = this.data
            userPreferenceDataSource.updateUserRole(Role.USER.name)
            tokenPreferenceDataSource.updateAccessToken(data.accessToken)
            tokenPreferenceDataSource.updateRefreshToken(data.refreshToken)
            emit(Unit)
        }.handleApiFailure(onError)
    }

    override suspend fun getCurrentUserId(): Long = withContext(Dispatchers.IO) {
        userPreferenceDataSource.userId.firstOrNull() ?: throw IllegalStateException("로그인 되있지 않아요")
    }

    override fun updateProfile(
        nickname: String,
        profileImage: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile> = flow {
        val imagePart = getImagePart(profileImage)
        userService.updateProfile(
            nickname.toRequestBody("text/plain".toMediaTypeOrNull()), imagePart,
        ).suspendMapSuccess {
            emit(this.data.toDomain())
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun updateNickname(
        nickname: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile> =
        flow {
            userService.updateNickname(NicknameRequest(nickname)).suspendMapSuccess {
                emit(ChangedProfile(nickname = Nickname(this.data)))
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
        }

    override fun updateProfileImage(
        profileImage: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<ChangedProfile> = flow {
        val imagePart = getImagePart(profileImage)
        userService.updateImage(imagePart).suspendMapSuccess {
            emit(ChangedProfile(profileImageUrl = this.data))
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> = flow {
        userService.isNicknameDuplicate(nickname.value).suspendMapSuccess {
            if (this.data) {
                onError(ClientError.NicknameError.Duplicated)
            } else {
                emit(Unit)
            }
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun logout(onError: suspend (CheonghaError) -> Unit): Flow<Unit> = flow {
        userService.logout().suspendMapSuccess {
            tokenPreferenceDataSource.removeAll()
            userPreferenceDataSource.removeAll()
            emit(Unit)
        }.handleApiFailure(onError)
    }

    private fun getImagePart(profileImage: String): MultipartBody.Part {
        val requestFile: File = Uri.parse(profileImage).convertToFile(context)
        val imageRequestBody = requestFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "imageFile",
            requestFile.name,
            imageRequestBody,
        )
    }

    private suspend fun onErrorWithAuthExpired(
        it: ResponseError,
        onError: suspend (CheonghaError) -> Unit,
    ) {
        if (it == ResponseError.INVALID_TOKEN_ERROR) {
            logout(onError).collect {
                onError(ClientError.AuthExpired)
            }
        } else {
            onError(it)
        }
    }
}
