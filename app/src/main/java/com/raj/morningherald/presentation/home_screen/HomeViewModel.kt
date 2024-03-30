package com.raj.morningherald.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.morningherald.data.model.Article
import com.raj.morningherald.domain.repository.NewsRepository
import com.raj.morningherald.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {

    private val _newsData = MutableStateFlow<UiState<List<Article>>>(UiState.Empty())
    val newsData: StateFlow<UiState<List<Article>>> = _newsData

    init {
        getHeadlines()
    }

    fun getHeadlines() {
        viewModelScope.launch {
            newsRepository.getHeadlines()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _newsData.value = UiState.Error(e.toString())
                }.collect {
                    _newsData.value = UiState.Success(it)
                }
        }
    }
}
