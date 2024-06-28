package com.withpeace.withpeace.feature.policydetail.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
internal fun HyperLinkDescriptionTitleAndContent(
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
    if (content.isBlank() || content == "null" || content == "-") {
        Text(
            text = "-",
            style = WithpeaceTheme.typography.policyDetailCaption,
            color = WithpeaceTheme.colors.SystemBlack,
        )
    } else {
        AnnotatedClickableText(link = content)
    }
    Spacer(modifier = modifier.height(16.dp))
}

@Composable
fun AnnotatedClickableText(link: String) {
    val annotatedText = buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL", annotation = link,
        )
        withStyle(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = WithpeaceTheme.colors.SystemHyperLink, fontWeight = FontWeight.Normal,
            ),
        ) {
            append(link)
        }

        pop()
    }
    val context = LocalContext.current
    ClickableText(
        style = WithpeaceTheme.typography.policyDetailCaption,
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "URL", start = offset, end = offset,
            ).firstOrNull()?.let { _ ->
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(link)
                context.startActivity(openURL)
            }
        },
    )
}