package com.withpeace.withpeace.feature.postlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun PostListRoute(
    onShowSnackBar: (String) -> Unit,
) {
    PostListScreen()
}

@Composable
fun PostListScreen(

) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopicTabs {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostListScreenPreview() {
    WithpeaceTheme {
        PostListScreen()
    }
}
