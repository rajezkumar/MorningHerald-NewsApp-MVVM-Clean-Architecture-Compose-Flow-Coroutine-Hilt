package com.raj.morningherald.data.remote

import com.raj.morningherald.core.util.Constants.COUNTRY
import com.raj.morningherald.core.util.Constants.PAGE
import com.raj.morningherald.core.util.Constants.PAGE_SIZE
import com.raj.morningherald.data.model.News
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-Headlines")
    suspend fun getHeadlines(
        @Query("country") country: String = COUNTRY,
        @Query("page") page: Int = PAGE,
        @Query("pageSize") pageSize: Int = PAGE_SIZE
    ): News

}