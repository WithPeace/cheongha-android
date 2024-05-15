package com.withpeace.withpeace.core.designsystem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun NoTitleDialog(
    modifier: Modifier = Modifier,
    onClickPositive: () -> Unit,
    onClickNegative: () -> Unit,
    contentText: String,
    positiveText: String,
    negativeText: String,
) {
    Surface(
        modifier = modifier
            .width(327.dp)
            .clip(RoundedCornerShape(10.dp)),
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = modifier.height(24.dp))
            Text(
                text = contentText,
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemGray1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = modifier.height(16.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    modifier = modifier
                        .width(136.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp),
                            color = WithpeaceTheme.colors.MainPurple,
                        )
                        .background(WithpeaceTheme.colors.SystemWhite),
                    onClick = { onClickNegative() },
                    content = {
                        Text(
                            text = negativeText,
                            color = WithpeaceTheme.colors.MainPurple,
                            style = WithpeaceTheme.typography.caption,
                        )
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    modifier = modifier
                        .width(136.dp)
                        .background(
                            WithpeaceTheme.colors.MainPurple,
                            shape = RoundedCornerShape(10.dp),
                        ),
                    onClick = { onClickPositive() },
                    content = {
                        Text(
                            text = positiveText,
                            color = WithpeaceTheme.colors.SystemWhite,
                            style = WithpeaceTheme.typography.caption,
                        )
                    },
                )
            }
            Spacer(modifier = modifier.height(24.dp))
        }
    }
}