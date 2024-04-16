package com.raj.morningherald.di


import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE_SIZE
import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.data.remote.PagingArticle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ArticlePagingModule {

    @Provides
    @ViewModelScoped
    fun provideArticlePager(pagingArticle: PagingArticle): Pager<Int, ArticleEntity> {
        return Pager(
            config = PagingConfig(DEFAULT_PAGE_SIZE)
        ) {
            pagingArticle
        }
    }
}
