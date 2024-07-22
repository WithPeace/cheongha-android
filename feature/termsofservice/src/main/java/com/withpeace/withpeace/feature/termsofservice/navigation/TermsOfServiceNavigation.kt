package com.withpeace.withpeace.feature.termsofservice.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.termsofservice.TermsOfServiceRoute

const val TERMS_OF_SERVICE_ROUTE = "terms_of_service_route"

fun NavController.navigateToTermsOfService(navOptions: NavOptions? = null) =
    navigate(TERMS_OF_SERVICE_ROUTE, navOptions)

fun NavGraphBuilder.termsOfServiceGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable(
        route = TERMS_OF_SERVICE_ROUTE,
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

        TermsOfServiceRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}