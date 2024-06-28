package com.withpeace.withpeace.feature.privacypolicy.navigation

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
    composable(PRIVACY_POLICY_ROUTE) {
        PrivacyPolicyRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}
