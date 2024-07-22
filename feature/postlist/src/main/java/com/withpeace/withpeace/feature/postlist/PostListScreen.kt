package com.withpeace.withpeace.feature.postlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.PretendardFont
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithpeaceCard
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.analytics.TrackScreenViewEvent
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.PostUiModel
import com.withpeace.withpeace.core.ui.toRelativeString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime

@Composable
fun PostListRoute(
    viewModel: PostListViewModel = hiltViewModel(),
    navigateToDetail: (postId: Long) -> Unit,
    onShowSnackBar: (String) -> Unit,
    onAuthExpired: () -> Unit,
) {
    val postListPagingData = viewModel.postListPagingFlow.collectAsLazyPagingItems()
    val currentTopic by viewModel.currentTopic.collectAsStateWithLifecycle()
    PostListScreen(
        currentTopic = currentTopic,
        postListPagingData = postListPagingData,
        onTopicChanged = viewModel::onTopicChanged,
        navigateToDetail = navigateToDetail,
    )

    LaunchedEffect(null) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                PostListUiEvent.NetworkError -> onShowSnackBar("네트워크를 확인해 주세요")
                PostListUiEvent.UnAuthorizedError -> onAuthExpired()
            }
        }
    }
}

@Composable
fun PostListScreen(
    currentTopic: PostTopicUiModel,
    postListPagingData: LazyPagingItems<PostUiModel>,
    onTopicChanged: (PostTopicUiModel) -> Unit = {},
    navigateToDetail: (postId: Long) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize().background(WithpeaceTheme.colors.SystemWhite)) {
        Spacer(modifier = Modifier.height(8.dp))
        TopicTabs(
            currentTopic = currentTopic,
            onClick = onTopicChanged,
            tabPosition = currentTopic.index,
        )
        when (postListPagingData.loadState.refresh) {
            is LoadState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = WithpeaceTheme.colors.MainPurple,
                    )
                }
            }

            is LoadState.Error -> {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = "네트워크 상태를 확인해주세요.",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }

            is LoadState.NotLoading -> {
                PostListItems(
                    postListPagingData = postListPagingData,
                    navigateToDetail = navigateToDetail,
                )
            }
        }
    }
    TrackScreenViewEvent(screenName = "post_list")
}

@Composable
fun PostListItems(
    postListPagingData: LazyPagingItems<PostUiModel>,
    navigateToDetail: (postId: Long) -> Unit = {},
) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = postListPagingData.itemCount,
            key = postListPagingData.itemKey { it.postId }
        ) {
            val post = postListPagingData[it] ?: throw IllegalStateException()
            WithpeaceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToDetail(post.postId) },
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .padding(vertical = 15.dp, horizontal = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    val (column, image) = createRefs()
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .constrainAs(column) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(image.start)
                                width = Dimension.fillToConstraints
                            },
                    ) {
                        Text(
                            text = post.title,
                            fontFamily = PretendardFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = post.content,
                            style = WithpeaceTheme.typography.caption,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = WithpeaceTheme.colors.SystemBlack,
                        )
                        Row(
                            modifier = Modifier
                                .height(IntrinsicSize.Min),
                        ) {
                            Image(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                painter =
                                painterResource(id = com.withpeace.withpeace.feature.postlist.R.drawable.ic_comment),
                                contentDescription = stringResource(
                                    com.withpeace.withpeace.feature.postlist.R.string.comment_count,
                                ),
                            )
                            Text(
                                modifier = Modifier.padding(start = 2.dp),
                                text = post.commentCount,
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Normal,
                                color = WithpeaceTheme.colors.SystemBlack,
                                fontSize = 12.sp,
                            )
                            VerticalDivider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 2.dp, horizontal = 4.dp),
                                color = WithpeaceTheme.colors.SystemGray2,
                            )
                            Text(
                                text = post.createDate.toRelativeString(context),
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Normal,
                                color = WithpeaceTheme.colors.SystemBlack,
                                fontSize = 12.sp,
                            )
                        }
                    }
                    post.postImageUrl?.let {
                        GlideImage(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .constrainAs(image) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                },
                            imageModel = { it },
                            previewPlaceholder = R.drawable.ic_freedom,
                        )
                    }
                }
            }
        }
        item {
            if (postListPagingData.loadState.append is LoadState.Loading) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                        ),
                ) {
                    CircularProgressIndicator(
                        Modifier.align(Alignment.CenterHorizontally),
                        color = WithpeaceTheme.colors.MainPurple,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostListScreenPreview() {
    WithpeaceTheme {
        PostListScreen(
            currentTopic = PostTopicUiModel.ECONOMY,
            postListPagingData =
                flowOf(
                    PagingData.from(
                        List(10) {
                            PostUiModel(
                                postId = 6724,
                                title = "fugit",
                                content = "varius",
                                postTopic = PostTopicUiModel.ECONOMY,
                                createDate = DateUiModel(
                                    date = LocalDateTime.now(),
                                ),
                                postImageUrl = null,
                                commentCount = "0",
                            )
                        },
                        sourceLoadStates =
                            LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                    ),
                ).collectAsLazyPagingItems(),
        )
    }
}
