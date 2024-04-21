package com.raj.morningherald.data.remote

import com.raj.morningherald.core.util.Constants.DEFAULT_COUNTRY
import com.raj.morningherald.core.util.Constants.DEFAULT_SOURCE
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE_NO
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE_SIZE
import com.raj.morningherald.data.remote.model.NewsDto
import com.raj.morningherald.data.remote.model.NewsSourceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-Headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = DEFAULT_COUNTRY,
        @Query("page") page: Int = DEFAULT_PAGE_NO,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsDto

    @GET("sources")
    suspend fun getNewsSources(): NewsSourceDto

    @GET("top-Headlines")
    suspend fun getNewsBySource(
        @Query("sources") sources: String = DEFAULT_SOURCE,
        @Query("page") page: Int = DEFAULT_PAGE_NO,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsDto

    @GET("everything")
    suspend fun browseNews(
        @Query("q") query: String,
        @Query("page") page: Int = DEFAULT_PAGE_NO,
        @Query("pageSize") pageSize: Int = DEFAULT_PAGE_SIZE
    ): NewsDto
}
