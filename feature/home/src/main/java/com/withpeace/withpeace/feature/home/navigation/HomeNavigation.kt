package com.withpeace.withpeace.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.home.HomeRoute

const val HOME_ROUTE = "homeRoute"

fun NavController.navigateHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowSnackBar: (message: String) -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onShowSnackBar = onShowSnackBar)
    }
}