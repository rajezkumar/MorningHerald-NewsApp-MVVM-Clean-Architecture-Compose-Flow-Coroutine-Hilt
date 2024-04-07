package com.raj.morningherald.presentation.browse

import app.cash.turbine.test
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.common.dispatcher.TestDispatcherProvider
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.domain.model.Source
import com.raj.morningherald.domain.repository.NewsRepository
import com.raj.morningherald.presentation.base.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class BrowseViewModelTest {


    @MockK
    private lateinit var newsRepository: NewsRepository

    private lateinit var dispatcherProvider: DispatcherProvider


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        Dispatchers.setMain(dispatcherProvider.io)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun browseNews_whenRepositorySuccess_shouldSetSuccessUiState() {
        runTest {
            val article = listOf(
                Article(
                    title = "title",
                    description = "description",
                    source = Source(id = "id", name = "name"),
                    url = "url",
                    urlToImage = "urlToImage"
                )
            )
            coEvery { newsRepository.browseNews("abc") } returns flow { emit(article) }

            val browseViewModel = BrowseViewModel(
                newsRepository, dispatcherProvider
            )

            browseViewModel.browseNews("abc")
            advanceTimeBy(400)
            browseViewModel.newsData.test {
                val firstItem = awaitItem()
                assert(firstItem is UiState.Success)
                cancelAndIgnoreRemainingEvents()
            }
            coVerify(exactly = 1) { newsRepository.browseNews("abc") }
        }
    }
}
