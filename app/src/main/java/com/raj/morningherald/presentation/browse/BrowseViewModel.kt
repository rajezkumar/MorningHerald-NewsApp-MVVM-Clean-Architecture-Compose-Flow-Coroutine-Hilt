package com.raj.morningherald.presentation.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.util.Constants.BROWSE_DEBOUNCE
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.domain.usecase.BrowseNewsUseCase
import com.raj.morningherald.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val browseNewsUseCase: BrowseNewsUseCase
) : ViewModel() {

    private val _newsData = MutableStateFlow<UiState<List<Article>>>(UiState.Empty())
    val newsData: StateFlow<UiState<List<Article>>> = _newsData

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    init {
        browseNews()
    }

    fun browseNews(browseQuery: String) {
        _query.value = browseQuery
    }

    fun browseNews() {
        viewModelScope.launch {
            _query.debounce(BROWSE_DEBOUNCE)
                .filter { query ->
                    return@filter query.isNotEmpty() && query.length >= 2
                }.distinctUntilChanged()
                .flatMapLatest {
                    _newsData.value = UiState.Loading()
                    return@flatMapLatest browseNewsUseCase.invoke(it)
                }.catch { e ->
                    _newsData.value = UiState.Error(e.message.toString())
                }.flowOn(dispatcherProvider.io)
                .collect { article ->
                    _newsData.value = UiState.Success(article)
                }

        }
    }
}
