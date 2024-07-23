package com.withpeace.withpeace.core.ui.bookmark

import androidx.compose.foundation.Image
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
    isClicked: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    Image(
        modifier = modifier.toggleable(
            value = isClicked, role = Role.Checkbox,
        ) {
            onClick(it)
        },
        painter = painterResource(
            id = if (isClicked) R.drawable.ic_heart else R.drawable.ic_empty_heart,
        ),
        contentDescription = "찜하기",
    )
}

@Composable
@Preview()
fun BookmarkButtonPreview() {
    WithpeaceTheme {
        BookmarkButton(onClick =  {})
    }
}

@Composable
@Preview()
fun BookmarkButtonClickedPreview() {
    WithpeaceTheme {
        BookmarkButton(isClicked = true, onClick = {})
    }
}