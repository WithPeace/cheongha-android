package com.withpeace.withpeace.feature.policydetail

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.ui.policy.ClassificationUiModel
import com.withpeace.withpeace.core.ui.policy.RegionUiModel
import com.withpeace.withpeace.core.ui.policy.YouthPolicyUiModel
import com.withpeace.withpeace.feature.policydetail.component.DescriptionTitleAndContent
import com.withpeace.withpeace.feature.policydetail.component.HyperLinkDescriptionTitleAndContent

@Composable
fun PolicyDetailRoute(
    policy: YouthPolicyUiModel,
    onShowSnackBar: (message: String) -> Unit,
    viewModel: PolicyDetailViewModel = hiltViewModel(),
    onClickBackButton: () -> Unit,
) {
    PolicyDetailScreen(
        onClickBackButton = onClickBackButton,
        policy = policy,
    )

}

@Composable
fun PolicyDetailScreen(
    policy: YouthPolicyUiModel,
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
                Text(
                    text = policy.title,
                    style = WithpeaceTheme.typography.title1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
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
                    .background(WithpeaceTheme.colors.SystemGray3),
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
        }
    }
}

@Composable
private fun TitleSection(
    modifier: Modifier,
    policy: YouthPolicyUiModel,
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = modifier.height(24.dp))
        // TODO(Tobbar 스크롤 체크)
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
        ConstraintLayout(modifier = modifier.fillMaxWidth()) {
            val (content, image) = createRefs()
            Text(
                modifier = modifier.constrainAs(
                    ref = content,
                    constrainBlock = {
                        width = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(image.start)
                    },
                ),
                overflow = TextOverflow.Ellipsis,
                text = policy.content + policy.content + policy.content,
                maxLines = 2,
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemBlack,
            )
            Image(
                modifier = modifier
                    .constrainAs(
                        ref = image,
                        constrainBlock = {
                            top.linkTo(parent.top)
                            start.linkTo(content.end)
                            end.linkTo(parent.end)
                        },
                    )
                    .padding(start = 16.dp),
                painter = painterResource(policy.classification.drawableResId),
                contentDescription = "정책 분류 이미지",
            )
        }
        Spacer(modifier = modifier.height(16.dp))
    }
}

@Composable
fun PolicySummarySection(modifier: Modifier = Modifier, policy: YouthPolicyUiModel) {
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
    policy: YouthPolicyUiModel,
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
    policy: YouthPolicyUiModel,
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
    }
}

@Preview(showBackground = true)
@Composable
fun PolicyDetailPreview() {
    WithpeaceTheme {
        PolicyDetailScreen(
            policy = YouthPolicyUiModel(
                id = "sociosqu",
                title = "facilis",
                content = "civibuscivibuscivibuscivibuscivibuscivibuscivibuscivibuscivibuscivibuscivibuscivibus",
                region = RegionUiModel.대구,
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
            ),
        ) {
        }
    }
}

//TODO caption 행간 확인