package com.withpeace.withpeace.feature.balancegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun BalanceGameRoute(
    onShowSnackBar: (String) -> Unit,
    onBackButtonClick: () -> Unit,
) {
    BalanceGameScreen()
}

@Composable
fun BalanceGameScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {

    }
}