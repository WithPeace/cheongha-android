package com.withpeace.withpeace.feature.registerpost.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.registerpost.RegisterPostRoute

const val REGISTER_POST_ROUTE = "register_post_route"

fun NavController.navigateToRegisterPost(navOptions: NavOptions? = null) = navigate(
    REGISTER_POST_ROUTE, navOptions,
)

fun NavGraphBuilder.registerPostNavGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
    onClickCameraButton: (imageLimit: Int) -> Unit,
) {
    composable(route = REGISTER_POST_ROUTE) {
        RegisterPostRoute(
            onShowSnackBar = onShowSnackBar,
            onClickedBackButton = onClickBackButton,
            onCompleteRegisterPost = onCompleteRegisterPost,
            onClickCameraButton = onClickCameraButton
        )
    }
}
