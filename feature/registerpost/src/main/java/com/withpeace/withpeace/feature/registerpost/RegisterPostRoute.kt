package com.withpeace.withpeace.feature.registerpost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceCompleteButton
import com.withpeace.withpeace.core.domain.model.PostTopic

@Composable
fun RegisterPostRoute(
    viewModel: RegisterPostViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickedBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
) {
    RegisterPostScreen(
        onClickBackButton = {},
        onTitleChanged = {},
        onContentChanged = {},
        onTopicChanged = {},
        onCompleteRegisterPost = {},
    )
}

@Composable
fun RegisterPostScreen(
    onClickBackButton: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onContentChanged: (String) -> Unit = {},
    onTopicChanged: (PostTopic) -> Unit = {},
    onCompleteRegisterPost: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        RegisterPostTopAppBar(
            onClickBackButton = onClickBackButton,
            onCompleteRegisterPost = onCompleteRegisterPost,
        )
        RegisterPostTopic(
            topic = null, onTopicChanged = onTopicChanged,
        )
        RegisterPostTitle(
            title = "", onTitleChanged = onTitleChanged,
        )
        RegisterPostContent(
            content = "", onContentChanged = onContentChanged,
        )
    }
}

@Composable
fun RegisterPostTopAppBar(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
) {
    Column {
        WithPeaceBackButtonTopAppBar(
            modifier = modifier.padding(end = 24.dp),
            onClickBackButton = onClickBackButton,
            title = {
                Text(text = "글 쓰기", style = WithpeaceTheme.typography.title1)
            },
            actions = {
                WithPeaceCompleteButton(
                    onClick = onCompleteRegisterPost,
                    enabled = true,
                )
            },
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPostTopic(
    modifier: Modifier = Modifier,
    topic: PostTopic?,
    onTopicChanged: (PostTopic) -> Unit,
) {
    var showBottomModal by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Column(
        modifier = modifier.clickable { showBottomModal = true },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (topic == null) {
                    "게시글의 주제를 선택해주세요"
                } else {
                    stringResource(
                        id = PosterTopicUiState.create(
                            topic,
                        ).textResId,
                    )
                },
                modifier = Modifier.weight(1f),
                style = WithpeaceTheme.typography.body,
            )
            Icon(
                modifier = Modifier.padding(vertical = 14.dp),
                painter = painterResource(id = R.drawable.ic_backarrow_right),
                contentDescription = "TopicArrow",
            )
        }
        Divider(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
    if (showBottomModal) {
        ModalBottomSheet(
            dragHandle = {},
            containerColor = WithpeaceTheme.colors.SystemWhite,
            sheetState = bottomSheetState,
            onDismissRequest = { showBottomModal = false },
            windowInsets = WindowInsets(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
        ) {
            TopicBottomSheetContent(
                currentTopic = topic,
                onClickTopic = {
                    onTopicChanged(it)
                    showBottomModal = false
                },
            )
        }
    }
}

@Composable
fun TopicBottomSheetContent(
    currentTopic: PostTopic?,
    onClickTopic: (PostTopic) -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(start = 24.dp, top = 24.dp),
            text = "게시글의 주제를 선택해주세요",
            style = WithpeaceTheme.typography.title1,
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(top = 32.dp, bottom = 16.dp),
            columns = GridCells.Fixed(3),
        ) {
            items(
                items = PosterTopicUiState.entries,
            ) { topicUiState ->
                Icon(
                    modifier = Modifier
                        .clickable {
                            onClickTopic(topicUiState.topic)
                        }
                        .padding(bottom = 24.dp),
                    painter = painterResource(id = topicUiState.iconResId),
                    contentDescription = topicUiState.iconResId.toString(),
                    tint = if (currentTopic == topicUiState.topic) WithpeaceTheme.colors.MainPink
                    else WithpeaceTheme.colors.SystemGray2,
                )
            }
        }
    }
}

@Composable
fun RegisterPostTitle(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit,
) {

    Column(
        modifier = modifier.padding(
            horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
        ),
    ) {
        BasicTextField(
            modifier = Modifier.padding(vertical = 16.dp),
            value = if (title.isBlank()) "제목을 입력해주세요" else title,
            onValueChange = onTitleChanged,
            enabled = false,
            readOnly = false,
            textStyle = if (title.isBlank()) {
                WithpeaceTheme.typography.title2.copy(color = WithpeaceTheme.colors.SystemGray2)
            } else {
                WithpeaceTheme.typography.title2
            },
            singleLine = true,
            maxLines = 2,
            minLines = 1,
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
}

@Composable
fun RegisterPostContent(
    modifier: Modifier = Modifier,
    content: String,
    onContentChanged: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(
                horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
            )
            .verticalScroll(scrollState),
    ) {
        BasicTextField(
            modifier = Modifier.padding(vertical = 16.dp),
            value = if (content.isBlank()) "내용을 입력해주세요" else content,
            onValueChange = onContentChanged,
            enabled = false,
            readOnly = false,
            textStyle = if (content.isBlank()) {
                WithpeaceTheme.typography.body.copy(color = WithpeaceTheme.colors.SystemGray2)
            } else {
                WithpeaceTheme.typography.body
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RegisterPostScreenPreview() {
    WithpeaceTheme {
        RegisterPostScreen {

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TopicBottomSheetContentPreview() {
    WithpeaceTheme {
        TopicBottomSheetContent(currentTopic = null) {

        }
    }
}
