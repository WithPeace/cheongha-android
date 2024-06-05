package com.withpeace.withpeace.feature.privacypolicy

import androidx.compose.foundation.layout.Column
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
fun PrivacyPolicyRoute(
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    PrivacyPolicyScreen(onClickBackButton = onClickBackButton)
}

@Composable
fun PrivacyPolicyScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
) {
    Column {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = onClickBackButton,
            title = {
                Text(
                    text = "개인정보처리방침",
                    style = WithpeaceTheme.typography.title1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.padding(end = 24.dp),
                )
            },
        )
        WithpeaceWebView(url = "https://pointy-shampoo-9c7.notion.site/3671c1dbd4664645831707152f29105a")
    }
}


