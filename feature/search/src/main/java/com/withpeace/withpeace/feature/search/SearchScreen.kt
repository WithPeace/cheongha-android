package com.withpeace.withpeace.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun SearchRoute() {
    SearchScreen(
        onSearchKeywordChanged = {},
        searchKeyword = "",
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    onSearchKeywordChanged: (String) -> Unit,
    searchKeyword: String,
) {
    val rememberKeyword = remember { mutableStateOf("") }
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
                modifier = modifier.size(24.dp),
                contentScale = ContentScale.None,
                painter = painterResource(R.drawable.ic_backarrow_left),
                contentDescription = "뒤로 가기",
            )
            Spacer(modifier = modifier.width(8.dp))
            SearchComponent(modifier, rememberKeyword, interactionSource)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchComponent(
    modifier: Modifier,
    rememberKeyword: MutableState<String>,
    interactionSource: MutableInteractionSource,
) {
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
            value = rememberKeyword.value,
            onValueChange = {
                rememberKeyword.value = it
                // onSearchKeywordChanged(it)
            },
            modifier = modifier.fillMaxWidth(),
            enabled = true,
            textStyle = WithpeaceTheme.typography.caption,
            singleLine = true,
            maxLines = 1,

            ) {
            TextFieldDefaults.DecorationBox(
                value = rememberKeyword.value,
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