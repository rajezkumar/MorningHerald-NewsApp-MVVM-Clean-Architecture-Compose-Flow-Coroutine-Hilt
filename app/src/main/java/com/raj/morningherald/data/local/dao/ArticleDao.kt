package com.raj.morningherald.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.raj.morningherald.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getAllArticles(): Flow<List<ArticleEntity>>

    @Insert
    fun insertArticles(articleEntity: List<ArticleEntity>)

    @Query("DELETE FROM article")
    fun deleteAllArticles()

    @Transaction
    fun deleteAllInsertAll(articles: List<ArticleEntity>) {
        deleteAllArticles()
        insertArticles(articles)
    }
}