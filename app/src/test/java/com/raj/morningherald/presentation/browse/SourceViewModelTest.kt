package com.raj.morningherald.presentation.browse

import app.cash.turbine.test
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.common.dispatcher.TestDispatcherProvider
import com.raj.morningherald.domain.model.Source
import com.raj.morningherald.domain.repository.NewsRepository
import com.raj.morningherald.presentation.base.UiState
import com.raj.morningherald.presentation.newssource.SourceViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SourceViewModelTest {


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
    fun sourceNews_whenRepositorySuccess_shouldSetSuccessUiState() {
        runTest {
            val source = listOf(
                Source(id = "id", name = "name")
            )
            coEvery { newsRepository.getNewsSource() } returns flow { emit(source) }

            val sourceViewModel = SourceViewModel(
                newsRepository, dispatcherProvider
            )
            sourceViewModel.getNewsSource()
            sourceViewModel.newsSource.test {
                val firstItem = awaitItem()
                assert(firstItem is UiState.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}

