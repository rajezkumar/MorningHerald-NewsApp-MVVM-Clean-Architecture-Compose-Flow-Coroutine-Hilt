package com.raj.morningherald.presentation.browse

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.morningherald.R
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.presentation.base.ShowError
import com.raj.morningherald.presentation.base.ShowLoading
import com.raj.morningherald.presentation.base.UiState
import com.raj.morningherald.presentation.newslist.NewsListView

@Composable
fun BrowseScreen(
    browseViewModel: BrowseViewModel = hiltViewModel(),
    backPressed: () -> Unit,
    articleClicked: (Article) -> Unit
) {
    val browseUiState: UiState<List<Article>> by browseViewModel.newsData.collectAsStateWithLifecycle()
    val browseQuery = browseViewModel.query.collectAsStateWithLifecycle().value

    SearchLayout(
        browseQuery = browseQuery,
        browseResult = browseUiState,
        newsClicked = articleClicked,
        retrySearch = {
            browseViewModel.browseNews()
        },
        backPressed = backPressed
    ) {
        browseViewModel.browseNews(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLayout(
    browseQuery: String,
    browseResult: UiState<List<Article>>,
    newsClicked: (Article) -> Unit,
    retrySearch: () -> Unit,
    backPressed: () -> Unit,
    onBrowseQueryChange: (String) -> Unit
) {
    SearchBar(
        query = browseQuery,
        onQueryChange = onBrowseQueryChange,
        onSearch = {},
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        shape = SearchBarDefaults.inputFieldShape,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        active = true,
        onActiveChange = {},
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        when (browseResult) {
            is UiState.Loading -> {
                ShowLoading()
            }

            is UiState.Error -> {
                val errorText = browseResult.message
                ShowError(
                    text = errorText,
                    retryEnabled = true
                ) {
                    retrySearch()
                }
            }

            is UiState.Success -> {
                if (browseResult.data.isEmpty()) {
                    ShowError(text = stringResource(id = R.string.no_data_available))
                } else {
                    NewsListView(newsList = browseResult.data) {
                        newsClicked(it)
                    }
                }
            }

            is UiState.Empty -> {}
        }
    }
    BackHandler {
        backPressed()
    }
}


