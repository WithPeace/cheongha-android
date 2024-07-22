package com.withpeace.withpeace.feature.signup.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.signup.SignUpRoute
import com.withpeace.withpeace.feature.signup.SignUpViewModel

const val SIGN_UP_ROUTE = "signUpRoute"
private const val IMAGE_LIST_ARGUMENT = "image_list_argument"

fun NavController.navigateSignUp(navOptions: NavOptions? = null) {
    navigate(SIGN_UP_ROUTE, navOptions)
}

fun NavGraphBuilder.signUpNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onNavigateToGallery: () -> Unit,
    onSignUpSuccess: () -> Unit,
) {
    composable(
        route = SIGN_UP_ROUTE,
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
    ) { entry ->
        val selectedImageUri =
            entry.savedStateHandle.get<List<String>>(IMAGE_LIST_ARGUMENT) ?: emptyList()
        val viewModel: SignUpViewModel = hiltViewModel()
        if (selectedImageUri.isNotEmpty()) {
            viewModel.onImageChanged(imageUri = selectedImageUri.firstOrNull() ?: null)
        }
        SignUpRoute(
            onShowSnackBar = onShowSnackBar,
            onNavigateToGallery = onNavigateToGallery,
            viewModel = viewModel,
            onSignUpSuccess = onSignUpSuccess
        )
    }
}