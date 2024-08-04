package com.withpeace.withpeace.feature.disablepolicy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar

@Composable
fun DisablePolicyRoute(

) {
    DisablePolicyScreen()
}

@Composable
fun DisablePolicyScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = {},
            title = { },
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FB)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = modifier.height(183.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_illust_delete),
                contentDescription = "삭제",
            )

        }
    }
}

@Composable
@Preview
fun DisablePolicyPreview() {
    DisablePolicyScreen()
}