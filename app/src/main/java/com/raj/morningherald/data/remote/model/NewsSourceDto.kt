package com.raj.morningherald.data.remote.model

import com.google.gson.annotations.SerializedName

data class NewsSourceDto(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("sources")
    val sources: List<SourceDto> = ArrayList()
)

