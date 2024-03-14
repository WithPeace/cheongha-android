package com.withpeace.withpeace

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.home.navigation.navigateHome
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_ROUTE
import com.withpeace.withpeace.feature.mypage.navigation.navigateMyPage
import com.withpeace.withpeace.feature.post.navigation.POST_ROUTE
import com.withpeace.withpeace.feature.post.navigation.navigatePost

@Composable
fun MainBottomBar(
    currentDestination: NavDestination,
    navController: NavHostController,
) {
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

private fun NavController.navigateToTabScreen(bottomTab: BottomTab) {
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

enum class BottomTab(
    val iconResId: Int,
    internal val contentDescription: String,
    val route: String,
) {
    HOME(
        iconResId = R.drawable.ic_bottom_home,
        contentDescription = "홈",
        HOME_ROUTE,
    ),
    POST(
        iconResId = R.drawable.ic_bottom_post,
        contentDescription = "게시판",
        POST_ROUTE,
    ),
    MY_PAGE(
        iconResId = R.drawable.ic_bottom_my_page,
        contentDescription = "마이페이지",
        MY_PAGE_ROUTE,
    );

    companion object {
        operator fun contains(route: String): Boolean {
            return entries.map { it.route }.contains(route)
        }

        fun find(route: String): BottomTab? {
            return entries.find { it.route == route }
        }
    }
}