package com.withpeace.withpeace.feature.postdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.PretendardFont
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.R.drawable
import com.withpeace.withpeace.core.ui.post.CommentUiModel
import com.withpeace.withpeace.core.ui.post.CommentUserUiModel
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel
import com.withpeace.withpeace.core.ui.toRelativeString
import java.time.LocalDateTime

fun LazyListScope.CommentSection(
    comments: List<CommentUiModel>,
    onReportComment: (id: Long, ReportTypeUiModel) -> Unit,
) {
    itemsIndexed(
        items = comments,
        key = { index, item -> item.id },
    ) { index, comment ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
        ) {
            if (index != 0) {
                HorizontalDivider(color = WithpeaceTheme.colors.SystemGray3)
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            CommentItem(comment = comment, onReportComment = onReportComment)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CommentItem(
    comment: CommentUiModel,
    onReportComment: (id: Long, ReportTypeUiModel) -> Unit,
) {
    var showCommentBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val imageModifier = Modifier
        .clip(CircleShape)
        .size(40.dp)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(modifier = Modifier.weight(1f)) {
                GlideImage(
                    modifier = imageModifier,
                    imageModel = { comment.commentUser.profileImageUrl },
                    previewPlaceholder = R.drawable.ic_chat,
                    failure = {
                        Image(
                            painterResource(id = drawable.ic_default_profile),
                            modifier = imageModifier,
                            contentDescription = stringResource(R.string.basic_user_profile),
                        )
                    },
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = comment.commentUser.nickname,
                        style = WithpeaceTheme.typography.body,
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = comment.createDate.toRelativeString(context),
                        fontSize = 12.sp,
                        fontFamily = PretendardFont,
                        fontWeight = FontWeight.Normal,
                        color = WithpeaceTheme.colors.SystemGray4,
                    )
                }
            }
            if (!comment.isMyComment) {
                Icon(
                    modifier = Modifier.clickable {
                        showCommentBottomSheet = true
                    },
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = stringResource(
                        R.string.comment_menu_icon_description,
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = comment.content,
            style = WithpeaceTheme.typography.caption,
        )
        if (showCommentBottomSheet) {
            CommentBottomSheet(
                isMyComment = comment.isMyComment,
                commentId = comment.id,
                onDismissRequest = { showCommentBottomSheet = false },
                onClickDeleteButton = { },
                onReportComment = onReportComment,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    isMyComment: Boolean,
    commentId: Long,
    onDismissRequest: () -> Unit,
    onClickDeleteButton: () -> Unit,
    onReportComment: (id: Long, ReportTypeUiModel) -> Unit,
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
        if (isMyComment) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onClickDeleteButton()
                            onDismissRequest()
                        }
                        .padding(
                            start = WithpeaceTheme.padding.BasicHorizontalPadding,
                            end = WithpeaceTheme.padding.BasicHorizontalPadding,
                        )
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.delete_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.delete_post),
                        style = WithpeaceTheme.typography.body,
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showReportBottomSheet = true
                        }
                        .padding(
                            start = WithpeaceTheme.padding.BasicHorizontalPadding,
                            end = WithpeaceTheme.padding.BasicHorizontalPadding,
                        )
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_complain),
                        contentDescription = stringResource(R.string.complain_icon_content_description),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.complain_post),
                        style = WithpeaceTheme.typography.body,
                    )
                }
                HorizontalDivider()
            }
        }
        if (showReportBottomSheet) {
            PostDetailReportBottomSheet(
                isPostReport = false,
                id = commentId,
                onDismissRequest = {
                    showReportBottomSheet = false
                    onDismissRequest()
                },
                onClickReportType = onReportComment,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentSectionPreview() {
    WithpeaceTheme {
        LazyColumn {
            CommentSection(
                comments = List(10) {
                    CommentUiModel(
                        it.toLong(),
                        "안녕하세요",
                        DateUiModel(LocalDateTime.now()),
                        CommentUserUiModel(it.toLong(), "우석", ""),
                        false,
                    )
                },
                onReportComment = { _, _ -> },
            )
        }
    }
}
