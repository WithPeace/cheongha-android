package com.withpeace.withpeace.core.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun WithpeaceWebView(
    url: String?,
    modifier: Modifier = Modifier,
) {
    if (url != null) {
        WebView(
            url = url,
            modifier = modifier,
        )
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "오류가 발생했습니다 다시 시도해주세요",
            )
        }
    }
}

@Composable
private fun WebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    var progress by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        if (progress != 100) {
            LinearProgressIndicator(
                progress = progress / 100f,
                color = WithpeaceTheme.colors.MainPurple,
            )
        }
        AndroidView(
            factory = { context ->
                createWebView(context) { progress = it }
            },
            update = {
                it.loadUrl(url)
            },
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createWebView(context: Context, onSetProgress: (Int) -> Unit) = WebView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT,
    )
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            onSetProgress(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
    settings.apply {
        builtInZoomControls = false
        domStorageEnabled = true
        javaScriptEnabled = true
        loadWithOverviewMode = true
        blockNetworkLoads = false
        setSupportZoom(false)
    }
}