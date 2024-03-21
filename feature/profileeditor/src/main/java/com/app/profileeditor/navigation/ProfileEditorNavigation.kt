package com.app.profileeditor.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.app.profileeditor.ProfileEditorRoute
import com.app.profileeditor.ProfileEditorViewModel

const val PROFILE_EDITOR_ROUTE = "profileEditorRoute"
const val IMAGE_LIST_ARGUMENT = "image_list_argument"

fun NavController.navigateProfileEditor(navOptions: NavOptions? = null) {
    navigate(PROFILE_EDITOR_ROUTE, navOptions)
}

fun NavGraphBuilder.profileEditorNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
    onNavigateToGallery: () -> Unit,
) {
    composable(route = PROFILE_EDITOR_ROUTE) { entry ->
        val selectedImageUri =
            entry.savedStateHandle.get<List<String>>(IMAGE_LIST_ARGUMENT) ?: emptyList()
        val viewModel: ProfileEditorViewModel = hiltViewModel()
        viewModel.onImageUriAdded(imageUri = selectedImageUri.firstOrNull() ?: "")
        ProfileEditorRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            onNavigateToGallery = onNavigateToGallery,
            viewModel = viewModel,
        )
    }
}