package com.raj.morningherald.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "article")
data class SourceEntity(
    @ColumnInfo(name = "sourceId")
    val id: String?,
    @ColumnInfo(name = "sourceName")
    val name: String?
)
