package com.withpeace.withpeace.core.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailReportBottomSheet(
    isPostReport: Boolean,
    id: Long,
    onDismissRequest: () -> Unit,
    onClickReportType: (id: Long, ReportTypeUiModel) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        containerColor = WithpeaceTheme.colors.SystemWhite,
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            WithPeaceBackButtonTopAppBar(
                onClickBackButton = onDismissRequest,
                title = {
                    Text(text = "신고하는 이유를 선택해주세요", style = WithpeaceTheme.typography.title1)
                },
            )
            HorizontalDivider(Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                ReportTypeUiModel.entries.forEach { reportTypeUiModel ->
                    ReportTypeItem(
                        isPostReport = isPostReport,
                        id = id,
                        reportTypeUiModel = reportTypeUiModel,
                        onClickReportType = onClickReportType,
                        onDismissRequest = onDismissRequest,
                    )
                }
            }
        }
    }
}