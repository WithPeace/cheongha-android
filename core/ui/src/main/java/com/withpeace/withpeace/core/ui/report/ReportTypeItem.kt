package com.withpeace.withpeace.core.ui.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.post.ReportTypeUiModel

@Composable
fun ReportTypeItem(
    modifier: Modifier = Modifier,
    isPostReport: Boolean,
    id: Long,
    reportTypeUiModel: ReportTypeUiModel,
    onClickReportType: (id: Long, ReportTypeUiModel) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var showReportDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val title = if (isPostReport) reportTypeUiModel.postTitle else reportTypeUiModel.commentTitle
    Column(
        modifier = modifier.clickable {
            showReportDialog = true
        }.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
    ) {
        Text(
            text = title,
            style = WithpeaceTheme.typography.body,
            modifier = Modifier.padding(start = 4.dp, top = 16.dp, bottom = 16.dp),
        )
        HorizontalDivider()
    }
    if (showReportDialog) {
        ReportDialog(
            title = title,
            onClickReportButton = {
                onClickReportType(id, reportTypeUiModel)
                onDismissRequest()
            },
            onDismissRequest = { showReportDialog = false },
        )
    }
}