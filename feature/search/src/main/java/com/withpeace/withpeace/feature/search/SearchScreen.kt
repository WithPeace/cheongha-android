package com.withpeace.withpeace.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun SearchRoute() {
    SearchScreen()
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {

    }
}