package com.raj.morningherald.presentation.newssource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.domain.model.Source
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
class SourceViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _newsSource = MutableStateFlow<UiState<List<Source>>>(UiState.Empty())
    val newsSource: StateFlow<UiState<List<Source>>> = _newsSource

    init {
        getNewsSource()
    }

    fun getNewsSource() {
        viewModelScope.launch {
            _newsSource.emit(UiState.Loading())
            newsRepository.getNewsSource()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsSource.value = UiState.Error(e.message.toString())
                }.collect { source ->
                    _newsSource.value = UiState.Success(source)
                }
        }
    }
}
