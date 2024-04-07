package com.withpeace.withpeace.feature.registerpost.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.core.ui.post.RegisterPostUiModel
import com.withpeace.withpeace.feature.registerpost.RegisterPostRoute
import com.withpeace.withpeace.feature.registerpost.RegisterPostViewModel

const val REGISTER_POST_ROUTE = "register_post_route"
const val IMAGE_LIST_ARGUMENT = "image_list_argument"
const val REGISTER_POST_ARGUMENT = "register_post_argument"

fun NavController.navigateToRegisterPost(navOptions: NavOptions? = null) = navigate(
    REGISTER_POST_ROUTE, navOptions,
)

fun NavGraphBuilder.registerPostNavGraph(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onCompleteRegisterPost: (postId: Long) -> Unit,
    onNavigateToGallery: (imageLimit: Int, imageCount: Int) -> Unit,
    originPost: RegisterPostUiModel?,
) {
    composable(route = REGISTER_POST_ROUTE) { entry ->
        val viewModel: RegisterPostViewModel = hiltViewModel()

        viewModel.initRegisterPost(originPost)

        // 갤러리에서 받아온 이미지를 추가하고, 화면회전에 대처하기 위해 savedStateHandle을 초기화해준다.
        val selectedImageUrls =
            entry.savedStateHandle.get<List<String>>(IMAGE_LIST_ARGUMENT)?: emptyList()
        viewModel.onImageUrlsAdded(selectedImageUrls)
        entry.savedStateHandle[IMAGE_LIST_ARGUMENT] = emptyList<String>()

        RegisterPostRoute(
            viewModel = viewModel,
            onShowSnackBar = onShowSnackBar,
            onClickedBackButton = onClickBackButton,
            onCompleteRegisterPost = onCompleteRegisterPost,
            onNavigateToGallery = onNavigateToGallery,
        )
    }
}
