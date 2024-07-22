package com.withpeace.withpeace.feature.policyconsent.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.policyconsent.PolicyConsentRoute

const val POLICY_CONSENT_ROUTE = "policy_consent_route"

fun NavController.navigateToPolicyConsent(navOptions: NavOptions? = null) =
    navigate(POLICY_CONSENT_ROUTE, navOptions)

fun NavGraphBuilder.policyConsentGraph(
    onShowSnackBar: (String) -> Unit,
    onShowPrivacyPolicyClick: () -> Unit,
    onShowTermsOfServiceClick: () -> Unit,
    onSuccessToNext: () -> Unit,
) {
    composable(
        POLICY_CONSENT_ROUTE,
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
        PolicyConsentRoute(
            onShowSnackBar = onShowSnackBar,
            onShowPrivacyPolicyClick = onShowPrivacyPolicyClick,
            onShowTermsOfServiceClick = onShowTermsOfServiceClick,
            onSuccessToNext = onSuccessToNext,
        )
    }
}
