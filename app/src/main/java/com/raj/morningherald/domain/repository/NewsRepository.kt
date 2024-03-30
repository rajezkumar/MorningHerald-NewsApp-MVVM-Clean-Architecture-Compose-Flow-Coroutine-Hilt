package com.raj.morningherald.domain.repository

import com.raj.morningherald.data.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getHeadlines(): Flow<List<Article>>
}