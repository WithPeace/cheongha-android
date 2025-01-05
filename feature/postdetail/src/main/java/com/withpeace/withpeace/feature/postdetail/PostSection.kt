package com.withpeace.withpeace.feature.postdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.comment.CommentSize
import com.withpeace.withpeace.core.ui.post.PostTopicUiModel
import com.withpeace.withpeace.core.ui.post.PostUserUiModel
import com.withpeace.withpeace.core.ui.toRelativeString
import com.withpeace.withpeace.feature.postdetail.R.string

fun LazyListScope.PostSection(
    postTopic: PostTopicUiModel,
    postUser: PostUserUiModel,
    createDate: DateUiModel,
    title: String,
    content: String,
    imageUrls: List<String>,
    commentSize: Int,
) {

    item {
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Spacer(modifier = Modifier.height(8.dp))
        PostTopic(postTopic = postTopic)
        Spacer(modifier = Modifier.height(16.dp))
        PostUserProfile(
            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            user = postUser,
            createDate = createDate,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PostTitle(
            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            title = title,
        )
        Spacer(modifier = Modifier.height(16.dp))
        PostContent(
            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            content = content,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    PostImages(
        imageUrls = imageUrls,
    )
    item {
        CommentSize(commentSize = commentSize)
        Spacer(modifier = Modifier.height(8.dp))
    }
    item {
        Box(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
                .background(WithpeaceTheme.colors.SystemGray3),
        )
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
            imageOptions = ImageOptions(contentScale = ContentScale.FillWidth),
            imageModel = { imageUrl },
            previewPlaceholder = R.drawable.ic_freedom,
            failure = {
                Text(
                    text = stringResource(string.can_not_load_image),
                    style = WithpeaceTheme.typography.caption,
                )
            },
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
                color = WithpeaceTheme.colors.MainPurple,
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
fun PostUserProfile(
    modifier: Modifier = Modifier,
    createDate: DateUiModel,
    user: PostUserUiModel,
) {
    val imageModifier = Modifier
        .size(56.dp)
        .clip(CircleShape)
    val context = LocalContext.current
    Row(modifier = modifier) {
        GlideImage(
            modifier = imageModifier,
            imageModel = { user.profileImageUrl },
            previewPlaceholder = R.drawable.ic_freedom,
            failure = {
                Image(
                    painterResource(id = R.drawable.ic_default_profile),
                    modifier = imageModifier,
                    contentDescription = stringResource(string.basic_user_profile),
                )
            },
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
