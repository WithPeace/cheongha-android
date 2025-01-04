package com.withpeace.withpeace.feature.balancegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar

@Composable
fun BalanceGameRoute(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    BalanceGameScreen(
        onClickBackButton = onClickBackButton,
    )
}

@Composable
fun BalanceGameScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithPeaceBackButtonTopAppBar(
                modifier = modifier,
                onClickBackButton = { onClickBackButton() },
                title = {
                    Text(
                        text = "밸런스 게임",
                        style = WithpeaceTheme.typography.title1,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                },
            )
        }
    }
}