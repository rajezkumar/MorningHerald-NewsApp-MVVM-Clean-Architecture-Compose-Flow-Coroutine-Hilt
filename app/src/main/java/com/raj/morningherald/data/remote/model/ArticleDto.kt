package com.raj.morningherald.data.remote.model

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("source")
    val source: SourceDto
)
