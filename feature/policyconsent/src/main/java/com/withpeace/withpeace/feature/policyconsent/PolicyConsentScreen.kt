package com.withpeace.withpeace.feature.policyconsent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.CheonghaCheckbox
import com.withpeace.withpeace.feature.policyconsent.uistate.PolicyConsentUiEvent
import com.withpeace.withpeace.feature.policyconsent.uistate.PolicyConsentUiState

@Composable
fun PolicyConsentRoute(
    onShowSnackBar: (String) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onSuccessToNext: () -> Unit,
    viewModel: PolicyConsentViewModel = hiltViewModel(),
) {
    val checkStatus = viewModel.uiState.collectAsStateWithLifecycle()
    PolicyConsentScreen(
        checkStatus = checkStatus.value,
        onPrivacyPolicyChecked = viewModel::onPrivacyPolicyChecked,
        onTermsOfServiceChecked = viewModel::onTermsOfServiceChecked,
        onAllChecked = viewModel::onAllChecked,
        onClickToNext = viewModel::checkToNext,
    )

    LaunchedEffect(key1 = viewModel.policyConsentEvent) {
        viewModel.policyConsentEvent.collect {
            when (it) {
                PolicyConsentUiEvent.SuccessToNext -> onSuccessToNext()
                PolicyConsentUiEvent.FailureToNext -> onShowSnackBar("필수 항목을 선택해주세요")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyConsentScreen(
    modifier: Modifier = Modifier,
    checkStatus: PolicyConsentUiState,
    onPrivacyPolicyChecked: (Boolean) -> Unit,
    onTermsOfServiceChecked: (Boolean) -> Unit,
    onAllChecked: (Boolean) -> Unit,
    onClickToNext: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = modifier.height(56.dp))
            Image(
                modifier = modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = com.withpeace.withpeace.core.ui.R.drawable.ic_app_logo),
                contentDescription = stringResource(id = com.withpeace.withpeace.core.ui.R.string.cheongha_logo),
            )
            Spacer(modifier = modifier.height(24.dp))
            Text(
                modifier = modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                text = "청하(청춘하랑)에 어서오세요!\n" +
                    "약관에 동의하시면 청하와의 여정을\n" +
                    "시작할 수 있어요!",
                style = WithpeaceTheme.typography.title2.merge(
                    TextStyle(
                        lineHeight = 28.sp,
                    ),
                ),
            )
            Spacer(modifier = modifier.height(32.dp))
            Row(
                modifier = modifier.toggleable(
                    value = checkStatus.allChecked,
                    role = Role.Checkbox,
                    onValueChange = { onAllChecked(it) },
                ),
            ) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    CheonghaCheckbox(
                        modifier = modifier.align(Alignment.CenterVertically),
                        checked = checkStatus.allChecked,
                        onCheckedChanged = {
                            onAllChecked(it)
                        },
                    )
                }
                Text(
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp),
                    text = "모두 동의",
                    style = if (checkStatus.allChecked) WithpeaceTheme.typography.semiBold16Sp else WithpeaceTheme.typography.body,
                    color = WithpeaceTheme.colors.SystemBlack,
                )
            }
            Spacer(modifier = modifier.height(24.dp))
            HorizontalDivider(color = WithpeaceTheme.colors.SystemGray3)
            Spacer(modifier = modifier.height(24.dp))
            Row {
                Row(
                    modifier = modifier.toggleable(
                        value = checkStatus.termsOfServiceChecked,
                        role = Role.Checkbox,
                        onValueChange = {
                            onTermsOfServiceChecked(it)
                        },
                    ),
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "서비스 이용약관 체크",
                        modifier = modifier
                            .padding(2.dp)
                            .size(18.dp),
                        tint = if (checkStatus.termsOfServiceChecked) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2,
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        text = "[필수] 서비스 이용약관",
                        style = if (checkStatus.termsOfServiceChecked) WithpeaceTheme.typography.semiBold16Sp else WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                }
                Text(
                    text = "보기",
                    modifier = modifier.padding(start = 8.dp).align(Alignment.CenterVertically),
                    textDecoration = TextDecoration.Underline,
                    style = WithpeaceTheme.typography.caption,
                    color = WithpeaceTheme.colors.SystemGray2,
                )
            }

            Spacer(modifier = modifier.height(16.dp))
            Row {
                Row(
                    modifier = modifier.toggleable(
                        value = checkStatus.privacyPolicyChecked,
                        role = Role.Checkbox,
                        onValueChange = {
                            onPrivacyPolicyChecked(it)
                        },
                    ),
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "개인정보 처리 방침 체크",
                        modifier = modifier
                            .padding(2.dp)
                            .size(18.dp),
                        tint = if (checkStatus.privacyPolicyChecked) WithpeaceTheme.colors.MainPurple else WithpeaceTheme.colors.SystemGray2,
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        text = "[필수] 개인정보처리방침",
                        style = if (checkStatus.privacyPolicyChecked) WithpeaceTheme.typography.semiBold16Sp else WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemBlack,
                    )
                }
                Text(
                    text = "보기",
                    modifier = modifier.padding(start = 8.dp).align(Alignment.CenterVertically),
                    textDecoration = TextDecoration.Underline,
                    style = WithpeaceTheme.typography.caption,
                    color = WithpeaceTheme.colors.SystemGray2,
                )
            }
        }
        NextButton(onClick = onClickToNext)
    }
}

@Composable
private fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(
                bottom = 40.dp,
                end = WithpeaceTheme.padding.BasicHorizontalPadding,
                start = WithpeaceTheme.padding.BasicHorizontalPadding,
            )
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.MainPurple),
        shape = RoundedCornerShape(9.dp),
    ) {
        Text(
            style = WithpeaceTheme.typography.body,
            text = stringResource(R.string.to_next),
            modifier = Modifier.padding(vertical = 18.dp),
            color = WithpeaceTheme.colors.SystemWhite,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PolicyConsentPreview() {
    WithpeaceTheme {
        PolicyConsentScreen(
            checkStatus = PolicyConsentUiState(
                allChecked = false,
                termsOfServiceChecked = false,
                privacyPolicyChecked = false,
            ),
            onPrivacyPolicyChecked = {},
            onTermsOfServiceChecked = {},
            onAllChecked = {},
            onClickToNext = {},
        )
    }
}
