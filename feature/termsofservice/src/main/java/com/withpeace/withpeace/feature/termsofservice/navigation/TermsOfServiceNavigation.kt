package com.withpeace.withpeace.feature.termsofservice.navigation

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
    composable(TERMS_OF_SERVICE_ROUTE) {

        TermsOfServiceRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}