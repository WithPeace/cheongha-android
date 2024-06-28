package com.withpeace.withpeace.core.designsystem.ui

import androidx.compose.foundation.background
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun CheonghaCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    Checkbox(
        modifier = modifier,
        colors = CheckboxDefaults.colors(
            checkedColor = WithpeaceTheme.colors.MainPurple,
            uncheckedColor = WithpeaceTheme.colors.SystemGray2,
            checkmarkColor = WithpeaceTheme.colors.SystemWhite,
        ),
        checked = checked,
        onCheckedChange = onCheckedChanged,
    )
}

@Composable
fun TransparentCheonghaCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    Checkbox(
        modifier = modifier,
        colors = CheckboxDefaults.colors(
            checkedColor = Color.Transparent,
            uncheckedColor = Color.Transparent,
        ).copy(
            uncheckedCheckmarkColor = WithpeaceTheme.colors.SystemGray2,
            checkedCheckmarkColor = WithpeaceTheme.colors.MainPurple,
            uncheckedBoxColor = Color.Unspecified,
        ),
        checked = checked,
        onCheckedChange = onCheckedChanged,
    )
}

@Composable
@Preview
fun TransparentCheonghaCheckboxPreview() {
    WithpeaceTheme {
        TransparentCheonghaCheckbox(
            modifier = Modifier.background(color = WithpeaceTheme.colors.SystemWhite),
            checked = false,
            onCheckedChanged = {},
        )
    }
}