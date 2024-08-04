package com.withpeace.withpeace.core.designsystem.ui.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun NavigatorSnackbar(content: String, action: () -> Unit, actionName: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
            .imePadding()
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(WithpeaceTheme.colors.SnackbarBlack),
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            style = WithpeaceTheme.typography.caption,
            text =content ,
            color = WithpeaceTheme.colors.SystemWhite,
        )
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp).clickable { action() },
            style = WithpeaceTheme.typography.caption,
            text = actionName,
            color = WithpeaceTheme.colors.SubSkyBlue,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
@Preview
fun NavigationSnackbarPreview() {
    WithpeaceTheme {
        NavigatorSnackbar(content = "장충동 왕족발 보쌈", action = {}, actionName = "먹으러 가기")
    }
}
