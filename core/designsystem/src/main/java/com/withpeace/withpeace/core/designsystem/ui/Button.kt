package com.withpeace.withpeace.core.designsystem.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun WithPeaceCompleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = WithpeaceTheme.colors.MainPink,
            disabledContainerColor = WithpeaceTheme.colors.SystemGray2,
        ),
        enabled = enabled,
    ) {
        Text(
            text = stringResource(R.string.complete_button_text),
            style = WithpeaceTheme.typography.body,
            color = WithpeaceTheme.colors.SystemWhite,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WithPeaceCompleteButtonPreview() {
    WithPeaceCompleteButton(onClick = {}, enabled = true)
}
