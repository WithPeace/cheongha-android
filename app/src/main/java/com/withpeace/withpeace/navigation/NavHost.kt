package com.withpeace.withpeace.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.withpeace.withpeace.feature.gallery.navigation.galleryNavGraph
import com.withpeace.withpeace.feature.gallery.navigation.navigateToGallery
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import com.withpeace.withpeace.feature.login.navigation.loginNavGraph
import com.withpeace.withpeace.feature.registerpost.navigation.REGISTER_POST_ROUTE
import com.withpeace.withpeace.feature.registerpost.navigation.registerPostNavGraph

@Composable
fun WithpeaceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = LOGIN_ROUTE,
    onShowSnackBar: (message: String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = REGISTER_POST_ROUTE,
    ) {
        loginNavGraph(onShowSnackBar = onShowSnackBar)
        registerPostNavGraph(
            onShowSnackBar = onShowSnackBar,
            onCompleteRegisterPost = {},
            onClickBackButton = {},
            onClickCameraButton = { navController.navigateToGallery(imageLimit = it) },
        )
        galleryNavGraph(
            onClickBackButton = navController::popBackStack,
            onCompleteRegisterImages = {},
        )
    }
}
