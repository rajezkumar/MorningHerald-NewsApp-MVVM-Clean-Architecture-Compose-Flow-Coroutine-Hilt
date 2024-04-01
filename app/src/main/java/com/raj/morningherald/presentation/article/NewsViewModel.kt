package com.raj.morningherald.presentation.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.morningherald.core.util.Constants
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.util.ValidationUtil.checkIfValidArgNews
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.domain.repository.NewsRepository
import com.raj.morningherald.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _newsData = MutableStateFlow<UiState<List<Article>>>(UiState.Empty())
    val newsData: StateFlow<UiState<List<Article>>> = _newsData

    init {
        fetchNews()
    }

    private fun fetchNews() {
        if (checkIfValidArgNews(savedStateHandle.get("source") as? String?)) {
            fetchNewsBySource(savedStateHandle.get("source"))
        } else {
            getHeadlines()
        }
    }

    fun getHeadlines() {
        viewModelScope.launch {
            _newsData.emit(UiState.Loading())
            newsRepository.getHeadlines()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsData.value = UiState.Error(e.message.toString())
                }.collect { article ->
                    _newsData.value = UiState.Success(article)
                }
        }
    }

    private fun fetchNewsBySource(source: String?) {
        viewModelScope.launch {
            _newsData.emit(UiState.Loading())
            newsRepository.getNewsBySource(source ?: Constants.DEFAULT_SOURCE)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsData.value = UiState.Error(e.message.toString())
                }.collect { article ->
                    _newsData.value = UiState.Success(article)
                }
        }
    }
}
