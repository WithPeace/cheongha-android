package com.app.profileeditor.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.profileeditor.ProfileEditorRoute
import com.app.profileeditor.ProfileEditorViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val PROFILE_EDITOR_ROUTE = "profileEditorRoute"
const val IMAGE_LIST_ARGUMENT = "image_list_argument"
const val PROFILE_NICKNAME_ARGUMENT = "nickname_argument"
const val PROFILE_IMAGE_URL_ARGUMENT = "profile_image_url_argument"
const val PROFILE_EDITOR_ROUTE_WITH_ARGUMENT =
    "$PROFILE_EDITOR_ROUTE/{$PROFILE_NICKNAME_ARGUMENT}/{$PROFILE_IMAGE_URL_ARGUMENT}"

fun NavController.navigateProfileEditor(
    navOptions: NavOptions? = null,
    nickname: String?,
    profileImageUrl: String?,
) {
    val encodedUrl = URLEncoder.encode(profileImageUrl, StandardCharsets.UTF_8.toString())
    navigate("$PROFILE_EDITOR_ROUTE/${nickname ?: ""}/${encodedUrl ?: ""}", navOptions)
}

fun NavGraphBuilder.profileEditorNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onUpdateSuccess: (nickname: String, imageUrl: String) -> Unit,
    onAuthExpired: () -> Unit,
) {
    composable(
        route = PROFILE_EDITOR_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(PROFILE_NICKNAME_ARGUMENT) { type = NavType.StringType },
            navArgument(PROFILE_IMAGE_URL_ARGUMENT) { type = NavType.StringType },
        ),
    ) { entry ->
        val selectedImageUri =
            entry.savedStateHandle.get<List<String>>(IMAGE_LIST_ARGUMENT) ?: emptyList()
        val viewModel: ProfileEditorViewModel = hiltViewModel()
        if (selectedImageUri.isNotEmpty()) {
            viewModel.onImageChanged(imageUri = selectedImageUri.firstOrNull() ?: "default.png")
        }
        ProfileEditorRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            onNavigateToGallery = onNavigateToGallery,
            viewModel = viewModel,
            onUpdateSuccess = onUpdateSuccess,
            onAuthExpired = onAuthExpired
        )
    }
}