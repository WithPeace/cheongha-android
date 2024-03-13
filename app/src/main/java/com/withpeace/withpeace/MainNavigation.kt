package com.withpeace.withpeace

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val MAIN_ROUTE = "mainRoute"

fun NavController.navigateMain(navOptions: NavOptions? = null) {
    navigate(MAIN_ROUTE, navOptions)
}

fun NavGraphBuilder.mainNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    navController: NavHostController,
) {
    composable(route = MAIN_ROUTE) {
        MainRoute(onShowSnackBar = onShowSnackBar, navController = navController)
    }
}