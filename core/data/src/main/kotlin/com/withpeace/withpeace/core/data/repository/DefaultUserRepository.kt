package com.withpeace.withpeace.core.data.repository

import android.content.Context
import android.net.Uri
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.util.convertToFile
import com.withpeace.withpeace.core.datastore.dataStore.token.TokenPreferenceDataSource
import com.withpeace.withpeace.core.datastore.dataStore.user.UserPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ChangedProfile
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.common.getErrorBody
import com.withpeace.withpeace.core.network.di.request.NicknameRequest
import com.withpeace.withpeace.core.network.di.service.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ProfileInfo> = flow {
        userService.getProfile().suspendMapSuccess {
            emit(this.data.toDomain())
        }.suspendOnError {
            if (statusCode.code == 401) {
                onError(WithPeaceError.UnAuthorized())
            } else {
                val errorBody = errorBody?.getErrorBody()
                onError(WithPeaceError.GeneralError(errorBody?.code, errorBody?.message))
            }
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override suspend fun signUp(
        signUpInfo: SignUpInfo,
        onError: suspend (WithPeaceError) -> Unit,
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
        }.suspendOnError {
            if (statusCode.code == 401) {
                onError(WithPeaceError.UnAuthorized())
            } else {
                val errorBody = errorBody?.getErrorBody()
                onError(WithPeaceError.GeneralError(errorBody?.code, errorBody?.message))
            }
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun updateProfile(
        nickname: String,
        profileImage: String,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ChangedProfile> = flow {
        val imagePart = getImagePart(profileImage)
        userService.updateProfile(
            nickname.toRequestBody("text/plain".toMediaTypeOrNull()), imagePart,
        ).suspendMapSuccess {
            emit(this.data.toDomain())
        }.suspendOnError {
            if (statusCode.code == 401) {
                onError(WithPeaceError.UnAuthorized())
            } else {
                val errorBody = errorBody?.getErrorBody()
                onError(WithPeaceError.GeneralError(errorBody?.code, errorBody?.message))
            }
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun updateNickname(
        nickname: String,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ChangedProfile> =
        flow {
            userService.updateNickname(NicknameRequest(nickname)).suspendMapSuccess {
                emit(ChangedProfile(nickname = Nickname(this.data)))
            }.suspendOnError {
                if (statusCode.code == 401) {
                    onError(WithPeaceError.UnAuthorized())
                } else {
                    val errorBody = errorBody?.getErrorBody()
                    onError(WithPeaceError.GeneralError(errorBody?.code, errorBody?.message))
                }
            }.suspendOnException {
                onError(WithPeaceError.GeneralError(message = messageOrNull))
            }
        }

    override fun updateProfileImage(
        profileImage: String,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<ChangedProfile> = flow {
        val imagePart = getImagePart(profileImage)
        userService.updateImage(imagePart).suspendMapSuccess {
            emit(ChangedProfile(profileImageUrl = this.data))
        }.suspendOnError {
            if (statusCode.code == 401) {
                onError(WithPeaceError.UnAuthorized())
            } else {
                val errorBody = errorBody?.getErrorBody()
                onError(WithPeaceError.GeneralError(errorBody?.code, errorBody?.message))
            }
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Unit> = flow {
        userService.isNicknameDuplicate(nickname.value).suspendMapSuccess {
            if (this.data) {
                onError(WithPeaceError.GeneralError(code = 2))
            } else {
                emit(Unit)
            }
        }.suspendOnError {
            onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }



    override fun logout(onError: suspend (WithPeaceError) -> Unit): Flow<Unit> = flow {
        userService.logout().suspendMapSuccess {
            tokenPreferenceDataSource.removeAll()
            userPreferenceDataSource.removeAll()
            emit(Unit)
        }.suspendOnError {
            if (statusCode.code == 401) {
                onError(WithPeaceError.UnAuthorized())
            } else {
                val errorBody = errorBody?.getErrorBody()
                onError(WithPeaceError.GeneralError(errorBody?.code, errorBody?.message))
            }
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
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
}