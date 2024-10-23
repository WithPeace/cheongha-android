package com.withpeace.withpeace.feature.policyfilter.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.policyfilter.PolicyFilterRoute

const val POLICY_FILTER_ROUTE = "policy_filter_route"

fun NavController.navigateToPrivacyPolicy(navOptions: NavOptions? = null) =
    navigate(POLICY_FILTER_ROUTE, navOptions)

fun NavGraphBuilder.privacyPolicyGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable(
        route = POLICY_FILTER_ROUTE,
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
        PolicyFilterRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}