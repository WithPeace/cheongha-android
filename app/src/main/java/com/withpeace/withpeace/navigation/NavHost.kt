package com.withpeace.withpeace.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.app.profileeditor.navigation.navigateProfileEditor
import com.app.profileeditor.navigation.profileEditorNavGraph
import com.withpeace.withpeace.feature.gallery.navigation.galleryNavGraph
import com.withpeace.withpeace.feature.gallery.navigation.navigateToGallery
import com.withpeace.withpeace.feature.home.navigation.homeNavGraph
import com.withpeace.withpeace.feature.home.navigation.navigateHome
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import com.withpeace.withpeace.feature.login.navigation.loginNavGraph
import com.withpeace.withpeace.feature.login.navigation.navigateLogin
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_CHANGED_IMAGE_ARGUMENT
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_CHANGED_NICKNAME_ARGUMENT
import com.withpeace.withpeace.feature.mypage.navigation.myPageNavGraph
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ROUTE_WITH_ARGUMENT
import com.withpeace.withpeace.feature.postdetail.navigation.navigateToPostDetail
import com.withpeace.withpeace.feature.postdetail.navigation.postDetailGraph
import com.withpeace.withpeace.feature.postlist.navigation.POST_LIST_ROUTE
import com.withpeace.withpeace.feature.postlist.navigation.postListGraph
import com.withpeace.withpeace.feature.registerpost.navigation.IMAGE_LIST_ARGUMENT
import com.withpeace.withpeace.feature.registerpost.navigation.REGISTER_POST_ARGUMENT
import com.withpeace.withpeace.feature.registerpost.navigation.REGISTER_POST_ROUTE
import com.withpeace.withpeace.feature.registerpost.navigation.navigateToRegisterPost
import com.withpeace.withpeace.feature.registerpost.navigation.registerPostNavGraph
import com.withpeace.withpeace.feature.signup.navigation.navigateSignUp
import com.withpeace.withpeace.feature.signup.navigation.signUpNavGraph

@Composable
fun WithpeaceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = LOGIN_ROUTE,
    onShowSnackBar: (message: String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        loginNavGraph(
            onShowSnackBar = onShowSnackBar,
            onSignUpNeeded = {
                navController.navigateSignUp()
            },
            onLoginSuccess = {
                navController.navigateHome()
            },
        )
        signUpNavGraph(
            onShowSnackBar = onShowSnackBar,
            onNavigateToGallery = {
                navController.navigateToGallery(imageLimit = 1)
            },
            onSignUpSuccess = {
                navController.navigateHome(
                    navOptions =
                        navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        },
                )
            },
        )
        registerPostNavGraph(
            onShowSnackBar = onShowSnackBar,
            onCompleteRegisterPost = { postId ->
                navController.navigateToPostDetail(
                    postId,
                    navOptions = navOptions {
                        // 수정일 경우 : 이전 화면이 상세화면이다
                        if (navController.previousBackStackEntry?.destination?.route == POST_DETAIL_ROUTE_WITH_ARGUMENT) {
                            popUpTo(POST_LIST_ROUTE){
                                inclusive = false
                            }
                        } else {
                            // 새로 등록인 경우
                            popUpTo(REGISTER_POST_ROUTE){
                                inclusive = true
                            }
                        }
                    },
                )
            },
            onClickBackButton = navController::popBackStack,
            onNavigateToGallery = { imageLimit, imageCount ->
                navController.navigateToGallery(
                    imageLimit = imageLimit,
                    currentImageCount = imageCount,
                )
            },
            originPost = navController.previousBackStackEntry?.savedStateHandle?.get(
                REGISTER_POST_ARGUMENT,
            ),
        )
        galleryNavGraph(
            onClickBackButton = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IMAGE_LIST_ARGUMENT,
                    emptyList<String>(),
                )
                navController.popBackStack()
            },
            onCompleteRegisterImages = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IMAGE_LIST_ARGUMENT,
                    it,
                )
                navController.popBackStack()
            },
            onShowSnackBar = onShowSnackBar,
        )
        homeNavGraph(onShowSnackBar)
        postDetailGraph(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = navController::popBackStack,
            onClickEditButton = {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = REGISTER_POST_ARGUMENT,
                    value = it,
                )
                navController.navigateToRegisterPost()
            },
        )
        postListGraph(
            onShowSnackBar = onShowSnackBar,
            navigateToPostDetail = navController::navigateToPostDetail,
        )
        myPageNavGraph(
            onShowSnackBar = onShowSnackBar,
            onEditProfile = { nickname, profileImageUrl ->
                navController.navigateProfileEditor(
                    nickname = nickname,
                    profileImageUrl = profileImageUrl,
                )
            },
            onLogoutSuccess = {
                navController.navigateLogin(
                    navOptions =
                        navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        },
                )
            },
            onWithdrawClick = {},
        )
        profileEditorNavGraph(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = {
                navController.popBackStack()
            },
            onNavigateToGallery = {
                navController.navigateToGallery(imageLimit = 1)
            },
            onUpdateSuccess = { nickname, imageUrl ->
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set(MY_PAGE_CHANGED_NICKNAME_ARGUMENT, nickname)
                    set(MY_PAGE_CHANGED_IMAGE_ARGUMENT, imageUrl)
                }
                navController.popBackStack()
            },
        )
        postListGraph(onShowSnackBar, navigateToPostDetail = navController::navigateToPostDetail)
    }
}
