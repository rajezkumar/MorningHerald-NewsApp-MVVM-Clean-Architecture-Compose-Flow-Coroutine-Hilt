package com.raj.morningherald.presentation.newslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.raj.morningherald.domain.model.Article

@Composable
fun NewsListView(
    newsList: List<Article>, articleClicked: (Article) -> Unit
) {
    LazyColumn {
        items(newsList) {
            Article(it) { article ->
                articleClicked(article)
            }
        }
    }
}

@Composable
fun Article(article: Article, onItemClick: (Article) -> Unit) {
    Row(modifier = Modifier.clickable {
        onItemClick(article)
    }) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            article.title?.let { ArticleTitle(title = it) }
            article.urlToImage?.let { ArticleImage(urlToImage = it, title = article.title) }
            article.description?.let { ArticleDescription(description = it) }
            Divider()
        }
    }
}

@Composable
fun ArticleTitle(title: String) {
    Text(
        text = title,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
fun ArticleImage(urlToImage: String, title: String?) {
    AsyncImage(
        model = urlToImage,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(175.dp)
            .padding(start = 16.dp, end = 16.dp)
    )
}

@Composable
fun ArticleDescription(description: String) {
    Text(
        text = description,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleSmall
    )
}


