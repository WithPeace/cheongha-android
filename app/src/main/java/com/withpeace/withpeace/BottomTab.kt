package com.withpeace.withpeace

import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.mypage.navigation.MY_PAGE_ROUTE
import com.withpeace.withpeace.feature.post.navigation.POST_ROUTE

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