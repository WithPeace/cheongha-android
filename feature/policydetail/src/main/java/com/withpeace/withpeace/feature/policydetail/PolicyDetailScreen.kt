package com.withpeace.withpeace.feature.policydetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PolicyDetailRoute(
    onShowSnackBar: (message: String) -> Unit,
) {
    PolicyDetailScreen()
}

@Composable
fun PolicyDetailScreen(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
    }
}