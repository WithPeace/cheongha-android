package com.withpeace.withpeace.feature.termsofservice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.common.WithpeaceWebView

@Composable
fun TermsOfServiceRoute(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    TermsOfServiceScreen(onClickBackButton = onClickBackButton)
}

@Composable
fun TermsOfServiceScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = onClickBackButton,
            title = {
                Text(
                    text = "서비스 이용약관",
                    style = WithpeaceTheme.typography.title1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.padding(end = 24.dp),
                )
            },
        )
        WithpeaceWebView(url = "https://pointy-shampoo-9c7.notion.site/099b85822b9a4394848888fc2bc96560")
    }
}