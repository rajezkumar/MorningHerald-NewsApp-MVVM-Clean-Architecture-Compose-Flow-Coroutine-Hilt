package com.raj.morningherald.presentation.base

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.raj.morningherald.R
import com.raj.morningherald.data.remote.model.Article

@Composable
fun ShowLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val contentDesc = stringResource(R.string.loading)
        CircularProgressIndicator(
            modifier = modifier
                .align(Alignment.Center)
                .semantics {
                    contentDescription = contentDesc
                }
        )
    }
}

@Composable
fun ShowError(
    modifier: Modifier = Modifier,
    text: String,
    retryEnabled: Boolean = false,
    retryClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = null,
            modifier = modifier
                .width(120.dp)
                .height(120.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(horizontal = 15.dp),
            textAlign = TextAlign.Center,
        )
        if (retryEnabled) {
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { retryClicked() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun WebViewScreen(
    url: String,
    modifier: Modifier = Modifier
) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    },
        modifier = modifier
    )
}


@Composable
fun ArticleScreen(article: Article) {
    Scaffold {
        if (article.url == null) {
            ShowError(text = stringResource(id = R.string.something_went_wrong))
        } else {
            WebViewScreen(
                url = article.url, modifier = Modifier.padding(it)
            )
        }
    }
}

