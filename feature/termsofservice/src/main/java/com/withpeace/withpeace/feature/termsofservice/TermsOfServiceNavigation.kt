package com.withpeace.withpeace.feature.termsofservice

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val TERMS_OF_SERVICE_ROUTE = "terms_of_service_route"

fun NavController.navigateToTermsOfService(navOptions: NavOptions? = null) =
    navigate(TERMS_OF_SERVICE_ROUTE, navOptions)

fun NavGraphBuilder.policyConsentGraph(
    onShowSnackBar: (String) -> Unit,
) {
    composable(TERMS_OF_SERVICE_ROUTE) {

        TermsOfServiceRoute(
            onShowSnackBar = onShowSnackBar,
        )
    }
}