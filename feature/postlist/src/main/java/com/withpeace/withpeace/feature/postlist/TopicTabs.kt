package com.withpeace.withpeace.feature.postlist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.analytics.AnalyticsEntryPoint
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.analytics.topicClick
import dagger.hilt.android.EntryPointAccessors

@Composable
fun TopicTabs(
    currentTopic: PostTopicUiModel,
    tabPosition: Int,
    onClick: (PostTopicUiModel) -> Unit,
) {
    val analyticsHelper = EntryPointAccessors.fromApplication(
        LocalContext.current,
        AnalyticsEntryPoint::class.java,
    ).get()
    TabRow(
        modifier = Modifier.wrapContentSize(),
        selectedTabIndex = tabPosition,
        containerColor = WithpeaceTheme.colors.SystemWhite,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabPosition])
                    .padding(horizontal = 16.dp),
                color = WithpeaceTheme.colors.MainPurple,
            )
        },
    ) {
        PostTopicUiModel.entries.forEachIndexed { index, postTopic ->
            val color = if (currentTopic == postTopic) WithpeaceTheme.colors.MainPurple
            else WithpeaceTheme.colors.SystemGray2
            Tab(
                selected = postTopic == currentTopic,
                onClick = {
                    onClick(postTopic)
                    analyticsHelper.topicClick(currentTopic)
                },
                text = {
                    Text(
                        text = stringResource(id = postTopic.textResId),
                        color = color,
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = postTopic.iconResId),
                        contentDescription = stringResource(id = postTopic.textResId),
                        tint = color,
                    )
                },
            )
        }
    }
}

