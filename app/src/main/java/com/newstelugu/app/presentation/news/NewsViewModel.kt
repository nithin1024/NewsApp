package com.newstelugu.app.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.data.remote.dto.NewsArticleDto
import com.newstelugu.app.domain.model.NewsArticle
import com.newstelugu.app.domain.model.NewsSentiment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NewsListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val articles: List<NewsArticle> = emptyList(),
    val category: String = "Top Stories"
)

class NewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewsListUiState())
    val uiState: StateFlow<NewsListUiState> = _uiState.asStateFlow()

    fun loadNewsForCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, category = category)
            try {
                val response = NetworkClient.apiService.getNews(category = category)
                val items = response.articles.map { dtoToDomain(it) }
                _uiState.value = _uiState.value.copy(isLoading = false, articles = items)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    private fun dtoToDomain(dto: NewsArticleDto) = NewsArticle(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        content = dto.content,
        sourceName = dto.sourceName,
        sourceUrl = dto.sourceUrl,
        imageUrl = dto.imageUrl,
        category = dto.category,
        publishedAt = dto.publishedAt,
        isBreaking = dto.isBreaking,
        teluguTitle = dto.teluguTitle ?: dto.title,
        teluguDescription = dto.teluguDescription ?: dto.description,
        teluguSummary = dto.teluguSummary ?: dto.description,
        sentiment = dto.sentiment?.let {
            NewsSentiment(
                sentiment = it.sentiment,
                score = it.score,
                marketRelevance = it.marketRelevance,
                relatedCompany = it.relatedCompany,
                relatedSymbol = it.relatedSymbol,
                relatedSector = it.relatedSector
            )
        }
    )
}
