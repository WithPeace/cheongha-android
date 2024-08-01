package com.withpeace.withpeace.feature.policybookmarks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.designsystem.util.dropShadow
import com.withpeace.withpeace.core.ui.bookmark.BookmarkButton
import com.withpeace.withpeace.feature.policybookmarks.uistate.BookmarkedPolicyUIState
import com.withpeace.withpeace.feature.policybookmarks.uistate.BookmarkedYouthPolicyUiModel

@Composable
fun PolicyBookmarksRoute(
    viewModel: PolicyBookmarkViewModel = hiltViewModel(),
    onShowSnackBar: (message: String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    val bookmarkedPolicyUiState = viewModel.bookmarkedPolicies.collectAsStateWithLifecycle()
    PolicyBookmarksScreen(
        onClickBackButton = onClickBackButton,
        bookmarkedPolicyUIState = bookmarkedPolicyUiState.value,
        onBookmarkClick = viewModel::bookmark,
    )
}

@Composable
fun PolicyBookmarksScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    bookmarkedPolicyUIState: BookmarkedPolicyUIState,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = {
                onClickBackButton()
            },
            title = {
                Text(
                    text = "내가 찜한 정책",
                    style = WithpeaceTheme.typography.title1,
                    color = WithpeaceTheme.colors.SnackbarBlack,
                )
            },
        )
        when (bookmarkedPolicyUIState) {
            is BookmarkedPolicyUIState.Success -> {
                BookmarkedPolicyList(
                    bookmarkedYouthPolicies = bookmarkedPolicyUIState.youthPolicies,
                    onBookmarkClick = onBookmarkClick,
                )
            }

            is BookmarkedPolicyUIState.Loading -> {
            }

            is BookmarkedPolicyUIState.Failure -> {
            }
        }

    }
}

@Composable
private fun BookmarkedPolicyList(
    modifier: Modifier = Modifier,
    bookmarkedYouthPolicies: List<BookmarkedYouthPolicyUiModel>,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = modifier.height(8.dp))
        LazyColumn(contentPadding = PaddingValues(bottom = 8.dp)) {
            items(bookmarkedYouthPolicies.size) {
                Spacer(modifier = modifier.height(8.dp))
                YouthPolicyCard(
                    youthPolicy = bookmarkedYouthPolicies[it],
                    onPolicyClick = {},
                    onBookmarkClick = onBookmarkClick,
                )
            }
        }
    }
}

@Composable
private fun YouthPolicyCard(
    modifier: Modifier = Modifier,
    youthPolicy: BookmarkedYouthPolicyUiModel,
    onPolicyClick: (String) -> Unit,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = WithpeaceTheme.colors.SystemWhite,
                shape = RoundedCornerShape(size = 10.dp),
            )
            .dropShadow(
                color = Color(0x1A000000),
                blurRadius = 4.dp,
                spreadRadius = 2.dp,
                borderRadius = 10.dp,
            )
            .clickable {
                onPolicyClick(youthPolicy.id)
            },
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .background(WithpeaceTheme.colors.SystemWhite)
                .padding(16.dp),
        ) {
            val (
                title, content,
                region, ageRange, thumbnail, heart,
            ) = createRefs()

            Text(
                text = youthPolicy.title,
                modifier.constrainAs(
                    title,
                    constrainBlock = {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(thumbnail.start, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    },
                ),
                color = WithpeaceTheme.colors.SystemBlack,
                style = WithpeaceTheme.typography.homePolicyTitle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = youthPolicy.content,
                modifier = modifier
                    .constrainAs(
                        content,
                        constrainBlock = {
                            top.linkTo(title.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(thumbnail.start, margin = 8.dp)
                            width = Dimension.fillToConstraints
                        },
                    ),
                color = WithpeaceTheme.colors.SystemBlack,
                style = WithpeaceTheme.typography.homePolicyContent,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )

            BookmarkButton(
                modifier = modifier.constrainAs(
                    heart,
                ) {
                    top.linkTo(content.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                isClicked = youthPolicy.isBookmarked,
                onClick = { isClicked ->
                    onBookmarkClick(youthPolicy.id, isClicked)
                },
            )

            Text(
                text = youthPolicy.region.name,
                color = WithpeaceTheme.colors.MainPurple,
                modifier = modifier
                    .constrainAs(
                        region,
                        constrainBlock = {
                            top.linkTo(heart.top)
                            start.linkTo(heart.end, margin = 8.dp)
                            bottom.linkTo(heart.bottom)
                        },
                    )
                    .background(
                        color = WithpeaceTheme.colors.SubPurple,
                        shape = RoundedCornerShape(size = 5.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = WithpeaceTheme.typography.homePolicyTag,
            )

            Text(
                text = youthPolicy.ageInfo,
                color = WithpeaceTheme.colors.SystemGray1,
                modifier = modifier
                    .constrainAs(
                        ageRange,
                        constrainBlock = {
                            top.linkTo(region.top)
                            start.linkTo(region.end, margin = 8.dp)
                            bottom.linkTo(region.bottom)
                        },
                    )
                    .background(
                        color = WithpeaceTheme.colors.SystemGray3,
                        shape = RoundedCornerShape(size = 5.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = WithpeaceTheme.typography.homePolicyTag,
            )


            Image(
                modifier = modifier
                    .size(57.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .constrainAs(
                        ref = thumbnail,
                        constrainBlock = {
                            start.linkTo(title.end)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        },
                    ),
                painter = painterResource(id = youthPolicy.classification.drawableResId),
                contentDescription = stringResource(id = youthPolicy.classification.stringResId),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PolicyBookmarksPreview() {
    PolicyBookmarksScreen(
        bookmarkedPolicyUIState = BookmarkedPolicyUIState.Success(
            youthPolicies = listOf(),

            ),
        onClickBackButton = {

        },
        onBookmarkClick = { id, ischecked ->

        },
    )
}
