package com.withpeace.withpeace.core.ui.report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.ui.R

@Composable
fun ReportDialog(
    title: String,
    onClickReportButton: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(
                    WithpeaceTheme.colors.SystemWhite,
                    RoundedCornerShape(10.dp),
                )
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = title,
                style = WithpeaceTheme.typography.title2,
            )
            Row {
                Button(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 4.dp)
                        .weight(1f),
                    onClick = { onDismissRequest() },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(width = 1.dp, color = WithpeaceTheme.colors.MainPurple),
                    colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.SystemWhite),
                ) {
                    Text(
                        text = stringResource(R.string.delete_cancel),
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.MainPurple,
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(start = 4.dp, end = 16.dp)
                        .weight(1f),
                    onClick = {
                        onClickReportButton()
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.MainPurple),
                ) {
                    Text(
                        text = "신고하기",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemWhite,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}