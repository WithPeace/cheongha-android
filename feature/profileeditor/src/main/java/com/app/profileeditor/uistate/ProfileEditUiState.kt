package com.app.profileeditor.uistate

// sealed interface ProfileEditUiState {
//     data class Editing(
//         val profileInfo: ProfileUiModel,
//     ) : ProfileEditUiState
//
//     data object NoChanges : ProfileEditUiState
// }

data class ProfileEditUiState(
    val currentProfileInfo: ProfileUiModel,
    val isChanged: Boolean,
)