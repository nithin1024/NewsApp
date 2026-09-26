package com.newstelugu.app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.data.remote.dto.NewsArticleDto
import com.newstelugu.app.data.remote.dto.StockQuoteDto
import com.newstelugu.app.domain.model.NewsArticle
import com.newstelugu.app.domain.model.NewsSentiment
import com.newstelugu.app.domain.model.StockQuote
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val stocks: List<StockQuote> = emptyList(),
    val articles: List<NewsArticle> = emptyList(),
    val categories: List<String> = emptyList()
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChanged(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _uiState.value = SearchUiState()
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce search
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val stockDtos = NetworkClient.apiService.searchStocks(newQuery)
                val stocks = stockDtos.map { dtoToDomain(it) }

                val newsRes = NetworkClient.apiService.searchNews(newQuery)
                val articles = newsRes.articles.map { dtoToDomain(it) }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    stocks = stocks,
                    articles = articles
                )
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun dtoToDomain(dto: StockQuoteDto) = StockQuote(
        symbol = dto.symbol,
        name = dto.name,
        exchange = dto.exchange,
        instrumentKey = dto.instrumentKey,
        currentPrice = dto.currentPrice,
        change = dto.change,
        percentChange = dto.percentChange,
        openPrice = dto.openPrice,
        highPrice = dto.highPrice,
        lowPrice = dto.lowPrice,
        prevClose = dto.prevClose,
        volume = dto.volume,
        fiftyTwoWeekHigh = dto.fiftyTwoWeekHigh,
        fiftyTwoWeekLow = dto.fiftyTwoWeekLow,
        sector = dto.sector,
        lastUpdated = dto.lastUpdated
    )

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
