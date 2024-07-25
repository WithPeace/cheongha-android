package com.withpeace.withpeace.core.designsystem.ui.snackbar

import androidx.compose.foundation.background
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
fun NavigatorSnackbar(data: SnackbarState) {
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
            text = data.message,
            color = WithpeaceTheme.colors.SystemWhite,
        )
        Text(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            style = WithpeaceTheme.typography.caption,
            text = (data.snackbarType as SnackbarType.Navigator).actionName,
            color = WithpeaceTheme.colors.SubSkyBlue,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
@Preview
fun NavigationSnackbarPreview() {
    WithpeaceTheme {
        NavigatorSnackbar(data = SnackbarState("장충동 왕족발 보쌈", SnackbarType.Navigator("먹기",{})))
    }
}
