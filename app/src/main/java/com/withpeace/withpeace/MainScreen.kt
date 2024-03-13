package com.withpeace.withpeace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.home.navigation.homeNavGraph
import com.withpeace.withpeace.feature.home.navigation.navigateHome
import com.withpeace.withpeace.feature.mypage.navigation.myPageNavGraph
import com.withpeace.withpeace.feature.mypage.navigation.navigateMyPage
import com.withpeace.withpeace.feature.post.navigation.navigatePost
import com.withpeace.withpeace.feature.post.navigation.postNavGraph

@Composable
fun MainRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    navController: NavHostController,
) {
    MainScreen(onShowSnackBar = onShowSnackBar, navController = navController)
}

@Composable
fun MainScreen(
    onShowSnackBar: (message: String) -> Unit = {},
    navController: NavHostController,
    startDestination: String = MAIN_ROUTE,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val clearAllStackOptions = navOptions { popUpTo(0) }
    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination ?: return@Scaffold,
                navController = navController,
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = startDestination) {
                homeNavGraph { navController.navigateHome(clearAllStackOptions) }
                postNavGraph { navController.navigatePost(clearAllStackOptions) }
                myPageNavGraph { navController.navigateMyPage(clearAllStackOptions) }
            }
        }
    }
}

@Composable
fun MainBottomBar(
    currentDestination: NavDestination,
    navController: NavHostController,
) {
    val context = LocalContext.current
    NavigationBar {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = currentDestination.route == tab.route,
                onClick = { navController.navigateToTabScreen(tab) },
                icon = {
                    Icon(
                        painterResource(id = tab.iconResId),
                        contentDescription = null,
                    )
                },
                label = { Text(text = tab.contentDescription) },
            )
        }
    }
}

fun NavController.navigateToTabScreen(bottomTab: BottomTab) {
    val tabNavOptions =
        navOptions {
            popUpTo(HOME_ROUTE) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    when (bottomTab) {
        BottomTab.HOME -> navigateHome(tabNavOptions)
        BottomTab.POST -> navigatePost(tabNavOptions)
        BottomTab.MY_PAGE -> navigateMyPage(tabNavOptions)
    }
}