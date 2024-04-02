package com.raj.morningherald.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raj.morningherald.core.common.NoInternetException
import com.raj.morningherald.core.common.connectivity.ConnectivityChecker
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE
import com.raj.morningherald.core.util.Constants.DEFAULT_PAGE_SIZE
import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.data.repository.NewsRepositoryImpl
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PagingArticle @Inject constructor(
    private val newsRepositoryImpl: NewsRepositoryImpl,
    private val connectivityChecker: ConnectivityChecker,
    private val dispatcherProvider: DispatcherProvider
) : PagingSource<Int, ArticleEntity>() {
    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        val page = params.key ?: 1
        lateinit var loadResult: LoadResult<Int, ArticleEntity>

        withContext(dispatcherProvider.io) {
            kotlin.runCatching {
                if (!connectivityChecker.hasInternetConnection()) {
                    if (page == DEFAULT_PAGE) {
                        val articles = newsRepositoryImpl.getNewsFromDb()
                        loadResult = LoadResult.Page(
                            data = articles,
                            prevKey = page.minus(1),
                            nextKey = if (articles.isEmpty()) null else page.plus(1)
                        )
                    } else {
                        throw NoInternetException()
                    }
                } else {
                    val articles = newsRepositoryImpl.getHeadlinesPagination(page)
                    loadResult = LoadResult.Page(
                        data = articles,
                        prevKey = if (page == 1) null else page.minus(1),
                        nextKey = if (articles.isEmpty()) null else page.plus(1)
                    )
                }
            }.onFailure {
                loadResult = LoadResult.Error(it)
            }
        }
        return loadResult
    }
}