import com.app.profileeditor.uistate.ProfileUiModel
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.model.profile.ProfileChangingStatus

internal fun ProfileUiModel.toDomain(): ChangingProfileInfo {
    return ChangingProfileInfo(nickname, profileImage)
}

internal fun ChangingProfileInfo.toUiModel(baseProfile: ProfileUiModel): ProfileUiModel {
    return ProfileUiModel(
        nickname,
        profileImage,
        isChanged = when (getChangingStatus(baseProfile.toDomain())) {
            ProfileChangingStatus.Same -> false
            else -> true
        },
    )
}