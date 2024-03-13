package com.withpeace.withpeace.feature.post.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.post.PostRoute

const val POST_ROUTE = "postRoute"

fun NavController.navigatePost(navOptions: NavOptions? = null) {
    navigate(POST_ROUTE, navOptions)
}

fun NavGraphBuilder.postNavGraph(
    onShowSnackBar: (message: String) -> Unit,
) {
    composable(route = POST_ROUTE) {
        PostRoute(onShowSnackBar = onShowSnackBar)
    }
}