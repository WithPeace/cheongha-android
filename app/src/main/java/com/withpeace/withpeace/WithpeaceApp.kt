package com.withpeace.withpeace

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.snackbar.CheonghaSnackbar
import com.withpeace.withpeace.core.designsystem.ui.snackbar.SnackbarState
import com.withpeace.withpeace.core.designsystem.ui.snackbar.SnackbarType
import com.withpeace.withpeace.navigation.WithpeaceNavHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WithpeaceApp(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarState: SnackbarState =
        remember { SnackbarState("", SnackbarType.Normal) }

    val coroutineScope = rememberCoroutineScope()
    fun showSnackBar(snackbarState: SnackbarState) = coroutineScope.launch {
        snackBarState = snackbarState
        snackBarHostState.currentSnackbarData?.dismiss()
        val snackbarResult = snackBarHostState.showSnackbar(snackbarState.message)
        when (snackbarResult) {
            SnackbarResult.Dismissed -> Unit
            SnackbarResult.ActionPerformed ->
                (snackbarState.snackbarType as SnackbarType.Navigator).action()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val parentDestination = currentDestination?.parent

    Scaffold(
        bottomBar = {
            if (
                BottomTab.contains(parentDestination?.route ?: currentDestination?.route ?: "")
            ) {
                MainBottomBar(
                    currentDestination = if (parentDestination?.route == null) {
                        currentDestination ?: return@Scaffold
                    } else parentDestination,
                    navController = navController,
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
            {
                when (snackBarState.snackbarType) {
                    is SnackbarType.Navigator -> {}
                    is SnackbarType.Normal -> {
                        CheonghaSnackbar(data = it)
                    }
                }
            }
        },
        containerColor = WithpeaceTheme.colors.SystemWhite,
    ) { innerPadding ->
        WithpeaceNavHost(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            onShowSnackBar = ::showSnackBar,
            startDestination = startDestination,
            navController = navController,
        )
    }
}
