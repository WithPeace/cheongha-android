import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.policybookmarks.PolicyBookmarksRoute

const val POLICY_BOOKMARKS_ROUTE = "POLICY_BOOKMARKS_ROUTE"

fun NavController.navigatePolicyBookmarks(navOptions: NavOptions? = null) {
    navigate(POLICY_BOOKMARKS_ROUTE, navOptions)
}

fun NavGraphBuilder.policyBookmarksNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
    onPolicyClick: (String) -> Unit,
    onDisablePolicyClick: (String) -> Unit,
) {
    composable(
        route = POLICY_BOOKMARKS_ROUTE,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500),
            )
        },
    ) {
        PolicyBookmarksRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            onPolicyClick = onPolicyClick,
            onDisablePolicyClick = onDisablePolicyClick,
        )
    }
}