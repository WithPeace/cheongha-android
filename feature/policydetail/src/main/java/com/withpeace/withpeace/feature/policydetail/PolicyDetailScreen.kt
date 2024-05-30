package com.withpeace.withpeace.feature.policydetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar

@Composable
fun PolicyDetailRoute(
    onShowSnackBar: (message: String) -> Unit,
    viewModel: PolicyDetailViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
) {
    PolicyDetailScreen(
        onClickBackButton = onClickBackButton,
    )

}

@Composable
fun PolicyDetailScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
) {
    //TODO 화면 로깅
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = onClickBackButton,
            title = {
                //TODO(스크롤에 따라 투명도 조절)
                Text(text = "근로자 휴가비 지원사업", style = WithpeaceTheme.typography.title1)
            },
        )
        HorizontalDivider(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Column(
            modifier = modifier
                .fillMaxHeight()
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = modifier.height(24.dp))
            // TODO(Tobbar 스크롤 체크)
            Text(
                text = "근로자 휴가비 지원사업",
                style = WithpeaceTheme.typography.title1,
                color = WithpeaceTheme.colors.SystemBlack,
            )
        }
    }
}

//TODO caption 행간 확인