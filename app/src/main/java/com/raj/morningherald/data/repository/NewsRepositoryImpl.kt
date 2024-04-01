package com.raj.morningherald.data.repository

import com.raj.morningherald.core.common.connectivity.ConnectivityChecker
import com.raj.morningherald.core.common.NoInternetException
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE
import com.raj.morningherald.data.local.database.NewsDatabase
import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.data.local.mapper.toArticle
import com.raj.morningherald.data.local.mapper.toArticleEntity
import com.raj.morningherald.data.remote.model.Article
import com.raj.morningherald.data.remote.model.Source
import com.raj.morningherald.data.remote.NewsApi
import com.raj.morningherald.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.Exception

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase,
    private val connectivityChecker: ConnectivityChecker
) : NewsRepository {
    override suspend fun getHeadlines(): Flow<List<Article>> {
        return flow {
            if (connectivityChecker.hasInternetConnection()) {
                try {
                    val fetchedArticles = newsApi.getHeadlines().articles
                    val articleEntities = fetchedArticles.map { it.toArticleEntity() }
                    newsDatabase.articleDao().deleteAllInsertAll(articleEntities)
                } catch (e: Exception) {
                    emit(emptyList<Article>())
                    return@flow
                }
            }
            val articles = newsDatabase.articleDao().getAllArticles()
            articles.collect { dbArticleEntities ->
                emit(dbArticleEntities.map { it.toArticle() })
            }
        }
    }

    override suspend fun getNewsSource(): Flow<List<Source>> {
        return flow {
            if (connectivityChecker.hasInternetConnection()) {
                emit(newsApi.getNewsSources().sources)
            } else {
                throw NoInternetException()
            }
        }
    }

    override suspend fun getNewsBySource(source: String): Flow<List<Article>> {
        return flow {
            if (connectivityChecker.hasInternetConnection()) {
                emit(newsApi.getNewsBySource(source).articles)
            } else {
                throw NoInternetException()
            }
        }
    }

    override suspend fun browseNews(query: String): Flow<List<Article>> {
        return flow {
            if (connectivityChecker.hasInternetConnection()) {
                emit(newsApi.browseNews(query).articles)
            } else {
                throw NoInternetException()
            }
        }
    }

    suspend fun getNewsFromDb(): List<ArticleEntity> {
        return newsDatabase.articleDao().getAllArticles().first()
    }

    override suspend fun getHeadlinesPagination(pageNumber: Int): List<ArticleEntity> {
        val fetchedArticles = newsApi.getHeadlines(
            page = pageNumber
        ).articles
        val articleEntities = fetchedArticles.map { it.toArticleEntity() }
        return if (pageNumber == DEFAULT_PAGE) {
            newsDatabase.articleDao().deleteAllInsertAll(articleEntities)
            newsDatabase.articleDao().getAllArticles().first()
        } else {
            articleEntities
        }
    }

}
