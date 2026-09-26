package com.newstelugu.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.data.remote.dto.NewsArticleDto
import com.newstelugu.app.domain.model.MarketIndex
import com.newstelugu.app.domain.model.NewsArticle
import com.newstelugu.app.domain.model.NewsSentiment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val indianMarkets: List<MarketIndex> = emptyList(),
    val globalMarkets: List<MarketIndex> = emptyList(),
    val breakingNews: List<NewsArticle> = emptyList(),
    val latestNews: List<NewsArticle> = emptyList(),
    val categoryNews: Map<String, List<NewsArticle>> = emptyMap()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val feed = NetworkClient.apiService.getHomeFeed()
                val indian = feed.indianMarkets.map {
                    MarketIndex(it.symbol, it.name, it.region, it.currentValue, it.change, it.percentChange, it.marketStatus, it.isAvailable, it.lastUpdated)
                }
                val globals = feed.globalMarkets.map {
                    MarketIndex(it.symbol, it.name, it.region, it.currentValue, it.change, it.percentChange, it.marketStatus, it.isAvailable, it.lastUpdated)
                }
                val breaking = feed.breakingNews.map { dtoToDomain(it) }
                val latest = feed.latestNews.map { dtoToDomain(it) }

                val catMap = mutableMapOf<String, List<NewsArticle>>()
                feed.categoryNews.forEach { (cat, dtos) ->
                    catMap[cat] = dtos.map { dtoToDomain(it) }
                }

                _uiState.value = HomeUiState(
                    isLoading = false,
                    indianMarkets = indian,
                    globalMarkets = globals,
                    breakingNews = breaking,
                    latestNews = latest,
                    categoryNews = catMap
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = "Failed to load feed: ${e.localizedMessage ?: "Network error"}"
                )
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
