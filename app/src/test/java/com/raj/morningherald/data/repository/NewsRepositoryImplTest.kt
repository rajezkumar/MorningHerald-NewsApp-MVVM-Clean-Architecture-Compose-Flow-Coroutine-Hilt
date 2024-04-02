import app.cash.turbine.test
import com.raj.morningherald.core.common.connectivity.ConnectivityChecker
import com.raj.morningherald.data.local.database.NewsDatabase
import com.raj.morningherald.data.local.mapper.toArticle
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.data.remote.model.NewsDto
import com.raj.morningherald.domain.model.Source
import com.raj.morningherald.data.remote.NewsApi
import com.raj.morningherald.data.remote.model.ArticleDto
import com.raj.morningherald.data.remote.model.SourceDto
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
            val articleDto = ArticleDto(
                title = "title",
                description = "description",
                url = "url",
                urlToImage = "urlToImage",
                source = SourceDto(id = "id", name = "name")
            )
            val articlesDto = listOf(articleDto)
            val newsDto =
                NewsDto(status = "ok", articles = articlesDto, totalResults = 1)

            val articles = articlesDto.map { dto ->
                Article(
                    title = dto.title,
                    description = dto.description,
                    url = dto.url,
                    urlToImage = dto.urlToImage,
                    source = Source(id = dto.source.id, name = dto.source.name)
                )
            }

            every { connectivityChecker.hasInternetConnection() } returns true
            coEvery { newsApi.getNewsBySource(sourceName) } returns newsDto

            newsRepositoryImpl.getNewsBySource(sourceName).test {
                val emittedArticles = awaitItem()
                assertEquals(articles, emittedArticles)
                awaitComplete()
            }
            coVerify { newsApi.getNewsBySource(sourceName) }
        }
    }
}
