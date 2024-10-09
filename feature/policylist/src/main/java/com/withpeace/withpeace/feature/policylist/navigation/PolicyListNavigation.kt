package com.withpeace.withpeace.feature.policylist.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.policylist.PolicyListRoute

const val POLICY_LIST_ROUTE = "policy_list_route"

fun NavController.navigateToPolicyList(navOptions: NavOptions? = null) =
    navigate(POLICY_LIST_ROUTE, navOptions)

fun NavGraphBuilder.policyListGraph(
    onShowSnackBar: (message: String) -> Unit,
    onNavigationSnackBar: (message: String) -> Unit = {},
    onPolicyClick: (String) -> Unit,
) {
    composable(
        route = POLICY_LIST_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        PolicyListRoute(
            onNavigationSnackBar = onNavigationSnackBar,
            onShowSnackBar = onShowSnackBar,
            onPolicyClick = onPolicyClick,
        )
    }
}
