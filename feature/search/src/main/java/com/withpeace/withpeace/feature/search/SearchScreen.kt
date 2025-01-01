package com.withpeace.withpeace.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.domain.model.error.NoSearchResultException
import com.withpeace.withpeace.core.ui.policy.YouthPolicyCard
import com.withpeace.withpeace.core.ui.policy.YouthPolicyUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRoute(
    onShowSnackBar: (message: String) -> Unit,
    onNavigationSnackBar: (message: String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel(),
    onBackButtonClick: () -> Unit,
    onPolicyClick: (String) -> Unit,
) {
    val recentSearchKeywords = viewModel.recentSearchKeywords.collectAsStateWithLifecycle()
    val searchKeyword = viewModel.searchKeyword.collectAsStateWithLifecycle()
    val youthPolicies = viewModel.youthPolicyPagingFlow.collectAsLazyPagingItems()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val dialogState = remember { mutableStateOf(false) }
    val totalItemCount = viewModel.totalCountFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect {
            when (it) {
                SearchUiEvent.BookmarkSuccess -> {
                    onNavigationSnackBar("관심 목록에 추가되었습니다.")
                }

                SearchUiEvent.BookmarkFailure -> {
                    onShowSnackBar("찜하기에 실패했습니다. 다시 시도해주세요.")
                }

                SearchUiEvent.UnBookmarkSuccess -> {
                    onShowSnackBar("관심목록에서 삭제되었습니다.")
                }

                SearchUiEvent.SingleCharacteristicError -> {
                    dialogState.value = true
                }
            }
        }
    }
    if (dialogState.value) {
        BasicAlertDialog(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp),
                )
                .background(color = WithpeaceTheme.colors.SystemWhite)
                .padding(vertical = 24.dp),
            onDismissRequest = {
                dialogState.value = false
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "두 글자 이상 입력해주세요.",
                    style = WithpeaceTheme.typography.title2,
                    color = WithpeaceTheme.colors.SnackbarBlack,
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(
                            RoundedCornerShape(10.dp),
                        )
                        .background(color = WithpeaceTheme.colors.MainPurple),
                    onClick = {
                        dialogState.value = false
                    },
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "확인",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemWhite,
                    )
                }
            }
        }
    }
    SearchScreen(
        onKeywordTextChanged = viewModel::onChangedKeyword,
        searchKeyword = searchKeyword.value,
        uiState = uiState.value,
        recentSearchKeyword = recentSearchKeywords.value,
        onBackButtonClick = onBackButtonClick,
        onClickSearchKeyword = viewModel::onClickSearchKeyword,
        onRemoveRecentKeyword = viewModel::removeKeywords,
        onRecentKeywordClick = viewModel::search,
        youthPolicies = youthPolicies,
        onBookmarkClick = viewModel::bookmark,
        onPolicyClick = onPolicyClick,
        totalItemCount = totalItemCount.value,
    )
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    searchKeyword: String,
    totalItemCount: Int,
    youthPolicies: LazyPagingItems<YouthPolicyUiModel>,
    recentSearchKeyword: List<String>,
    uiState: SearchUiState,
    onBackButtonClick: () -> Unit,
    onKeywordTextChanged: (String) -> Unit,
    onClickSearchKeyword: (String) -> Unit,
    onRemoveRecentKeyword: () -> Unit,
    onRecentKeywordClick: () -> Unit,
    onPolicyClick: (String) -> Unit,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        Spacer(modifier = modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Image(
                modifier = modifier
                    .size(24.dp)
                    .clickable {
                        onBackButtonClick()
                    },
                contentScale = ContentScale.None,
                painter = painterResource(R.drawable.ic_backarrow_left),
                contentDescription = "뒤로 가기",
            )
            Spacer(modifier = modifier.width(8.dp))
            SearchComponent(
                modifier = modifier,
                interactionSource = interactionSource,
                onSearchKeywordChanged = onKeywordTextChanged,
                onSearchClick = onRecentKeywordClick,
                searchKeyword = searchKeyword,
            )
        }
        Spacer(modifier = modifier.height(7.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = WithpeaceTheme.colors.SystemGray3,
        )
        when (uiState) {
            is SearchUiState.PagingData -> {
                SearchCompleted(
                    youthPolicies = youthPolicies,
                    searchKeyword = searchKeyword,
                    onPolicyClick = onPolicyClick,
                    onBookmarkClick = onBookmarkClick,
                    totalItemCount = totalItemCount,
                )
            }

            else -> {
                SearchIntro(
                    recentSearchKeyword = recentSearchKeyword,
                    onClickSearchKeyword = onClickSearchKeyword,
                    onRemoveKeyword = onRemoveRecentKeyword,
                )
            }
        }
    }
}

@Composable
private fun SearchCompleted(
    modifier: Modifier = Modifier,
    totalItemCount: Int,
    searchKeyword: String,
    youthPolicies: LazyPagingItems<YouthPolicyUiModel>,
    onPolicyClick: (String) -> Unit,
    onBookmarkClick: (id: String, isChecked: Boolean) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF8F9FB)),
    ) {
        if (youthPolicies.loadState.refresh is LoadState.Error) {
            if ((youthPolicies.loadState.refresh as LoadState.Error).error is NoSearchResultException) {
                Column(
                    modifier = modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxSize()
                        .background(color = Color(0xFFF8F9FB)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = modifier.height(76.dp))
                    Image(
                        painter = painterResource(com.withpeace.withpeace.feature.search.R.drawable.ic_caution),
                        contentDescription = "결과 없음",
                        modifier = modifier.size(52.dp),
                    )
                    Spacer(modifier = modifier.height(16.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("“")
                            withStyle(
                                style = SpanStyle(
                                    color = WithpeaceTheme.colors.MainPurple,
                                ),
                            ) { // 키워드의 색상 설정
                                append(searchKeyword)
                            }
                            append("”에 해당하는 검색 결과를 찾지 못했어요")
                        },
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray1,
                    )
                    Spacer(modifier.height(8.dp))
                    Text(
                        "다른 검색어를 입력해보세요",
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray1,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .testTag("home:policies"),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp),
            ) {
                item {
                    Spacer(modifier = modifier.height(16.dp))
                    Text(
                        "총 ${totalItemCount}개",
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemGray1,
                    )
                    Spacer(modifier = modifier.height(16.dp))
                }
                items(
                    count = youthPolicies.itemCount,
                    key = youthPolicies.itemKey { it.id },
                ) {
                    val youthPolicy = youthPolicies[it] ?: throw IllegalStateException()
                    Spacer(modifier = modifier.height(8.dp))
                    YouthPolicyCard(
                        modifier = modifier,
                        youthPolicy = youthPolicy,
                        onPolicyClick = onPolicyClick,
                        onBookmarkClick = onBookmarkClick,
                    )
                }
                item {
                    if (youthPolicies.loadState.append is LoadState.Loading) {
                        Column(
                            modifier = modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .background(
                                    Color.Transparent,
                                ),
                        ) {
                            CircularProgressIndicator(
                                modifier.align(Alignment.CenterHorizontally),
                                color = WithpeaceTheme.colors.MainPurple,
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchComponent(
    modifier: Modifier,
    searchKeyword: String,
    interactionSource: MutableInteractionSource,
    onSearchKeywordChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(
                    size = 999.dp,
                ),
                color = WithpeaceTheme.colors.SystemGray3,
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
    ) {
        Image(
            modifier = modifier.size(17.dp),
            painter = painterResource(com.withpeace.withpeace.feature.search.R.drawable.ic_search),
            contentDescription = "검색 아이콘",
        )
        Spacer(modifier.width(8.dp))
        BasicTextField(
            value = searchKeyword,
            onValueChange = {
                onSearchKeywordChanged(it)
            },
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            textStyle = WithpeaceTheme.typography.caption,
            singleLine = true,
            maxLines = 1,
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick()
                    keyboardController?.hide()
                },
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        ) {
            TextFieldDefaults.DecorationBox(
                value = searchKeyword,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = "어떤 정책이 궁금하신가요?",
                        style = WithpeaceTheme.typography.caption,
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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchIntro(
    modifier: Modifier = Modifier,
    recentSearchKeyword: List<String>,
    onClickSearchKeyword: (String) -> Unit,
    onRemoveKeyword: () -> Unit,
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = modifier.height(24.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "최근 검색어",
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemGray1,
            )
            Text(
                text = "지우기",
                modifier = modifier.clickable {
                    onRemoveKeyword()
                },
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray2,
            )
        }
        Spacer(modifier = modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recentSearchKeyword.map {
                Text(
                    modifier = modifier
                        .background(
                            color = WithpeaceTheme.colors.SubPurple,
                            shape = RoundedCornerShape(999.dp),
                        )
                        .clickable {
                            onClickSearchKeyword(it)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    style = WithpeaceTheme.typography.caption,
                    color = WithpeaceTheme.colors.MainPurple,
                    text = it,
                )
            }
        }
    }
}

//TODO 문구, 다이얼로그, 데이터 갯수