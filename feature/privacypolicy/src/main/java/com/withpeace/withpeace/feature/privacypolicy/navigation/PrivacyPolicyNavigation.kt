package com.withpeace.withpeace.feature.privacypolicy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.privacypolicy.PrivacyPolicyRoute

const val PRIVACY_POLICY_ROUTE = "privacy_policy_route"

fun NavController.navigateToPrivacyPolicy(navOptions: NavOptions? = null) =
    navigate(PRIVACY_POLICY_ROUTE, navOptions)

fun NavGraphBuilder.privacyPolicyGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable(
        route = PRIVACY_POLICY_ROUTE,
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
        PrivacyPolicyRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}
