package com.withpeace.withpeace.feature.search.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.search.SearchRoute

private const val SEARCH_ROUTE = "SEARCH_ROUTE"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) =
    navigate(SEARCH_ROUTE, navOptions)

fun NavGraphBuilder.searchGraph(
    onShowSnackBar: (String) -> Unit,
    onAuthExpired: () -> Unit,
) {
    composable(
        route = SEARCH_ROUTE,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500),
            )
        },
    ) {
        SearchRoute()
    }
}
