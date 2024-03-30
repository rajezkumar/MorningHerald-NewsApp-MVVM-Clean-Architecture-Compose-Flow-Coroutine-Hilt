package com.raj.morningherald.data.repository

import com.raj.morningherald.data.local.database.NewsDatabase
import com.raj.morningherald.data.local.mapper.toArticle
import com.raj.morningherald.data.local.mapper.toArticleEntity
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.data.remote.NewsApi
import com.raj.morningherald.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase
) : NewsRepository {
    override suspend fun getHeadlines(): Flow<List<Article>> {
        return flow {
            val fetchedArticles = newsApi.getHeadlines().articles
            val articleEntities = fetchedArticles.map { it.toArticleEntity() }
            newsDatabase.articleDao().deleteAllInsertAll(articleEntities)
            newsDatabase.articleDao().getAllArticles().collect { dbArticleEntities ->
                emit(dbArticleEntities.map { it.toArticle() })
            }

        }
    }
}
