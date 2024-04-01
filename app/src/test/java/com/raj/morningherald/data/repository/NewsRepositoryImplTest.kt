import app.cash.turbine.test
import com.raj.morningherald.core.common.connectivity.ConnectivityChecker
import com.raj.morningherald.data.local.database.NewsDatabase
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.data.model.NewsResponse
import com.raj.morningherald.data.model.Source
import com.raj.morningherald.data.remote.NewsApi
import com.raj.morningherald.data.repository.NewsRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var newsRepositoryImpl: NewsRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        newsRepositoryImpl = NewsRepositoryImpl(newsApi, newsDatabase, connectivityChecker)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun fetchNewsBySources_WithSuccessfulNetworkResponse_ReturnsSuccess() {
        runTest {
            val sourceName = "abc-news"
            val article = Article(
                title = "title",
                description = "description",
                url = "url",
                urlToImage = "urlToImage",
                source = Source(id = "id", name = "name")
            )
            val articles = listOf(article)
            val newsResponse =
                NewsResponse(status = "ok", articles = articles, totalResults = 1)

            every { connectivityChecker.hasInternetConnection() } returns true
            coEvery { newsApi.getNewsBySource(sourceName) } returns newsResponse

            newsRepositoryImpl.getNewsBySource(sourceName).test {
                val emittedArticles = awaitItem()
                assertEquals(articles, emittedArticles)
                awaitComplete()
            }
            coVerify { newsApi.getNewsBySource(sourceName) }
        }
    }
}
