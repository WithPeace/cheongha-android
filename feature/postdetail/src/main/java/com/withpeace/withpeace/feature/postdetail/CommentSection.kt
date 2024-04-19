package com.withpeace.withpeace.feature.postdetail

import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.withpeace.withpeace.core.ui.R.*
import com.withpeace.withpeace.core.ui.post.CommentUiModel
import com.withpeace.withpeace.core.ui.post.CommentUserUiModel
import com.withpeace.withpeace.core.ui.toRelativeString
import java.time.LocalDateTime

fun LazyListScope.CommentSection(
    comments: List<CommentUiModel>,
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
            CommentItem(comment = comment)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CommentItem(
    comment: CommentUiModel,
) {
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
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = stringResource(
                    R.string.comment_menu_icon_description,
                ),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = comment.content,
            style = WithpeaceTheme.typography.caption,
        )
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
                    )
                },
            )
        }
    }
}
