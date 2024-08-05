import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.policybookmarks.PolicyBookmarkViewModel
import com.withpeace.withpeace.feature.policybookmarks.PolicyBookmarksRoute

const val POLICY_BOOKMARKS_ROUTE = "POLICY_BOOKMARKS_ROUTE"
const val POLICY_REMOVED_ID_ARGUMENT = "POLICY_REMOVED_ID_ARGUMENT"

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
    ) { navBackStackEntry ->
        val policyId = navBackStackEntry.savedStateHandle.get<String>(POLICY_REMOVED_ID_ARGUMENT)
        val viewModel: PolicyBookmarkViewModel = hiltViewModel()
        policyId?.let {
            Log.d("test",policyId)
            viewModel.deleteBookmarkedPolicy(it)
        }

        PolicyBookmarksRoute(
            viewModel = viewModel,
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            onPolicyClick = onPolicyClick,
            onDisablePolicyClick = onDisablePolicyClick,
        )
    }
}