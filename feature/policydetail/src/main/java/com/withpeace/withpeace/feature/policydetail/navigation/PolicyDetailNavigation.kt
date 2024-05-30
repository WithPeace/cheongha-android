package com.withpeace.withpeace.feature.policydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.feature.policydetail.PolicyDetailRoute

const val POLICY_DETAIL_ROUTE = "policyDetailRoute"

fun NavController.navigateToPolicyDetail(navOptions: NavOptions? = null) {
    navigate(POLICY_DETAIL_ROUTE, navOptions)
}
fun NavGraphBuilder.policyDetailNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable(route = POLICY_DETAIL_ROUTE,
    ) {
        PolicyDetailRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}