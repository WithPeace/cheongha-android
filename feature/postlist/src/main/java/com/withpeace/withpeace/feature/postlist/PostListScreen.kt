package com.withpeace.withpeace.feature.postlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import java.time.LocalDateTime

@Composable
fun PostListRoute(
    viewModel: PostListViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val postList = viewModel.postList.collectAsStateWithLifecycle().value
    val currentTopic = viewModel.currentTopic.collectAsStateWithLifecycle().value
    PostListScreen(
        currentTopic = currentTopic,
        postList,
        onTopicChanged = viewModel::onTopicChanged,
    )
}

@Composable
fun PostListScreen(
    currentTopic: PostTopic,
    postList: List<Post>,
    onTopicChanged: (PostTopic) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        TopicTabs(
            currentTopic = currentTopic,
            onClick = onTopicChanged,
            tabPosition = PostTopic.findIndex(currentTopic),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostListScreenPreview() {
    WithpeaceTheme {
        PostListScreen(
            currentTopic = PostTopic.FREEDOM,
            postList = List(10) {
                Post(
                    postId = 2049,
                    title = "periculis",
                    content = "pellentesque",
                    postTopic = PostTopic.INFORMATION,
                    createDate = LocalDateTime.now(),
                    postImageUrl = "https://duckduckgo.com/?q=verterem",
                )
            },
        )
    }
}
