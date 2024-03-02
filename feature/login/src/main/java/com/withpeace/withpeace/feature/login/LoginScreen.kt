package com.withpeace.withpeace.feature.login

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.googlelogin.GoogleLoginManager

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onShowSnackBar: (message: String) -> Unit,
) {
    LoginScreen(
        onGoogleLogin = viewModel::googleLogin,
    )
    LaunchedEffect(key1 = null) {
        viewModel.loginUiEvent.collect { uiEvent ->
            when (uiEvent) {
                is LoginUiEvent.SignUpSuccess -> {
                    onShowSnackBar("LoginRoute: 로그인 성공")
                }

                is LoginUiEvent.SignUpFail -> {
                    onShowSnackBar("LoginRoute: 로그인 실패")
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onGoogleLogin: (idToken: String) -> Unit = {},
) {
    val googleLoginManager = GoogleLoginManager(context = LocalContext.current)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(152.dp))
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = stringResource(R.string.app_logo_content_description),
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                style = WithpeaceTheme.typography.title1,
                text = stringResource(R.string.welcome_to_withpeace),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                style = WithpeaceTheme.typography.body,
                text = stringResource(R.string.welcome_introduction),
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = {
                googleLoginManager.startLogin(
                    coroutineScope = coroutineScope,
                    onSuccessLogin = onGoogleLogin,
                    onFailLogin = { Log.e("woogi", "LoginScreen: 로그인 실패") },
                )
            },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .padding(
                    bottom = 40.dp,
                    end = WithpeaceTheme.padding.BasicHorizontalPadding,
                    start = WithpeaceTheme.padding.BasicHorizontalPadding,
                )
                .fillMaxWidth(),
            border = BorderStroke(width = 1.dp, color = WithpeaceTheme.colors.SystemBlack),
            colors = ButtonDefaults.buttonColors(containerColor = WithpeaceTheme.colors.SystemWhite),
            shape = RoundedCornerShape(9.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.img_google_logo),
                    contentDescription = stringResource(R.string.image_google_logo),
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    color = WithpeaceTheme.colors.SystemBlack,
                    style = WithpeaceTheme.typography.notoSans,
                    text = stringResource(R.string.login_to_google),
                )
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 900, showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
