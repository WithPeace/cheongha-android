package com.withpeace.withpeace.feature.policydetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.analytics.TrackPolicyDetailScreenViewEvent
import com.withpeace.withpeace.feature.policydetail.component.DescriptionTitleAndContent
import com.withpeace.withpeace.feature.policydetail.component.HyperLinkDescriptionTitleAndContent
import com.withpeace.withpeace.feature.policydetail.uistate.YouthPolicyDetailUiModel
import com.withpeace.withpeace.feature.policydetail.uistate.YouthPolicyDetailUiState
import eu.wewox.textflow.TextFlow
import eu.wewox.textflow.TextFlowObstacleAlignment

@Composable
fun PolicyDetailRoute(
    onShowSnackBar: (message: String) -> Unit,
    viewModel: PolicyDetailViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
) {
    val policyDetailUiState = viewModel.policyDetailUiState.collectAsStateWithLifecycle()
    PolicyDetailScreen(
        onClickBackButton = onClickBackButton,
        policyUiState = policyDetailUiState.value,
    )
}

@Composable
fun PolicyDetailScreen(
    policyUiState: YouthPolicyDetailUiState,
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
) {
    when (policyUiState) {
        is YouthPolicyDetailUiState.Success -> {
            PolicyDetailContent(modifier, onClickBackButton, policyUiState.youthPolicyDetail)
        }

        YouthPolicyDetailUiState.Failure -> {}
        YouthPolicyDetailUiState.Loading -> {}
    }
}

@Composable
private fun PolicyDetailContent(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    policy: YouthPolicyDetailUiModel,
) {
    val scrollState = rememberScrollState()
    val position = remember {
        mutableIntStateOf(0)
    }
    val visibility = remember {
        derivedStateOf {
            scrollState.value >= position.intValue
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = onClickBackButton,
            title = {
                AnimatedVisibility(
                    visible = visibility.value, modifier = modifier,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Text(
                        text = policy.title,
                        style = WithpeaceTheme.typography.title1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier.padding(end = 24.dp),
                    )
                }
            },
        )
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            TitleSection(modifier, policy)
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(WithpeaceTheme.colors.SystemGray3)
                    .onGloballyPositioned {
                        position.intValue = it.positionInParent().y.toInt()
                    },
            )
            PolicySummarySection(policy = policy)
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(WithpeaceTheme.colors.SystemGray3),
            )
            ApplyQualificationSection(policy = policy)
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(WithpeaceTheme.colors.SystemGray3),
            )
            ApplicationGuideSection(policy = policy)
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(WithpeaceTheme.colors.SystemGray3),
            )
            AdditionalInfoSection(policy = policy)
        }
    }
    TrackPolicyDetailScreenViewEvent(
        screenName = "policy_detail",
        policyId = policy.id,
        policyTitle = policy.title,
    )
}

@Composable
private fun TitleSection(
    modifier: Modifier,
    policy: YouthPolicyDetailUiModel,
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = policy.title,
            style = WithpeaceTheme.typography.title1,
            color = WithpeaceTheme.colors.SystemBlack,
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(16.dp),
        )
        TextFlow(text = policy.content,
            style = WithpeaceTheme.typography.body.merge(
                TextStyle(
                    lineHeight = 21.sp,
                    letterSpacing = 0.16.sp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
                )
            ),
            modifier = Modifier.fillMaxWidth(),
            obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
            obstacleContent = {
                Image(
                    modifier = modifier.padding(start = 16.dp),
                    painter = painterResource(policy.classification.drawableResId),
                    contentDescription = "정책 분류 이미지",
                )
            }
        )
        Spacer(modifier = modifier.height(16.dp))
    }
}

@Composable
fun PolicySummarySection(modifier: Modifier = Modifier, policy: YouthPolicyDetailUiModel) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = "정책을 한 눈에 확인해 보세요",
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )
        Spacer(modifier = modifier.height(24.dp))

        DescriptionTitleAndContent(modifier = modifier, title = "정책 번호", content = policy.id)
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "정책 분야",
            content = stringResource(id = policy.classification.stringResId).replace(",", "."),
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "지원 내용",
            content = policy.applicationDetails,
        )

        Spacer(modifier = modifier.height(8.dp))
    }
}

@Composable
fun ApplyQualificationSection(
    modifier: Modifier = Modifier,
    policy: YouthPolicyDetailUiModel,
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = "신청 자격을 확인하세요",
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )
        Spacer(modifier = modifier.height(24.dp))
        DescriptionTitleAndContent(modifier = modifier, title = "연령", content = policy.ageInfo)
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "거주지 및 소득",
            content = policy.residenceAndIncome,
        )
        DescriptionTitleAndContent(modifier = modifier, title = "학력", content = policy.education)
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "특화 분야",
            content = policy.specialization,
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "추가 단서 사항",
            content = policy.additionalNotes,
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "참여 제한 대상",
            content = policy.participationRestrictions,
        )
        Spacer(modifier = modifier.height(8.dp))
    }
}

@Composable
fun ApplicationGuideSection(
    modifier: Modifier = Modifier,
    policy: YouthPolicyDetailUiModel,
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = "이렇게 신청하세요",
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )
        Spacer(modifier = modifier.height(24.dp))
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "신청 절차",
            content = policy.applicationProcess,
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "심사 및 발표",
            content = policy.screeningAndAnnouncement,
        )
        HyperLinkDescriptionTitleAndContent(
            modifier = modifier,
            title = "신청 사이트",
            content = policy.applicationSite,
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "제출 서류",
            content = policy.submissionDocuments,
        )
        Spacer(modifier = modifier.height(8.dp))
    }
}

@Composable
fun AdditionalInfoSection(
    modifier: Modifier = Modifier,
    policy: YouthPolicyDetailUiModel,
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = modifier.height(24.dp))
        Text(
            text = "추가 정보를 확인해 보세요",
            style = WithpeaceTheme.typography.title2,
            color = WithpeaceTheme.colors.SystemBlack,
        )
        Spacer(modifier = modifier.height(24.dp))
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "기타 유익 정보",
            content = policy.additionalUsefulInformation,
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "주관 기관",
            content = policy.supervisingAuthority,
        )
        DescriptionTitleAndContent(
            modifier = modifier,
            title = "운영 기관",
            content = policy.operatingOrganization,
        )
        HyperLinkDescriptionTitleAndContent(
            modifier = modifier,
            title = "사업관련 참고 사이트 1",
            content = policy.businessRelatedReferenceSite1,
        )
        HyperLinkDescriptionTitleAndContent(
            modifier = modifier,
            title = "사업관련 참고 사이트 2",
            content = policy.businessRelatedReferenceSite2,
        )
        Spacer(modifier = modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PolicyDetailPreview() {
    WithpeaceTheme {
        PolicyDetailContent(
            policy = YouthPolicyDetailUiModel(
                id = "sociosqu",
                title = "facilis",
                content = "가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바가나다라마사바",
                ageInfo = "cum",
                applicationDetails = "지원내용들.....",
                residenceAndIncome = "sale",
                education = "vestibulum",
                specialization = "mattis",
                additionalNotes = "arcu",
                participationRestrictions = "urna",
                applicationProcess = "deterruisset",
                screeningAndAnnouncement = "adolescens",
                applicationSite = "consul",
                submissionDocuments = "an",
                classification = ClassificationUiModel.JOB,
                additionalUsefulInformation = "lorem",
                supervisingAuthority = "congue",
                operatingOrganization = "brute",
                businessRelatedReferenceSite1 = "noluisse",
                businessRelatedReferenceSite2 = "quo",
            ),
            onClickBackButton = {},
        )
    }
}
