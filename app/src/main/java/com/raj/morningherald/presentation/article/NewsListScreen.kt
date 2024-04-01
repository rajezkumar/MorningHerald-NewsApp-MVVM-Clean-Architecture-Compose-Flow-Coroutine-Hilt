package com.raj.morningherald.presentation.article

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.morningherald.R
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.presentation.base.ShowError
import com.raj.morningherald.presentation.base.ShowLoading
import com.raj.morningherald.presentation.base.UiState

@Composable
fun NewsListScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {
    val articlesUiState: UiState<List<Article>> by newsViewModel.newsData.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (articlesUiState) {
            is UiState.Loading -> {
                ShowLoading()
            }

            is UiState.Error -> {
                val errorText = (articlesUiState as UiState.Error<List<Article>>).message
                ShowError(
                    text = errorText,
                    retryEnabled = true
                ) {
                    newsViewModel.getHeadlines()
                }
            }

            is UiState.Success -> {
                if ((articlesUiState as UiState.Success<List<Article>>).data.isEmpty()
                ) {
                    ShowError(text = stringResource(R.string.no_data_available))
                } else {
                    NewsListView(newsList = (articlesUiState as UiState.Success<List<Article>>).data) {
                        newsClicked(it)
                    }
                }
            }

            is UiState.Empty -> {}
        }
    }
}

