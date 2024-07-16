import androidx.hilt.navigation.compose.hiltViewModel
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
) {
    composable(route = POLICY_BOOKMARKS_ROUTE) {
        PolicyBookmarksRoute(
            onShowSnackBar = onShowSnackBar,
        )
    }
}