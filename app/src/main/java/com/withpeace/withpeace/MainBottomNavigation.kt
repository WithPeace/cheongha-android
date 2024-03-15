package com.withpeace.withpeace

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.home.navigation.navigateHome
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_ROUTE
import com.withpeace.withpeace.feature.mypage.navigation.navigateMyPage
import com.withpeace.withpeace.feature.post.navigation.POST_ROUTE
import com.withpeace.withpeace.feature.post.navigation.navigatePost

@Composable
fun MainBottomBar(
    modifier: Modifier,
    currentDestination: NavDestination,
    navController: NavHostController,
) {
    NavigationBar(
        containerColor = WithpeaceTheme.colors.SystemWhite,
    ) {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = WithpeaceTheme.colors.SystemBlack,
                    unselectedTextColor = WithpeaceTheme.colors.SystemGray2,
                    indicatorColor = WithpeaceTheme.colors.SystemWhite,
                ),
                selected = currentDestination.route == tab.route,
                onClick = { navController.navigateToTabScreen(tab) },
                icon = {
                    Image(
                        painter = painterResource(
                            id = if (currentDestination.route == tab.route) tab.iconSelectedResId
                            else tab.iconUnSelectedResId,
                        ),
                        contentDescription = tab.contentDescription,
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
    val iconUnSelectedResId: Int,
    val iconSelectedResId: Int,
    internal val contentDescription: String,
    val route: String,
) {
    HOME(
        iconUnSelectedResId = R.drawable.ic_bottom_home,
        iconSelectedResId = R.drawable.ic_bottom_home_select,
        contentDescription = "홈",
        HOME_ROUTE,
    ),
    POST(
        iconUnSelectedResId = R.drawable.ic_bottom_post,
        iconSelectedResId = R.drawable.ic_bottom_post_select,
        contentDescription = "게시판",
        POST_ROUTE,
    ),
    MY_PAGE(
        iconUnSelectedResId = R.drawable.ic_bottom_my_page,
        iconSelectedResId = R.drawable.ic_bottom_my_page_select,
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