package com.withpeace.withpeace

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.withpeace.withpeace.navigation.WithpeaceNavHost
import kotlinx.coroutines.launch


@Composable
fun WithpeaceApp(
    startDestination: String
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    fun showSnackBar(message: String) = coroutineScope.launch {
        snackBarHostState.showSnackbar(message)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            WithpeaceNavHost(
                startDestination = startDestination,
                onShowSnackBar = ::showSnackBar,
            )
        }
    }
}
