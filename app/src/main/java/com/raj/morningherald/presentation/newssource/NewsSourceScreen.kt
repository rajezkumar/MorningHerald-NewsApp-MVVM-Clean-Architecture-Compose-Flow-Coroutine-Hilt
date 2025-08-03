package com.raj.morningherald.presentation.newssource

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raj.morningherald.R
import com.raj.morningherald.domain.model.Source
import com.raj.morningherald.presentation.base.ShowError
import com.raj.morningherald.presentation.base.ShowLoading
import com.raj.morningherald.presentation.base.UiState

@Composable
fun NewsSourceScreen(
    uiState: UiState<List<Source>>,
    onRetry: () -> Unit,
    onSourceClick: (Source) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> ShowLoading()
        is UiState.Error -> ShowError(
            text = uiState.message,
            retryEnabled = true,
            retryClicked = onRetry
        )
        is UiState.Success -> SourceListLayout(
            sourceList = uiState.data,
            sourceClicked = onSourceClick
        )
        is UiState.Empty -> { /* Handle empty state */ }
    }
}


@Composable
fun SourceListLayout(
    sourceList: List<Source>,
    sourceClicked: (Source) -> Unit
) {
    LazyColumn {
        items(sourceList) {
            SourceItem(it) { source ->
                sourceClicked(source)
            }
        }
    }
}

@Composable
fun SourceItem(
    source: Source,
    onItemClick: (Source) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp)
        .clickable {
            onItemClick(source)
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color = Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(text = source.name ?: stringResource(R.string.unknown), color = Color.White)
        }
    }
}