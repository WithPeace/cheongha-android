package com.withpeace.withpeace.feature.postdetail.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.core.ui.post.RegisterPostUiModel
import com.withpeace.withpeace.feature.postdetail.PostDetailRoute

const val POST_DETAIL_ROUTE = "post_detail_route"
const val POST_DETAIL_ID_ARGUMENT = "post_id_argument"
const val POST_DETAIL_ROUTE_WITH_ARGUMENT =
    "$POST_DETAIL_ROUTE/{$POST_DETAIL_ID_ARGUMENT}"

fun NavController.navigateToPostDetail(
    postId: Long,
    navOptions: NavOptions? = null,
) = navigate(route = "$POST_DETAIL_ROUTE/$postId", navOptions)

fun NavGraphBuilder.postDetailGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onClickEditButton: (RegisterPostUiModel) -> Unit,
    onAuthExpired: () -> Unit,
    onDeleteSuccess: (Long) -> Unit,
) {
    composable(
        route = POST_DETAIL_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(POST_DETAIL_ID_ARGUMENT) { type = NavType.LongType },
        ),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500),
            )
        },
    ) {
        PostDetailRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            onClickEditButton = onClickEditButton,
            onAuthExpired = onAuthExpired,
            onDeleteSuccess = onDeleteSuccess,
        )
    }
}
