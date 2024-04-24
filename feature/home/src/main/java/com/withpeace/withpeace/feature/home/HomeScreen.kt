package com.withpeace.withpeace.feature.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
}