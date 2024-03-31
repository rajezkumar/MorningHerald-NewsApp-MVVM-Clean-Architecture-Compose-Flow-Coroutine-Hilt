package com.raj.morningherald.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.morningherald.core.util.ConnectivityChecker
import com.raj.morningherald.core.util.ConnectivityCheckerImpl
import com.raj.morningherald.core.util.DefaultDispatcherProvider
import com.raj.morningherald.core.util.DispatcherProvider
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
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val connectivityChecker: ConnectivityChecker,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _newsData = MutableStateFlow<UiState<List<Article>>>(UiState.Empty())
    val newsData: StateFlow<UiState<List<Article>>> = _newsData

    init {
        getHeadlines()
    }

    fun getHeadlines() {
        viewModelScope.launch {
            _newsData.emit(UiState.Loading())
            newsRepository.getHeadlines()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsData.value = UiState.Error(e.toString())
                }.collect {
                    _newsData.value = UiState.Success(it)
                    Log.d("Rajesh", it.toString())
                }
        }
    }
}
