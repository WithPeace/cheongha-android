package com.withpeace.withpeace.feature.postdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.DurationFromNowUiModel
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.post.PostDetailUiModel
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.PostUserUiModel
import com.withpeace.withpeace.core.ui.post.RegisterPostUiModel
import com.withpeace.withpeace.core.ui.toRelativeString
import com.withpeace.withpeace.feature.postdetail.R.drawable
import com.withpeace.withpeace.feature.postdetail.R.string
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun PostDetailRoute(
    viewModel: PostDetailViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onClickEditButton: (RegisterPostUiModel) -> Unit,
    onAuthExpired: () -> Unit,
) {
    val postUiState = viewModel.postUiState.collectAsStateWithLifecycle().value
    PostDetailScreen(
        postUiState = postUiState,
        onClickBackButton = onClickBackButton,
        onClickDeleteButton = viewModel::deletePost,
        onClickEditButton = onClickEditButton,
        onAuthExpired = onAuthExpired,
    )

    LaunchedEffect(null) {
        viewModel.postUiEvent.collect {
            when (it) {
                PostDetailUiEvent.DeleteFailByNetworkError -> onShowSnackBar("삭제에 실패하였습니다. 네트워크를 확인해주세요")

                PostDetailUiEvent.UnAuthorzied -> onAuthExpired()

                PostDetailUiEvent.DeleteSuccess -> {
                    onShowSnackBar("게시글이 삭제되었습니다.")
                    onClickBackButton()
                }
            }
        }
    }
}

@Composable
fun PostDetailScreen(
    onClickBackButton: () -> Unit = {},
    onClickDeleteButton: () -> Unit = {},
    postUiState: PostDetailUiState,
    onClickEditButton: (RegisterPostUiModel) -> Unit = {},
    onAuthExpired: () -> Unit,
) {
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val lazyListState = rememberLazyListState()
    Column {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = onClickBackButton,
            title = {},
            actions = {
                Icon(
                    modifier = Modifier
                        .clickable {
                            showBottomSheet = true
                        }
                        .padding(21.dp)
                        .size(24.dp),
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "option",
                )
            },
        )
        when (postUiState) {
            PostDetailUiState.FailByNetwork ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(string.netwokr_error_message_text),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

            PostDetailUiState.NotFound ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(string.not_found_post),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

            PostDetailUiState.Loading ->
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = WithpeaceTheme.colors.MainPink,
                    )
                }

            is PostDetailUiState.Success -> {
                LazyColumn(state = lazyListState) {
                    item {
                        HorizontalDivider(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                            color = WithpeaceTheme.colors.SystemGray3,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PostTopic(postTopic = postUiState.postDetail.postTopic)
                        Spacer(modifier = Modifier.height(16.dp))
                        UserProfile(
                            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                            user = postUiState.postDetail.postUser,
                            createDate = postUiState.postDetail.createDate,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PostTitle(
                            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                            title = postUiState.postDetail.title,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PostContent(
                            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                            content = postUiState.postDetail.content,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    PostImages(
                        imageUrls = postUiState.postDetail.imageUrls,
                    )
                }

                if (showBottomSheet) {
                    PostDetailBottomSheet(
                        isMyPost = postUiState.postDetail.isMyPost,
                        onDismissRequest = { showBottomSheet = false },
                        onClickDeleteButton = { showDeleteDialog = true },
                        onClickEditButton = {
                            onClickEditButton(
                                RegisterPostUiModel(
                                    id = postUiState.postDetail.id,
                                    title = postUiState.postDetail.title,
                                    content = postUiState.postDetail.content,
                                    topic = postUiState.postDetail.postTopic,
                                    imageUrls = postUiState.postDetail.imageUrls,
                                ),
                            )
                        },
                    )
                }

                if (showDeleteDialog) {
                    DeletePostDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        onClickConfirmButton = onClickDeleteButton,
                    )
                }
            }

            PostDetailUiState.UnAuthorized -> onAuthExpired()
        }
    }
}

@Composable
fun DeletePostDialog(
    onDismissRequest: () -> Unit,
    onClickConfirmButton: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(
                    WithpeaceTheme.colors.SystemWhite,
                    RoundedCornerShape(10.dp),
                )
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(string.delete_post_dialog_title),
                style = WithpeaceTheme.typography.title2,
            )
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(string.delete_post_dialog_description),
                style = WithpeaceTheme.typography.body,
                textAlign = TextAlign.Center,
            )
            Row {
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 4.dp)
                        .weight(1f),
                    onClick = { onDismissRequest() },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(width = 1.dp, color = WithpeaceTheme.colors.MainPink),
                    colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.SystemWhite),
                ) {
                    Text(
                        text = stringResource(string.delete_cancel),
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.MainPink,
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 16.dp)
                        .weight(1f),
                    onClick = { onClickConfirmButton() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.MainPink),
                ) {
                    Text(
                        text = stringResource(id = string.delete_post),
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemWhite,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailBottomSheet(
    isMyPost: Boolean,
    onDismissRequest: () -> Unit,
    onClickDeleteButton: () -> Unit,
    onClickEditButton: () -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        dragHandle = {},
        containerColor = WithpeaceTheme.colors.SystemWhite,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
    ) {
        if(isMyPost){
            Column(
                modifier = Modifier.padding(
                    start = WithpeaceTheme.padding.BasicHorizontalPadding,
                    end = WithpeaceTheme.padding.BasicHorizontalPadding,
                    top = 24.dp,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onClickEditButton()
                            onDismissRequest()
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = drawable.ic_edit),
                        contentDescription = stringResource(string.edit_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(string.edit_post),
                        style = WithpeaceTheme.typography.body,
                    )
                }
                HorizontalDivider(color = WithpeaceTheme.colors.SystemGray3)
                Row(
                    modifier = Modifier
                        .clickable {
                            onClickDeleteButton()
                            onDismissRequest()
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = drawable.ic_delete),
                        contentDescription = stringResource(string.delete_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(string.delete_post),
                        style = WithpeaceTheme.typography.body,
                    )
                }
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)) {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = drawable.ic_complain),
                        contentDescription = stringResource(string.complain_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(string.complain_post),
                        style = WithpeaceTheme.typography.body,
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = drawable.ic_hide),
                        contentDescription = stringResource(string.hide_user_posts_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(string.hide_user_posts),
                        style = WithpeaceTheme.typography.body,
                    )
                }
            }
        }
    }
}

fun LazyListScope.PostImages(
    imageUrls: List<String>,
) {
    items(
        items = imageUrls,
    ) { imageUrl ->
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            imageModel = { imageUrl },
            previewPlaceholder = R.drawable.ic_freedom,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PostTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier,
        text = title,
        style = WithpeaceTheme.typography.title2,
    )
}

@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    content: String,
) {
    Text(
        modifier = modifier,
        text = content,
        style = WithpeaceTheme.typography.body,
    )
}

@Composable
fun PostTopic(
    modifier: Modifier = Modifier,
    postTopic: PostTopicUiModel,
) {
    Box(
        modifier = modifier
            .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)
            .background(
                color = WithpeaceTheme.colors.MainPink,
                shape = RoundedCornerShape(20.dp),
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = postTopic.iconResId),
                contentDescription = postTopic.name,
                tint = WithpeaceTheme.colors.SystemWhite,
            )
            Spacer(modifier = Modifier.width(11.dp))
            Text(
                text = stringResource(id = postTopic.textResId),
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemWhite,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun UserProfile(
    modifier: Modifier = Modifier,
    createDate: DateUiModel,
    user: PostUserUiModel,
) {
    val context = LocalContext.current
    Row(modifier = modifier) {
        GlideImage(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            imageModel = { user.profileImageUrl },
            previewPlaceholder = R.drawable.ic_freedom,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = user.name,
                style = WithpeaceTheme.typography.body,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = createDate.toRelativeString(context),
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray4,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900, backgroundColor = 0xFFffffff)
@Composable
private fun PostDetailScreenPreview() {
    WithpeaceTheme {
        PostDetailScreen(
            postUiState = PostDetailUiState.Success(
                PostDetailUiModel(
                    postUser = PostUserUiModel(
                        id = 4323,
                        name = "Angeline Shaw",
                        profileImageUrl = "https://www.google.com/#q=ignota",
                    ),
                    id = 1529,
                    title = "일찌기 나는 아무것도 아니었다.",
                    content = "돌아가는 팽이를 보고 싶어서, 그 팽이가 온전히 내 팽이이고 싶어서, 내 속도를 그대로 빼닮은 팽이의 회전을 여유롭게 관찰하고 싶어서, 그러니까 문방구에서 막상 팽이를 사오긴 했는데 요즘 누가 팽이 돌리나 눈치 보다 땅에다가는 못 풀고 눈으로 푸는 마음, 그 눈에서 돌아가는 팽이의 마음, 그거 같다",
                    postTopic = PostTopicUiModel.FREEDOM,
                    imageUrls = listOf("", "", ""),
                    createDate = DateUiModel(
                        LocalDateTime.now(),
                        DurationFromNowUiModel.LessThanOneMinute
                    ),
                    isMyPost = false,
                ),
            ),
            onAuthExpired = {}
        )
    }
}
