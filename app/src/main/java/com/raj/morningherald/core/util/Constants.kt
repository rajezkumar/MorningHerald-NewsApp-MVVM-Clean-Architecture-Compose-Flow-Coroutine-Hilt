package com.raj.morningherald.core.util

import com.raj.morningherald.BuildConfig

object Constants {
    const val DEFAULT_COUNTRY = "us"
    const val DEFAULT_PAGE_NO = 1
    const val DEFAULT_PAGE_SIZE = 20
    const val DEFAULT_SOURCE = "abc-news"
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = BuildConfig.API_KEY
    const val DB_NAME = "article.db"
    const val BROWSE_DEBOUNCE = 300L
}
