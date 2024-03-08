package com.withpeace.withpeace.feature.registerpost.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.registerpost.RegisterPostRoute

const val RegisterPostRoute = "register_post_route"

fun NavController.navigateToRegisterPost(navOptions: NavOptions? = null) = navigate(
    RegisterPostRoute, navOptions,
)

fun NavGraphBuilder.registerPostNavGraph(
    onShowSnackBar: (String) -> Unit,
) {
    composable(route = RegisterPostRoute) {
        RegisterPostRoute(
            onShowSnackBar = onShowSnackBar,
        )
    }
}
