package com.raj.morningherald.domain.repository

import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.domain.model.Source
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getHeadlines(): Flow<List<Article>>
    suspend fun getNewsSource(): Flow<List<Source>>
    suspend fun getNewsBySource(source: String): Flow<List<Article>>
    suspend fun browseNews(query: String): Flow<List<Article>>
    suspend fun getHeadlinesPagination(pageNumber: Int): List<ArticleEntity>

}
