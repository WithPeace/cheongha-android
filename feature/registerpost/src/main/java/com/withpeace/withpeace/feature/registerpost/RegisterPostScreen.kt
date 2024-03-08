package com.withpeace.withpeace.feature.registerpost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceCompleteButton
import com.withpeace.withpeace.core.domain.model.Post
import com.withpeace.withpeace.core.domain.model.PostTopic
import com.withpeace.withpeace.feature.registerpost.R.drawable

@Composable
fun KeyboardAware(
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.imePadding()) {
        content()
    }
}

@Composable
fun RegisterPostRoute(
    viewModel: RegisterPostViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickedBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
) {
    val postUiState = viewModel.postUiState.collectAsStateWithLifecycle().value
    val showBottomSheet = viewModel.showBottomSheet.collectAsStateWithLifecycle().value

    KeyboardAware {
    RegisterPostScreen(
        postUiState = postUiState,
        onClickBackButton = onClickedBackButton,
        onTitleChanged = viewModel::onTitleChanged,
        onContentChanged = viewModel::onContentChanged,
        onTopicChanged = viewModel::onTopicChanged,
        onCompleteRegisterPost = viewModel::onRegisterPostCompleted,
        onImageUrlsChanged = viewModel::onImageUrlsChanged,
        onShowBottomSheetChanged = viewModel::onShowBottomSheetChanged,
        showBottomSheet = showBottomSheet,
        onImageUrlDeleted = viewModel::onImageUrlDeleted,
    )
    }
    LaunchedEffect(null) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                RegisterPostUiEvent.ContentBlank -> onShowSnackBar("내용을 입력해 주세요")
                RegisterPostUiEvent.PostSuccess -> {
                    onCompleteRegisterPost()
                    onShowSnackBar("게시글 등록 가능")
                }

                RegisterPostUiEvent.TitleBlank -> onShowSnackBar("제목을 입력해 주세요")
                RegisterPostUiEvent.TopicBlank -> viewModel.onShowBottomSheetChanged(true)
            }
        }
    }
}

@Composable
fun RegisterPostScreen(
    postUiState: Post,
    onClickBackButton: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onContentChanged: (String) -> Unit = {},
    onTopicChanged: (PostTopic) -> Unit = {},
    onCompleteRegisterPost: () -> Unit,
    onImageUrlsChanged: (images: List<String>) -> Unit,
    onImageUrlDeleted: (String) -> Unit,
    onShowBottomSheetChanged: (Boolean) -> Unit = {},
    showBottomSheet: Boolean,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            RegisterPostTopAppBar(
                onClickBackButton = onClickBackButton,
                onCompleteRegisterPost = onCompleteRegisterPost,
            )
            RegisterPostTopic(
                topic = postUiState.topic,
                onTopicChanged = onTopicChanged,
                onShowBottomSheetChanged = onShowBottomSheetChanged,
                showBottomSheet = showBottomSheet,
            )
            RegisterPostTitle(
                title = postUiState.title, onTitleChanged = onTitleChanged,
            )
            RegisterPostContent(
                content = postUiState.content,
                onContentChanged = onContentChanged,
            )
        }
        Column {
            PostImageList(
                imageUrls = postUiState.imageUrls,
                onImageUrlDeleted = onImageUrlDeleted,
            )
            RegisterPostCamera(
                onImageUrlsChanged = onImageUrlsChanged,
            )
        }
    }
}

@Composable
fun PostImageList(
    imageUrls: List<String>,
    onImageUrlDeleted: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(start = WithpeaceTheme.padding.BasicHorizontalPadding),
    ) {
        items(
            items = imageUrls,
        ) { imageUrl ->
            Box(modifier = Modifier.size(122.dp)) {
                GlideImage(
                    modifier = Modifier
                        .size(110.dp)
                        .padding(top = 12.dp),
                    imageModel = { imageUrl },
                    previewPlaceholder = drawable.ic_cate_free,
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            onImageUrlDeleted(imageUrl)
                        }
                        .align(Alignment.TopEnd),
                    painter = painterResource(id = drawable.ic_cate_living),
                    contentDescription = "ImageDelete",
                )
            }
        }
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
    onShowBottomSheetChanged: (Boolean) -> Unit,
    showBottomSheet: Boolean,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Column(
        modifier = modifier.clickable { onShowBottomSheetChanged(true) },
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
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                style = WithpeaceTheme.typography.body,
            )
            Icon(
                modifier = Modifier,
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
    if (showBottomSheet) {
        ModalBottomSheet(
            dragHandle = {},
            containerColor = WithpeaceTheme.colors.SystemWhite,
            sheetState = bottomSheetState,
            onDismissRequest = { onShowBottomSheetChanged(false) },
            windowInsets = WindowInsets(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
        ) {
            TopicBottomSheetContent(
                currentTopic = topic,
                onClickTopic = {
                    onTopicChanged(it)
                    onShowBottomSheetChanged(false)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPostTitle(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.padding(
            horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
        ),
    ) {
        BasicTextField(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            value = title,
            onValueChange = onTitleChanged,
            enabled = true,
            textStyle = WithpeaceTheme.typography.title1,
            singleLine = true,
            maxLines = 2,
            minLines = 1,
        ) {
            TextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = "제목을 입력해주세요",
                        style = WithpeaceTheme.typography.title2,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPostContent(
    modifier: Modifier = Modifier,
    content: String,
    onContentChanged: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        BasicTextField(
            modifier = Modifier
                .padding(
                    vertical = 16.dp,
                    horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
                )
                .fillMaxWidth(),
            value = content,
            onValueChange = onContentChanged,
            enabled = true,
            textStyle = WithpeaceTheme.typography.body,
            minLines = 10,
        ) {
            TextFieldDefaults.DecorationBox(
                value = content,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = "내용을 입력해주세요",
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
fun RegisterPostCamera(
    modifier: Modifier = Modifier,
    onImageUrlsChanged: (List<String>) -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
        ),
    ) {
        Divider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Row(
            modifier = modifier.padding(
                bottom = 16.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .clickable { }
                    .padding(end = 8.dp),
                painter = painterResource(id = drawable.ic_camera),
                contentDescription = "CameraIcon",
            )
            Text(text = "사진", style = WithpeaceTheme.typography.caption)
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RegisterPostScreenPreview() {
    WithpeaceTheme {
        RegisterPostScreen(
            onCompleteRegisterPost = {},
            onImageUrlsChanged = {},
            showBottomSheet = false,
            onImageUrlDeleted = {},
            onShowBottomSheetChanged = {},
            postUiState = Post("", "", PostTopic.INFORMATION, listOf("", "", "")),
        )
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
