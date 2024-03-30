package com.raj.morningherald.data.repository

import com.raj.morningherald.data.model.Article
import com.raj.morningherald.data.remote.NewsApi
import com.raj.morningherald.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRepository {
    override suspend fun getHeadlines(): Flow<List<Article>> {
        return flow {
            emit(newsApi.getHeadlines())
        }.map {
            it.articles
        }
    }
}
