package com.raj.morningherald.domain.di

import com.raj.morningherald.domain.repository.NewsRepository
import com.raj.morningherald.domain.usecase.BrowseNewsUseCase
import com.raj.morningherald.domain.usecase.GetArticleHeadlinesUseCase
import com.raj.morningherald.domain.usecase.GetNewsBySourceUseCase
import com.raj.morningherald.domain.usecase.GetNewsSourceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetNewsSourceUseCase(newsRepository: NewsRepository): GetNewsSourceUseCase {
        return GetNewsSourceUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideGetNewsBySourceUseCase(newsRepository: NewsRepository): GetNewsBySourceUseCase {
        return GetNewsBySourceUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideBrowseNewsUseCase(newsRepository: NewsRepository): BrowseNewsUseCase {
        return BrowseNewsUseCase(newsRepository)
    }

    @Provides
    @Singleton
    fun provideGetArticleUseCase(newsRepository: NewsRepository): GetArticleHeadlinesUseCase {
        return GetArticleHeadlinesUseCase(newsRepository)
    }
}
