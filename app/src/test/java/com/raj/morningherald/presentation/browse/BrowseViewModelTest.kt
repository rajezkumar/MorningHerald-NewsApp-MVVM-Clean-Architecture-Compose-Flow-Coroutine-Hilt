package com.raj.morningherald.presentation.browse

import app.cash.turbine.test
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.common.dispatcher.TestDispatcherProvider
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.domain.model.Source
import com.raj.morningherald.domain.usecase.BrowseNewsUseCase
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
    private lateinit var browseNewsUseCase: BrowseNewsUseCase

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
    fun browseNews_WithValidQuery_ShouldSetSuccessUiState() = runTest {
        val article = listOf(
            Article(
                title = "title",
                description = "description",
                source = Source(id = "id", name = "name"),
                url = "url",
                urlToImage = "urlToImage"
            )
        )
        coEvery { browseNewsUseCase("abc") } returns flow { emit(article) }

        val browseViewModel = BrowseViewModel(
            dispatcherProvider, browseNewsUseCase
        )

        browseViewModel.browseNews("abc")
        advanceTimeBy(400)
        browseViewModel.newsData.test {
            val firstItem = awaitItem()
            assert(firstItem is UiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { browseNewsUseCase("abc") }
    }

    @Test
    fun browseNews_WithEmptyQuery_ShouldNotCallUseCase() = runTest {
        val browseViewModel = BrowseViewModel(
            dispatcherProvider, browseNewsUseCase
        )

        browseViewModel.browseNews("")
        advanceTimeBy(400)
        coVerify(exactly = 0) { browseNewsUseCase(any()) }
    }

    @Test
    fun browseNews_WithShortQuery_ShouldNotCallUseCase() = runTest {
        val browseViewModel = BrowseViewModel(
            dispatcherProvider, browseNewsUseCase
        )

        browseViewModel.browseNews("a")
        advanceTimeBy(400)
        coVerify(exactly = 0) { browseNewsUseCase(any()) }
    }

    @Test
    fun browseNews_WithUseCaseFailure_ShouldSetErrorUiState() = runTest {
        coEvery { browseNewsUseCase("abc") } throws Exception("Error")

        val browseViewModel = BrowseViewModel(
            dispatcherProvider, browseNewsUseCase
        )

        browseViewModel.browseNews("abc")
        advanceTimeBy(400)
        browseViewModel.newsData.test {
            val firstItem = awaitItem()
            assert(firstItem is UiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { browseNewsUseCase("abc") }
    }
}


