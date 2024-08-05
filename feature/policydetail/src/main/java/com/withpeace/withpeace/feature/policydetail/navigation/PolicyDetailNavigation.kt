package com.withpeace.withpeace.feature.policydetail.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.feature.policydetail.PolicyDetailRoute

const val POLICY_DETAIL_ROUTE = "policyDetailRoute"
const val POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT = "youthPolicy_argument"
const val POLICY_DETAIL_ROUTE_WITH_ARGUMENT =
    "$POLICY_DETAIL_ROUTE/{$POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT}"

fun NavController.navigateToPolicyDetail(
    navOptions: NavOptions? = null,
    policyId: String,
) {
    navigate("$POLICY_DETAIL_ROUTE/${policyId}", navOptions)
}
fun NavGraphBuilder.policyDetailNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onNavigationSnackbar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable(
        route = POLICY_DETAIL_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(POLICY_DETAIL_YOUTH_POLICY_ID_ARGUMENT) {
                type = NavType.StringType
            },
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

        PolicyDetailRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            onNavigationSnackbar = onNavigationSnackbar,
        )
    }
}