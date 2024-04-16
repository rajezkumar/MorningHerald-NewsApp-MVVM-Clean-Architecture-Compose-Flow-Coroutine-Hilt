package com.raj.morningherald.domain.usecase

import com.raj.morningherald.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsBySourceUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(source: String) = newsRepository.getNewsBySource(source)
}
