package com.withpeace.withpeace.feature.disablepolicy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar

@Composable
fun DisabledPolicyRoute(
    onShowSnackBar: (message: String) -> Unit = {},
    viewModel: DisablePolicyViewModel = hiltViewModel(),
    onBookmarkDeleteSuccess: (policyId: String) -> Unit = {},
    onClickBackButton: () -> Unit = {},
) {

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect {
            when (it) {
                DisabledPolicyUiEvent.UnBookmarkFailure -> onShowSnackBar("찜하기 해제 실패하였습니다.")
                is DisabledPolicyUiEvent.UnBookmarkSuccess -> onBookmarkDeleteSuccess(it.policyId)
            }
        }
    }

    DisabledPolicyScreen(
        onClickBackButton = onClickBackButton,
        onBookmarkDelete = viewModel::unBookmark,
    )
}

@Composable
fun DisabledPolicyScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit = {},
    onBookmarkDelete: () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = {
                onClickBackButton()
            },
            title = { },
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FB)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = modifier.height(183.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_illust_delete),
                contentDescription = "삭제",
            )
            Spacer(modifier = modifier.height(24.dp))
            Text(
                text = "해당 정책은 삭제된 정책입니다.",
                color = WithpeaceTheme.colors.SnackbarBlack,
                style = WithpeaceTheme.typography.disablePolicyTitle,
            )
            Spacer(modifier = modifier.height(16.dp))
            TextButton(
                onClick = {
                    onBookmarkDelete()
                },
                modifier
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = WithpeaceTheme.colors.MainPurple,
                        ),
                        shape = RoundedCornerShape(50.dp),
                    ),
                contentPadding = PaddingValues(horizontal = 40.dp),
            ) {
                Text(
                    text = "관심목록 해제하기",
                    color = WithpeaceTheme.colors.MainPurple,
                    style = WithpeaceTheme.typography.caption.merge(
                        TextStyle(
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None,
                            ),
                        ),
                    ),
                )
            }
        }
    }
}

@Composable
@Preview
fun DisablePolicyPreview() {
    DisabledPolicyScreen()
}