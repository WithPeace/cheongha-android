package com.withpeace.withpeace.navigation

import POLICY_REMOVED_ID_ARGUMENT
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.app.profileeditor.navigation.navigateProfileEditor
import com.app.profileeditor.navigation.profileEditorNavGraph
import com.withpeace.withpeace.core.designsystem.ui.snackbar.SnackbarState
import com.withpeace.withpeace.core.designsystem.ui.snackbar.SnackbarType
import com.withpeace.withpeace.feature.disablepolicy.navigation.disabledPolicyNavGraph
import com.withpeace.withpeace.feature.disablepolicy.navigation.navigateDisabledPolicy
import com.withpeace.withpeace.feature.gallery.navigation.galleryNavGraph
import com.withpeace.withpeace.feature.gallery.navigation.navigateToGallery
import com.withpeace.withpeace.feature.home.navigation.homeNavGraph
import com.withpeace.withpeace.feature.home.navigation.navigateHome
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import com.withpeace.withpeace.feature.login.navigation.loginNavGraph
import com.withpeace.withpeace.feature.login.navigation.navigateLogin
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_CHANGED_IMAGE_ARGUMENT
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_CHANGED_NICKNAME_ARGUMENT
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_ROUTE
import com.withpeace.withpeace.feature.mypage.navigation.myPageNavGraph
import com.withpeace.withpeace.feature.policyconsent.navigation.navigateToPolicyConsent
import com.withpeace.withpeace.feature.policyconsent.navigation.policyConsentGraph
import com.withpeace.withpeace.feature.policydetail.navigation.navigateToPolicyDetail
import com.withpeace.withpeace.feature.policydetail.navigation.policyDetailNavGraph
import com.withpeace.withpeace.feature.policylist.navigation.policyListGraph
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ROUTE_WITH_ARGUMENT
import com.withpeace.withpeace.feature.postdetail.navigation.navigateToPostDetail
import com.withpeace.withpeace.feature.postdetail.navigation.postDetailGraph
import com.withpeace.withpeace.feature.postlist.navigation.POST_LIST_DELETED_POST_ID_ARGUMENT
import com.withpeace.withpeace.feature.postlist.navigation.POST_LIST_ROUTE
import com.withpeace.withpeace.feature.postlist.navigation.navigateToPostList
import com.withpeace.withpeace.feature.postlist.navigation.postListGraph
import com.withpeace.withpeace.feature.privacypolicy.navigation.navigateToPrivacyPolicy
import com.withpeace.withpeace.feature.privacypolicy.navigation.privacyPolicyGraph
import com.withpeace.withpeace.feature.registerpost.navigation.IMAGE_LIST_ARGUMENT
import com.withpeace.withpeace.feature.registerpost.navigation.REGISTER_POST_ARGUMENT
import com.withpeace.withpeace.feature.registerpost.navigation.REGISTER_POST_ROUTE
import com.withpeace.withpeace.feature.registerpost.navigation.navigateToRegisterPost
import com.withpeace.withpeace.feature.registerpost.navigation.registerPostNavGraph
import com.withpeace.withpeace.feature.signup.navigation.navigateSignUp
import com.withpeace.withpeace.feature.signup.navigation.signUpNavGraph
import com.withpeace.withpeace.feature.termsofservice.navigation.navigateToTermsOfService
import com.withpeace.withpeace.feature.termsofservice.navigation.termsOfServiceGraph
import navigatePolicyBookmarks
import policyBookmarksNavGraph

@Composable
fun WithpeaceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = LOGIN_ROUTE,
    onShowSnackBar: (SnackbarState) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        loginNavGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onSignUpNeeded = {
                navController.navigateToPolicyConsent()
            },
            onLoginSuccess = {
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
        policyConsentGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onSuccessToNext = {
                navController.navigateSignUp()
            },
            onShowTermsOfServiceClick = {
                navController.navigateToTermsOfService()
            },
            onShowPrivacyPolicyClick = {
                navController.navigateToPrivacyPolicy()
            },
        )
        termsOfServiceGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onClickBackButton = {
                navController.popBackStack()
            },
        )

        privacyPolicyGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onClickBackButton = {
                navController.popBackStack()
            },
        )

        signUpNavGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
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
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onCompleteRegisterPost = { postId ->
                navController.navigateToPostDetail(
                    postId,
                    navOptions = navOptions {
                        // 수정일 경우 : 이전 화면이 상세화면이다
                        if (navController.previousBackStackEntry?.destination?.route == POST_DETAIL_ROUTE_WITH_ARGUMENT) {
                            popUpTo(POST_LIST_ROUTE) {
                                inclusive = false
                            }
                        } else {
                            // 새로 등록인 경우
                            popUpTo(REGISTER_POST_ROUTE) {
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
            onAuthExpired = {
                onAuthExpired(onShowSnackBar, navController)
            },
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
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
        )
        homeNavGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onNavigationSnackBar = {
                onShowSnackBar(
                    SnackbarState(
                        it,
                        SnackbarType.Navigator(
                            actionName = "목록 보러가기",
                            action = {
                                navController.navigatePolicyBookmarks()
                            },
                        ),
                    ),
                )
            },
            onPolicyClick = {
                navController.navigateToPolicyDetail(
                    policyId = it,
                )
            },
            onPostClick = { // TODO 인스턴스가 존재할 때, argument 로딩안됨
                navController.navigateToPostList(
                    it.name,
                    navOptions = navOptions {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    },
                )
            },
        )
        policyDetailNavGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onClickBackButton = { navController.popBackStack() },
            onNavigationSnackbar = {
                onShowSnackBar(
                    SnackbarState(
                        it,
                        SnackbarType.Navigator(
                            actionName = "목록 보러가기",
                            action = {
                                navController.navigatePolicyBookmarks()
                            },
                        ),
                    ),
                )
            },
        )
        navigation(startDestination = MY_PAGE_ROUTE, MY_PAGE_NESTED_ROUTE) {
            myPageNavGraph(
                onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
                onEditProfile = { nickname, profileImageUrl ->
                    navController.navigateProfileEditor(
                        nickname = nickname,
                        profileImageUrl = profileImageUrl,
                    )
                },
                onLogoutSuccess = {
                    navController.navigateLogin(
                        navOptions = navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        },
                    )
                },
                onWithdrawSuccess = {
                    navController.navigateLogin(
                        navOptions = navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        },
                    )
                },
                onAuthExpired = {
                    onAuthExpired(onShowSnackBar, navController)
                },
                onDibsOfPolicyClick = {
                    navController.navigatePolicyBookmarks()
                },
            )
            disabledPolicyNavGraph(
                onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
                onClickBackButton = {
                    navController.popBackStack()
                },
                onAuthExpired = {},
                onBookmarkDeleteSuccess = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        POLICY_REMOVED_ID_ARGUMENT,
                        it,
                    )
                    navController.popBackStack()
                },
            )
            profileEditorNavGraph(
                onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
                onClickBackButton = {
                    navController.popBackStack()
                },
                onNavigateToGallery = {
                    navController.navigateToGallery(imageLimit = 1)
                },
                onAuthExpired = {
                    onAuthExpired(onShowSnackBar, navController)
                },
                onUpdateSuccess = { nickname, imageUrl ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set(MY_PAGE_CHANGED_NICKNAME_ARGUMENT, nickname)
                        set(MY_PAGE_CHANGED_IMAGE_ARGUMENT, imageUrl)
                    }
                    navController.popBackStack()

                },
            )
        }
        policyBookmarksNavGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onClickBackButton = {
                navController.popBackStack()
            },
            onPolicyClick = {
                navController.navigateToPolicyDetail(policyId = it)
            },
            onDisablePolicyClick = {
                navController.navigateDisabledPolicy(policyId = it)
            },
        )
        postDetailGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onClickBackButton = navController::popBackStack,
            onClickEditButton = {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = REGISTER_POST_ARGUMENT,
                    value = it,
                )
                navController.navigateToRegisterPost()
            },
            onAuthExpired = {
                onAuthExpired(onShowSnackBar, navController)
            },
            onDeleteSuccess = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    POST_LIST_DELETED_POST_ID_ARGUMENT,
                    it,
                )
                navController.popBackStack()
            },
        )
        postListGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            navigateToPostDetail = navController::navigateToPostDetail,
            onAuthExpired = {
                onAuthExpired(onShowSnackBar, navController)
            },
            onClickRegisterPost = {
                navController.navigateToRegisterPost()
            }
        )
        policyListGraph(
            onShowSnackBar = { onShowSnackBar(SnackbarState(it)) },
            onNavigationSnackBar = {
                onShowSnackBar(
                    SnackbarState(
                        it,
                        SnackbarType.Navigator(
                            actionName = "목록 보러가기",
                            action = {
                                navController.navigatePolicyBookmarks()
                            },
                        ),
                    ),
                )
            },
            onPolicyClick = {
                navController.navigateToPolicyDetail(policyId = it)
            },
        )
    }
}

private fun onAuthExpired(
    onShowSnackBar: (SnackbarState) -> Unit,
    navController: NavHostController,
) {
    onShowSnackBar(SnackbarState("세션이 만료되었습니다. 로그인 후 다시 시도해 주세요."))
    navController.navigateLogin(
        navOptions = navOptions {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        },
    )
}

const val POST_NESTED_ROUTE = "post_nested_route"
const val MY_PAGE_NESTED_ROUTE = "my_page_nested_route"