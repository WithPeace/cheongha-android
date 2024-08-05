package com.withpeace.withpeace.feature.disablepolicy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.feature.disablepolicy.DisabledPolicyRoute

const val DISABLE_POLICY_ROUTE = "DISABLE_POLICY_ROUTE"
const val DISABLE_POLICY_ID_ARGUMENT = "DISABLE_POLICY_ID_ARGUMENT"
const val DISABLE_POLICY_ROUTE_WITH_ARGUMENT = "$DISABLE_POLICY_ROUTE/{$DISABLE_POLICY_ID_ARGUMENT}"

fun NavController.navigateDisabledPolicy(
    navOptions: NavOptions? = null, policyId: String,
) {
    navigate("$DISABLE_POLICY_ROUTE/$policyId", navOptions)
}

fun NavGraphBuilder.disabledPolicyNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onAuthExpired: () -> Unit,
    onBookmarkDeleteSuccess: (policyId: String) -> Unit = {},
    onClickBackButton: () -> Unit = {},
) {

    composable(
        route = DISABLE_POLICY_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(DISABLE_POLICY_ID_ARGUMENT) { type = NavType.StringType },
        ),
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
        DisabledPolicyRoute(
            onShowSnackBar = onShowSnackBar,
            onBookmarkDeleteSuccess = onBookmarkDeleteSuccess,
            onClickBackButton = onClickBackButton,
        )
    }
}