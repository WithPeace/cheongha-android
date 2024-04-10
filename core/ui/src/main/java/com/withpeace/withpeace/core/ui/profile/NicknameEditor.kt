package com.withpeace.withpeace.core.ui.profile

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NickNameEditor(
    modifier: Modifier = Modifier,
    nickname: String,
    isChanged: Boolean,
    nicknameValidStatus: ProfileNicknameValidUiState,
    onNickNameChanged: (String) -> Unit,
    onKeyBoardTimerEnd: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(nickname) {
        delay(1.seconds)
        onKeyBoardTimerEnd()
    }

    Column(
        modifier = modifier
            .width(140.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BasicTextField(
            value = nickname,
            onValueChange = {
                onNickNameChanged(it)
            },
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            textStyle = WithpeaceTheme.typography.body.copy(textAlign = TextAlign.Center),
            singleLine = true,
            maxLines = 1,
        ) {
            TextFieldDefaults.DecorationBox(
                value = nickname,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.enter_nickname),
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
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
        // isChanged: 닉네임이 변경되지 않은 경우, ProfileNicknameValidUiState.Valid: 닉네임이 검증 된 경우
        HorizontalDivider(
            modifier = modifier
                .width(140.dp)
                .height(1.dp),
            color = if (nicknameValidStatus is ProfileNicknameValidUiState.Changing || nicknameValidStatus is ProfileNicknameValidUiState.Valid || isChanged.not()) WithpeaceTheme.colors.SystemBlack
            else WithpeaceTheme.colors.SystemError,
        )
    }
    if (!(nicknameValidStatus is ProfileNicknameValidUiState.Changing || nicknameValidStatus is ProfileNicknameValidUiState.Valid || isChanged.not())) {
        Text(
            text = if (nicknameValidStatus is ProfileNicknameValidUiState.InValidDuplicated) stringResource(
                R.string.nickname_duplicated,
            ) else stringResource(id = R.string.nickname_policy),
            style = WithpeaceTheme.typography.caption,
            color = WithpeaceTheme.colors.SystemError,
            modifier = modifier.padding(top = 4.dp),
        )
    }
}