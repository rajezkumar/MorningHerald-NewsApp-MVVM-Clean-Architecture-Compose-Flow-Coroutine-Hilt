package com.raj.morningherald.presentation.newslist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.raj.morningherald.R
import com.raj.morningherald.data.remote.model.Article
import com.raj.morningherald.presentation.base.ShowError
import com.raj.morningherald.presentation.base.ShowLoading
import com.raj.morningherald.presentation.base.UiState

@ExperimentalMaterial3Api
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
                    newsViewModel.getArticleHeadlines()
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

@ExperimentalMaterial3Api
@Composable
fun NewsListPagingScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
    newsClicked: (Article) -> Unit
) {
    val pagingData = newsViewModel.newsDataPaging.collectAsLazyPagingItems()
    when (pagingData.loadState.refresh) {
        is LoadState.Loading -> {
            ShowLoading()
        }

        is LoadState.Error -> {
            val errorText =
                (pagingData.loadState.refresh as LoadState.Error).error.message
                    ?: stringResource(id = R.string.something_went_wrong)
            ShowError(
                text = errorText,
                retryEnabled = true
            ) {
                newsViewModel.getArticlesPagination()
            }
        }

        else -> {
            NewsPagingAppend(pagingData, newsClicked)
        }
    }
}


@Composable
private fun NewsPagingAppend(
    pagingData: LazyPagingItems<Article>,
    newsClicked: (Article) -> Unit,
) {
    LazyColumn {
        items(pagingData.itemCount) { index ->
            pagingData.get(index)?.let { article ->
                Article(article) {
                    newsClicked(article)
                }
            }
        }
    }
}
