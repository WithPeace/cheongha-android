package com.withpeace.withpeace.core.ui.bookmark

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
    isClick: Boolean = false,
) {
    Image(
        modifier = modifier,
        painter = painterResource(
            id = if (isClick) R.drawable.ic_heart else com.withpeace.withpeace.core.ui.R.drawable.ic_empty_heart,
        ),
        contentDescription = "찜하기",
    )
}

@Composable
@Preview()
fun BookmarkButtonPreview() {
    WithpeaceTheme {
        BookmarkButton()
    }
}

@Composable
@Preview()
fun BookmarkButtonClickedPreview() {
    WithpeaceTheme {
        BookmarkButton(isClick = true)
    }
}