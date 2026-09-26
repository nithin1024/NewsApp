package com.newstelugu.app.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.domain.model.StockQuote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WatchlistUiState(
    val isLoading: Boolean = true,
    val items: List<StockQuote> = emptyList(),
    val error: String? = null
)

class WatchlistViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val res = NetworkClient.apiService.getWatchlist()
                val quotes = res.items.map { dto ->
                    StockQuote(
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
                }
                _uiState.value = WatchlistUiState(isLoading = false, items = quotes)
            } catch (e: Exception) {
                _uiState.value = WatchlistUiState(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun removeFromWatchlist(symbol: String) {
        viewModelScope.launch {
            try {
                NetworkClient.apiService.removeFromWatchlist(symbol)
                loadWatchlist()
            } catch (_: Exception) {}
        }
    }
}
