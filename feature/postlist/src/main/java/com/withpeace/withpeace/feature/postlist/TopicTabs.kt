package com.withpeace.withpeace.feature.postlist

import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.ui.PostTopicUiState

@Composable
fun TopicTabs(
    currentTopic: PostTopic = PostTopic.FREEDOM,
    onClick: (PostTopic) -> Unit,
) {
    TabRow(selectedTabIndex = PostTopic.findIndex(currentTopic)) {
        PostTopicUiState.entries.forEachIndexed { index, postTopicUiState ->
            Tab(
                selected = postTopicUiState.topic == currentTopic,
                onClick = { onClick(postTopicUiState.topic) },
                text = { Text(text = stringResource(id = postTopicUiState.textResId)) },
                icon = {
                    Icon(
                        painter = painterResource(id = postTopicUiState.iconResId),
                        contentDescription = stringResource(id = postTopicUiState.textResId),
                        tint = if (currentTopic == postTopicUiState.topic) WithpeaceTheme.colors.MainPink
                        else WithpeaceTheme.colors.SystemGray2,
                    )
                },
            )
        }
    }
}
