package com.withpeace.withpeace.feature.policydetail.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
internal fun DescriptionTitleAndContent(
    modifier: Modifier,
    title: String,
    content: String,
) {
    Text(
        text = title,
        style = WithpeaceTheme.typography.bold16Sp,
        color = WithpeaceTheme.colors.SystemBlack,
    )

    Spacer(modifier = modifier.height(8.dp))
    if (content.isBlank() || content == "null") {
        Text(
            text = "-",
            style = WithpeaceTheme.typography.policyDetailCaption,
            color = WithpeaceTheme.colors.SystemBlack,
        )
    } else {
        SelectionContainer {
            Text(
                text = content,
                style = WithpeaceTheme.typography.policyDetailCaption,
                color = WithpeaceTheme.colors.SystemBlack,
            )
        }
    }
    Spacer(modifier = modifier.height(16.dp))
}