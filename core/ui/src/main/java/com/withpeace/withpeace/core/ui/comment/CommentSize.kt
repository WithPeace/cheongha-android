package com.withpeace.withpeace.core.ui.comment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R

@Composable
fun CommentSize(
    commentSize: Int,
) {
    Row(
        modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chat),
            contentDescription = "댓글 개수",
            modifier = Modifier.padding(end = 4.dp),
        )
        Text(
            text = "$commentSize",
            style = WithpeaceTheme.typography.caption,
        )
    }
}