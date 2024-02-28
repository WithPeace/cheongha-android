package com.withpeace.withpeace

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.withpeace.withpeace.navigation.WithpeaceNavHost
import kotlinx.coroutines.launch


@Composable
fun WithpeaceApp() {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun showSnackBar(message: String) = coroutineScope.launch {
        snackBarHostState.showSnackbar(message)
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        WithpeaceNavHost(
            onShowSnackBar = ::showSnackBar,
        )
        it
    }
}