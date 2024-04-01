package com.raj.morningherald.presentation.article

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raj.morningherald.R
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.presentation.base.ShowError
import com.raj.morningherald.presentation.base.WebViewScreen

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