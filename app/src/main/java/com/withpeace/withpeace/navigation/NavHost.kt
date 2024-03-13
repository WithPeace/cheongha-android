package com.withpeace.withpeace.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import com.withpeace.withpeace.feature.login.navigation.loginNavGraph
import com.withpeace.withpeace.mainNavGraph


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
        startDestination = startDestination,
    ) {
        loginNavGraph(onShowSnackBar = onShowSnackBar)
        mainNavGraph(
            onShowSnackBar = onShowSnackBar,
            navController = navController,
        )
    }
}