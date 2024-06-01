package com.withpeace.withpeace.feature.policydetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.core.ui.policy.YouthPolicyUiModel
import com.withpeace.withpeace.core.ui.serializable.toNavigationValue
import com.withpeace.withpeace.feature.policydetail.PolicyDetailRoute
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

const val POLICY_DETAIL_ROUTE = "policyDetailRoute"
const val POLICY_DETAIL_YOUTH_POLICY_ARGUMENT = "youthPolicy_argument"
const val POLICY_DETAIL_ROUTE_WITH_ARGUMENT =
    "$POLICY_DETAIL_ROUTE/{$POLICY_DETAIL_YOUTH_POLICY_ARGUMENT}"

fun NavController.navigateToPolicyDetail(
    navOptions: NavOptions? = null,
    policy: YouthPolicyUiModel,
) {
    val policyDetail = YouthPolicyUiModel.toNavigationValue(policy)
    navigate("$POLICY_DETAIL_ROUTE/${policyDetail}", navOptions)
}
fun NavGraphBuilder.policyDetailNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable(
        route = POLICY_DETAIL_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(POLICY_DETAIL_YOUTH_POLICY_ARGUMENT) {
                type = NavType.StringType
            },
        ),
    ) {

        val policy: YouthPolicyUiModel = YouthPolicyUiModel.parseNavigationValue(
            it.arguments?.getString(POLICY_DETAIL_YOUTH_POLICY_ARGUMENT) ?: ""
        )
        // val arguments = requireNotNull(it.arguments)
        // val policy: YouthPolicyUiModel =
        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        //         requireNotNull(
        //             arguments.getSerializable(
        //                 POLICY_DETAIL_YOUTH_POLICY_ARGUMENT,
        //                 YouthPolicyUiModel::class.java,
        //             ),
        //         )
        //     } else ({
        //         arguments.getSerializable(POLICY_DETAIL_YOUTH_POLICY_ARGUMENT)
        //     }) as YouthPolicyUiModel

        PolicyDetailRoute(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = onClickBackButton,
            policy = policy,
        )
    }
}