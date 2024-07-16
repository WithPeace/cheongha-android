package com.withpeace.withpeace.feature.policybookmarks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.bookmark.BookmarkButton

@Composable
fun PolicyBookmarksRoute(
    onShowSnackBar: (message: String) -> Unit,
) {
    PolicyBookmarksScreen()
}

@Composable
fun PolicyBookmarksScreen() {
    WithPeaceBackButtonTopAppBar(
        onClickBackButton = { },
        title = { Text(text = "내가 찜한 정책") },
        actions = { BookmarkButton() },
    )
}

@Composable
@Preview(showBackground = true)
fun PolicyBookmarksPreview() {
    PolicyBookmarksScreen()
}
