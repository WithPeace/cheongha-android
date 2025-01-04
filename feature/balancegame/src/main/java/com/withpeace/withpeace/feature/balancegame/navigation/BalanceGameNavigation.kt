package com.withpeace.withpeace.feature.balancegame.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.balancegame.BalanceGameRoute

private const val BALANCE_GAME_ROUTE = "BALANCE_GAME_ROUTE"

fun NavController.navigateToBalanceGame(navOptions: NavOptions? = null) =
    navigate(BALANCE_GAME_ROUTE, navOptions)

fun NavGraphBuilder.balanceGameGraph(
    onShowSnackBar: (String) -> Unit,
    onAuthExpired: () -> Unit,
    onBackButtonClick: () -> Unit,
) {
    composable(
        route = BALANCE_GAME_ROUTE,
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
        BalanceGameRoute(
            onShowSnackBar = onShowSnackBar,
            onBackButtonClick = onBackButtonClick,
        )
    }
}
