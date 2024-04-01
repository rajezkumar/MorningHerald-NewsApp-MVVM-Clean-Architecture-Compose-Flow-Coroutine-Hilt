package com.raj.morningherald.data.local.mapper

import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.data.local.entity.SourceEntity
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.data.model.Source

fun Article.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        source = source.toSourceEntity()
    )
}

fun Source.toSourceEntity(): SourceEntity {
    return SourceEntity(
        id = id,
        name = name
    )
}

fun ArticleEntity.toArticle(): Article {
    return Article(
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        source = source.toSource()
    )
}

fun SourceEntity.toSource(): Source {
    return Source(
        id = id,
        name = name
    )
}