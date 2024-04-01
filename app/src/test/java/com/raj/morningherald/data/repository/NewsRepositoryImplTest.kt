package com.raj.morningherald.data.repository

import app.cash.turbine.test
import com.raj.morningherald.core.common.connectivity.ConnectivityChecker
import com.raj.morningherald.data.local.dao.ArticleDao
import com.raj.morningherald.data.local.database.NewsDatabase
import com.raj.morningherald.data.local.mapper.toArticleEntity
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.data.model.NewsResponse
import com.raj.morningherald.data.model.Source
import com.raj.morningherald.data.remote.NewsApi
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsRepositoryImplTest {

    @MockK
    private lateinit var newsApi: NewsApi

    @MockK
    private lateinit var newsDatabase: NewsDatabase

    @MockK
    private lateinit var connectivityChecker: ConnectivityChecker

    @MockK
    private lateinit var articleDao: ArticleDao

    private lateinit var newsRepositoryImpl: NewsRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { newsDatabase.articleDao() } returns articleDao
        newsRepositoryImpl = NewsRepositoryImpl(newsApi, newsDatabase, connectivityChecker)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun fetchHeadlines_WithSuccessfulNetworkResponse_ReturnsSuccess() {
        runTest {
            val source = Source(id = "id", name = "name")
            val article = Article(
                title = "title",
                description = "description",
                url = "url",
                urlToImage = "urlToImage",
                source = source
            )
            val articles = listOf(article)
            val articleEntities = articles.map { it.toArticleEntity() }

            val newsResponse =
                NewsResponse(status = "ok", articles = articles, totalResults = 1)

            every { connectivityChecker.hasInternetConnection() } returns true

            coEvery { newsApi.getHeadlines() } returns newsResponse

            coEvery { articleDao.deleteAllInsertAll(any()) } just Runs
            every { articleDao.getAllArticles() } returns flowOf(articleEntities)

            newsRepositoryImpl.getHeadlines().test {
                val emittedArticles = awaitItem()
                assertEquals(articles.size, emittedArticles.size)
                assertEquals(articles, emittedArticles)
                awaitComplete()

                coVerify(exactly = 1) { newsApi.getHeadlines() }
                coVerify(exactly = 1) { articleDao.deleteAllInsertAll(articleEntities) }
                verify(exactly = 1) { articleDao.getAllArticles() }

            }
        }
    }
}