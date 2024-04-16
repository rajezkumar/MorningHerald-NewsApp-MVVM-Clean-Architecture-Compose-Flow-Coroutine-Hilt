package com.raj.morningherald.domain.usecase

import com.raj.morningherald.domain.repository.NewsRepository
import javax.inject.Inject

class BrowseNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(query: String) = newsRepository.browseNews(query)
}
