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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.KeyboardAware
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.post.CommentUiModel
import com.withpeace.withpeace.core.ui.post.CommentUserUiModel
import com.withpeace.withpeace.core.ui.post.PostDetailUiModel
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.PostUserUiModel
import com.withpeace.withpeace.core.ui.post.RegisterPostUiModel
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel
import com.withpeace.withpeace.feature.postdetail.R.drawable
import com.withpeace.withpeace.feature.postdetail.R.string
import java.time.LocalDateTime

@Composable
fun PostDetailRoute(
    viewModel: PostDetailViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onClickEditButton: (RegisterPostUiModel) -> Unit,
    onAuthExpired: () -> Unit,
) {
    val postUiState by viewModel.postUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val commentText by viewModel.commentText.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    KeyboardAware {
        PostDetailScreen(
            postUiState = postUiState,
            commentText = commentText,
            onClickRegisterCommentButton = viewModel::registerComment,
            onCommentTextChanged = viewModel::onCommentTextChanged,
            onClickBackButton = onClickBackButton,
            onClickDeleteButton = viewModel::deletePost,
            onClickEditButton = onClickEditButton,
            isLoading = isLoading,
            lazyListState = lazyListState,
            onReportComment = { _, _ -> },
            onReportPost = viewModel::reportPost,
        )
    }

    LaunchedEffect(null) {
        viewModel.postUiEvent.collect {
            when (it) {
                PostDetailUiEvent.DeleteFailByNetworkError -> onShowSnackBar("삭제에 실패하였습니다. 네트워크를 확인해주세요")

                PostDetailUiEvent.UnAuthorized -> onAuthExpired()

                PostDetailUiEvent.DeleteSuccess -> {
                    onShowSnackBar("게시글이 삭제되었습니다.")
                    onClickBackButton()
                }

                PostDetailUiEvent.RegisterCommentFailByNetwork -> onShowSnackBar("댓글 등록에 실패했습니다. 네트워크를 확인해주세요")
                PostDetailUiEvent.RegisterCommentSuccess -> {
                    lazyListState.fullAnimatedScroll()
                }

                PostDetailUiEvent.ReportCommentFail -> onShowSnackBar("신고에 실패하였습니다")
                PostDetailUiEvent.ReportCommentSuccess -> onShowSnackBar("신고 되었습니다")
                PostDetailUiEvent.ReportPostFail -> onShowSnackBar("신고에 실패하였습니다")
                PostDetailUiEvent.ReportPostSuccess -> onShowSnackBar("신고 되었습니다")
                PostDetailUiEvent.ReportCommentDuplicated -> onShowSnackBar("이미 신고한 댓글입니다")
                PostDetailUiEvent.ReportPostDuplicated -> onShowSnackBar("이미 신고한 게시글입니다")
            }
        }
    }
}

private suspend fun LazyListState.fullAnimatedScroll() {
    val maxIndex = Integer.MAX_VALUE
    val maxOffset = Integer.MAX_VALUE
    animateScrollToItem(maxIndex, maxOffset)
}

@Composable
fun PostDetailScreen(
    onClickBackButton: () -> Unit = {},
    onClickDeleteButton: () -> Unit = {},
    postUiState: PostDetailUiState,
    isLoading: Boolean = false,
    onClickEditButton: (RegisterPostUiModel) -> Unit = {},
    commentText: String = "",
    onClickRegisterCommentButton: () -> Unit = {},
    onCommentTextChanged: (String) -> Unit = {},
    lazyListState: LazyListState,
    onReportPost: (id: Long, ReportTypeUiModel) -> Unit = { _, _ -> },
    onReportComment: (id: Long, ReportTypeUiModel) -> Unit = { _, _ -> },
) {
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        containerColor = WithpeaceTheme.colors.SystemWhite,
        topBar = {
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
        },
        bottomBar = {
            RegisterCommentSection(
                onClickRegisterButton = onClickRegisterCommentButton,
                onTextChanged = onCommentTextChanged,
                text = commentText,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = WithpeaceTheme.colors.MainPink,
                )
            }
            when (postUiState) {
                PostDetailUiState.Init -> Unit
                PostDetailUiState.FailByNetwork -> {
                    Text(
                        text = stringResource(string.netwokr_error_message_text),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                PostDetailUiState.NotFound -> {
                    Text(
                        text = stringResource(string.not_found_post),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is PostDetailUiState.Success -> {

                    LazyColumn(state = lazyListState) {
                        PostSection(
                            postTopic = postUiState.postDetail.postTopic,
                            postUser = postUiState.postDetail.postUser,
                            createDate = postUiState.postDetail.createDate,
                            title = postUiState.postDetail.title,
                            content = postUiState.postDetail.content,
                            imageUrls = postUiState.postDetail.imageUrls,
                            commentSize = postUiState.postDetail.comments.size,
                        )
                        CommentSection(postUiState.postDetail.comments)
                    }

                    if (showBottomSheet) {
                        PostDetailPostBottomSheet(
                            isMyPost = postUiState.postDetail.isMyPost,
                            postId = postUiState.postDetail.id,
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
                            onReportPost = onReportPost,
                        )
                    }

                    if (showDeleteDialog) {
                        DeletePostDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            onClickConfirmButton = onClickDeleteButton,
                        )
                    }
                }
            }
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
fun PostDetailPostBottomSheet(
    isMyPost: Boolean,
    postId: Long,
    onDismissRequest: () -> Unit,
    onClickDeleteButton: () -> Unit,
    onClickEditButton: () -> Unit,
    onReportPost: (id: Long, ReportTypeUiModel) -> Unit,
) {
    var showReportBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
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
                            showReportBottomSheet = true
                        }
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
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
        if (showReportBottomSheet) {
            PostDetailReportBottomSheet(
                isPostReport = true,
                id = postId,
                onDismissRequest = {
                    showReportBottomSheet = false
                    onDismissRequest()
                },
                onClickReportType = onReportPost,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailReportBottomSheet(
    isPostReport: Boolean,
    id: Long,
    onDismissRequest: () -> Unit,
    onClickReportType: (id: Long, ReportTypeUiModel) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        dragHandle = {},
        containerColor = WithpeaceTheme.colors.SystemWhite,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            WithPeaceBackButtonTopAppBar(
                onClickBackButton = onDismissRequest,
                title = {
                    Text(text = "신고하는 이유를 선택해주세요", style = WithpeaceTheme.typography.title1)
                },
            )
            HorizontalDivider(Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            ) {
                ReportTypeUiModel.entries.forEach { reportTypeUiModel ->
                    ReportTypeItem(
                        isPostReport = isPostReport,
                        id = id,
                        reportTypeUiModel = reportTypeUiModel,
                        onClickReportType = onClickReportType,
                        onDismissRequest = onDismissRequest,
                    )
                }
            }
        }
    }
}

@Composable
fun ReportTypeItem(
    modifier: Modifier = Modifier,
    isPostReport: Boolean,
    id: Long,
    reportTypeUiModel: ReportTypeUiModel,
    onClickReportType: (id: Long, ReportTypeUiModel) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var showReportDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val title = if (isPostReport) reportTypeUiModel.postTitle else reportTypeUiModel.commentTitle
    Column(
        modifier = modifier.clickable {
            showReportDialog = true
        },
    ) {
        Text(
            text = title,
            style = WithpeaceTheme.typography.body,
            modifier = Modifier.padding(start = 4.dp, top = 16.dp, bottom = 16.dp),
        )
        HorizontalDivider()
    }
    if (showReportDialog) {
        ReportDialog(
            title = title,
            onClickReportButton = {
                onClickReportType(id, reportTypeUiModel)
                onDismissRequest()
            },
            onDismissRequest = { showReportDialog = false },
        )
    }
}

@Composable
fun ReportDialog(
    title: String,
    onClickReportButton: () -> Unit,
    onDismissRequest: () -> Unit,
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
                text = title,
                style = WithpeaceTheme.typography.title2,
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
                    onClick = {
                        onClickReportButton()
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.MainPink),
                ) {
                    Text(
                        text = "신고하기",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemWhite,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                    imageUrls = listOf(),
                    createDate = DateUiModel(
                        LocalDateTime.now(),
                    ),
                    isMyPost = false,
                    comments = List(10) {
                        CommentUiModel(
                            id = it.toLong(),
                            content = "natum",
                            createDate = DateUiModel(date = LocalDateTime.now()),
                            commentUser = CommentUserUiModel(
                                id = 9807,
                                nickname = "Becky Lowery",
                                profileImageUrl = "https://www.google.com/#q=repudiare",
                            ),
                        )
                    },
                ),
            ),
            lazyListState = rememberLazyListState(),
        )
    }
}
