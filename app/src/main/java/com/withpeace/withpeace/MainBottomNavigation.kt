package com.withpeace.withpeace

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.home.navigation.navigateHome
import com.withpeace.withpeace.feature.policylist.navigation.POLICY_LIST_ROUTE
import com.withpeace.withpeace.feature.policylist.navigation.navigateToPolicyList
import com.withpeace.withpeace.feature.postlist.navigation.POST_LIST_ROUTE
import com.withpeace.withpeace.feature.postlist.navigation.navigateToPostList
import com.withpeace.withpeace.navigation.MY_PAGE_NESTED_ROUTE

@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination,
    navController: NavHostController,
) {
    val context = LocalContext.current
    NavigationBar(
        modifier = modifier,
        containerColor = WithpeaceTheme.colors.SystemWhite,
    ) {
        BottomTab.entries.forEach { tab ->
            NavigationBarItem(
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedTextColor = WithpeaceTheme.colors.SystemBlack,
                        unselectedTextColor = WithpeaceTheme.colors.SystemGray2,
                        indicatorColor = WithpeaceTheme.colors.SystemWhite,
                    ),
                selected = currentDestination.route == tab.route,
                onClick = { navController.navigateToTabScreen(tab) },
                icon = {
                    Image(
                        painter =
                            painterResource(
                                id =
                                    if (currentDestination.route == tab.route) {
                                        tab.iconSelectedResId
                                    } else {
                                        tab.iconUnSelectedResId
                                    },
                            ),
                        contentDescription = context.getString(tab.contentDescription),
                    )
                },
                label = { Text(text = context.getString(tab.contentDescription)) },
            )
        }
    }
}

private fun NavController.navigateToTabScreen(bottomTab: BottomTab) {
    val tabNavOptions =
        navOptions {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    // 여기 아니면 NavHost

    when (bottomTab) {
        BottomTab.HOME -> navigateHome(tabNavOptions)
        BottomTab.POLICY -> navigateToPolicyList(tabNavOptions)
        BottomTab.POST -> navigateToPostList(tabNavOptions)
        BottomTab.MY_PAGE -> navigate(MY_PAGE_NESTED_ROUTE, tabNavOptions)
    }
}

enum class BottomTab(
    val iconUnSelectedResId: Int,
    val iconSelectedResId: Int,
    @StringRes val contentDescription: Int,
    val route: String,
) {
    HOME(
        iconUnSelectedResId = R.drawable.ic_bottom_home,
        iconSelectedResId = R.drawable.ic_bottom_home_select,
        contentDescription = R.string.home,
        HOME_ROUTE,
    ),
    POLICY(
        iconUnSelectedResId = R.drawable.ic_bottom_policy,
        iconSelectedResId = R.drawable.ic_bottom_policy_select,
        contentDescription = R.string.youth_policy,
        POLICY_LIST_ROUTE,
    ),
    POST(
        iconUnSelectedResId = R.drawable.ic_bottom_community,
        iconSelectedResId = R.drawable.ic_bottom_community_select,
        contentDescription = R.string.post,
        POST_LIST_ROUTE,
    ),
    MY_PAGE(
        iconUnSelectedResId = R.drawable.ic_bottom_my_page,
        iconSelectedResId = R.drawable.ic_bottom_my_page_select,
        contentDescription = R.string.my_page,
        MY_PAGE_NESTED_ROUTE,
    ),
    ;
}
// https://stackoverflow.com/questions/76721423/compose-navigation-go-to-top-level-destination-when-clicking-on-navigation-bar