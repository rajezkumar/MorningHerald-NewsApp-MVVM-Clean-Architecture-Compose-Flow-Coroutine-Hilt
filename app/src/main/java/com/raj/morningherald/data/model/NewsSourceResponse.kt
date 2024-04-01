package com.raj.morningherald.data.model

import com.google.gson.annotations.SerializedName

data class NewsSourceResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("sources")
    val sources: List<Source> = ArrayList()
)

