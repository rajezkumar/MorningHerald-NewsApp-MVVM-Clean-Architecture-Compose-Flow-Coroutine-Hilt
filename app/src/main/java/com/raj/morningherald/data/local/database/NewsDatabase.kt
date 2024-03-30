package com.raj.morningherald.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raj.morningherald.data.local.dao.ArticleDao
import com.raj.morningherald.data.local.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}