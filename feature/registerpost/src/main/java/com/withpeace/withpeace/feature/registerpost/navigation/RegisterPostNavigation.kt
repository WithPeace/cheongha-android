package com.withpeace.withpeace.feature.registerpost.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.registerpost.RegisterPostRoute

const val REGISTER_POST_ROUTE = "register_post_route"
const val IMAGE_LIST_ARGUMENT = "image_list_argument"

fun NavController.navigateToRegisterPost(navOptions: NavOptions? = null) = navigate(
    REGISTER_POST_ROUTE, navOptions,
)

fun NavGraphBuilder.registerPostNavGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
    onNavigateToGallery: (imageLimit: Int, imageCount: Int) -> Unit,
) {
    composable(route = REGISTER_POST_ROUTE) { entry ->
        val selectedImageUrls =
            entry.savedStateHandle.get<List<String>>(IMAGE_LIST_ARGUMENT)?: emptyList()
        RegisterPostRoute(
            onShowSnackBar = onShowSnackBar,
            onClickedBackButton = onClickBackButton,
            onCompleteRegisterPost = onCompleteRegisterPost,
            onNavigateToGallery = onNavigateToGallery,
            selectedImageUrls = selectedImageUrls,
        )
    }
}
