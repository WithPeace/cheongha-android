package com.withpeace.withpeace.feature.postlist.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.feature.postlist.PostListRoute
import com.withpeace.withpeace.feature.postlist.PostListViewModel

const val POST_LIST_ROUTE = "post_list_route"
const val POST_LIST_DELETED_POST_ID_ARGUMENT = "post_list_deleted_post_id"

const val POST_TYPE_ARGUMENT = "youthPolicy_argument"
const val POST_LIST_ROUTE_WITH_ARGUMENT =
    "$POST_LIST_ROUTE/{$POST_TYPE_ARGUMENT}"

fun NavController.navigateToPostList(postTopic: String? = null, navOptions: NavOptions? = null) =
    navigate("$POST_LIST_ROUTE/${postTopic ?: PostTopicUiModel.FREEDOM}", navOptions)

fun NavGraphBuilder.postListGraph(
    onShowSnackBar: (String) -> Unit,
    navigateToPostDetail: (postId: Long) -> Unit,
    onAuthExpired: () -> Unit,
    onClickRegisterPost: () -> Unit = {},
) {
    composable(
        arguments = listOf(
            navArgument(POST_TYPE_ARGUMENT) {
                type = NavType.StringType
            },
        ),
        route = POST_LIST_ROUTE_WITH_ARGUMENT,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        val viewModel: PostListViewModel = hiltViewModel()
        val deletedId = it.savedStateHandle.get<Long>(POST_LIST_DELETED_POST_ID_ARGUMENT)

        val postType = it.savedStateHandle.get<String>(POST_TYPE_ARGUMENT)
        // postType?.let { // recomposition이 안되는건가
        //     viewModel.onTopicChanged(
        //         PostTopicUiModel.entries.find {
        //             it.name == postType
        //         } ?: PostTopicUiModel.FREEDOM,
        //     )
        // }
        deletedId?.let { id ->
            viewModel.updateDeletedId(id)
        }
        PostListRoute(
            viewModel = viewModel,
            onShowSnackBar = onShowSnackBar,
            navigateToDetail = navigateToPostDetail,
            onAuthExpired = onAuthExpired,
            onClickRegisterPost = onClickRegisterPost,
        )
    }
}
