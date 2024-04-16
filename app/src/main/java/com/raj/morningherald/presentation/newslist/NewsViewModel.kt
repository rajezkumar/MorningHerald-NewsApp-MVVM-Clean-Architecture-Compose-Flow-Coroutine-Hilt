package com.raj.morningherald.presentation.newslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.raj.morningherald.core.util.Constants
import com.raj.morningherald.core.common.dispatcher.DispatcherProvider
import com.raj.morningherald.core.util.ValidationUtil.checkIfValidArgNews
import com.raj.morningherald.data.local.entity.ArticleEntity
import com.raj.morningherald.data.local.mapper.toArticle
import com.raj.morningherald.domain.model.Article
import com.raj.morningherald.domain.usecase.GetArticleHeadlinesUseCase
import com.raj.morningherald.domain.usecase.GetNewsBySourceUseCase
import com.raj.morningherald.presentation.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
    private val articlePager: Pager<Int, ArticleEntity>,
    private val getArticleHeadlinesUseCase: GetArticleHeadlinesUseCase,
    private val getNewsBySourceUseCase: GetNewsBySourceUseCase,
) : ViewModel() {

    private val _newsData = MutableStateFlow<UiState<List<Article>>>(UiState.Empty())
    val newsData: StateFlow<UiState<List<Article>>> = _newsData

    private val _newsDataPaging = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val newsDataPaging: StateFlow<PagingData<Article>> = _newsDataPaging

    init {
        fetchNews()
    }

    private fun fetchNews() {
        if (checkIfValidArgNews(savedStateHandle.get("source") as? String?)) {
            fetchNewsBySource(savedStateHandle.get("source"))
        } else {
            getArticlesPagination()
        }
    }

    fun getArticleHeadlines() {
        viewModelScope.launch {
            _newsData.emit(UiState.Loading())
            getArticleHeadlinesUseCase()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsData.value = UiState.Error(e.message.toString())
                }
                .map {
                    it.filter { article ->
                        article.title?.isNotEmpty() == true &&
                                article.urlToImage?.isNotEmpty() == true
                    }
                }.collect { article ->
                    _newsData.value = UiState.Success(article)
                }
        }
    }

    private fun fetchNewsBySource(source: String?) {
        viewModelScope.launch {
            _newsData.emit(UiState.Loading())
            getNewsBySourceUseCase(source ?: Constants.DEFAULT_SOURCE)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsData.value = UiState.Error(e.message.toString())
                }
                .map {
                    it.filter { article ->
                        article.title?.isNotEmpty() == true &&
                                article.urlToImage?.isNotEmpty() == true
                    }
                }.collect { article ->
                    _newsData.value = UiState.Success(article)
                }
        }
    }

    fun getArticlesPagination() {
        viewModelScope.launch {
            articlePager.flow.cachedIn(viewModelScope).map {
                it.map { articleEntity ->
                    articleEntity.toArticle()
                }.filter { article ->
                    article.title?.isNotEmpty() == true &&
                            article.urlToImage?.isNotEmpty() == true
                }
            }.collect {
                _newsDataPaging.value = it
            }
        }
    }
}
