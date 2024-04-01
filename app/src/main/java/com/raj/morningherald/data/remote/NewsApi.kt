package com.raj.morningherald.data.remote

import com.raj.morningherald.core.util.Constants.COUNTRY
import com.raj.morningherald.core.util.Constants.DEFAULT_SOURCE
import com.raj.morningherald.core.util.Constants.PAGE
import com.raj.morningherald.core.util.Constants.PAGE_SIZE
import com.raj.morningherald.data.model.NewsResponse
import com.raj.morningherald.data.model.NewsSourceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-Headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = COUNTRY,
        @Query("page") page: Int = PAGE,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): NewsResponse
    @GET("sources")
    suspend fun getNewsSources(): NewsSourceResponse
    @GET("top-Headlines")
    suspend fun getNewsBySource(
        @Query("sources") sources: String = DEFAULT_SOURCE,
        @Query("page") page: Int = PAGE,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): NewsResponse
    @GET("everything")
    suspend fun browseNews(
        @Query("q") query: String,
        @Query("page") page: Int = PAGE,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): NewsResponse
}