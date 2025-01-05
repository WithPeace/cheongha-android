package com.withpeace.withpeace.core.ui.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R

@Composable
fun RegisterCommentSection(
    modifier: Modifier = Modifier,
    onClickRegisterButton: () -> Unit = {},
    onTextChanged: (String) -> Unit = {},
    text: String = "",
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column {
        HorizontalDivider(color = WithpeaceTheme.colors.SystemGray3)
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
                    vertical = 8.dp,
                ),
            shape = RoundedCornerShape(21.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
                        vertical = 8.dp,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CommentTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .weight(1f),
                    onTextChanged = onTextChanged,
                    text = text,
                )
                Icon(
                    modifier = Modifier.clickable {
                        onClickRegisterButton()
                        keyboardController?.hide()
                    },
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = stringResource(R.string.comment_register_icon_description),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit = {},
    text: String = "",
) {
    BasicTextField(
        value = text,
        onValueChange = {
            onTextChanged(it)
        },
        modifier = modifier,
        enabled = true,
        textStyle = WithpeaceTheme.typography.caption,
    ) {
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = it,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            placeholder = {
                Text(
                    text = stringResource(R.string.please_type_comment),
                    style = WithpeaceTheme.typography.caption,
                )
            },
            interactionSource = remember { MutableInteractionSource() },
            contentPadding = PaddingValues(0.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
        )
    }
}
