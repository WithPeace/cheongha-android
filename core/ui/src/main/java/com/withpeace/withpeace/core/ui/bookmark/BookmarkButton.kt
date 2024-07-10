package com.withpeace.withpeace.core.ui.bookmark

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = com.withpeace.withpeace.core.ui.R.drawable.ic_empty_heart),
        contentDescription = "찜하기",
    )
}