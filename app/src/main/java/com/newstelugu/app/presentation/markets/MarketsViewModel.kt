package com.newstelugu.app.presentation.markets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.domain.model.MarketIndex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MarketsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val indianMarkets: List<MarketIndex> = emptyList(),
    val globalMarkets: List<MarketIndex> = emptyList()
)

class MarketsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MarketsUiState())
    val uiState: StateFlow<MarketsUiState> = _uiState.asStateFlow()

    init {
        loadMarkets()
    }

    fun loadMarkets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val overview = NetworkClient.apiService.getMarketsOverview()
                val indian = overview.indianMarkets.map {
                    MarketIndex(it.symbol, it.name, it.region, it.currentValue, it.change, it.percentChange, it.marketStatus, it.isAvailable, it.lastUpdated)
                }
                val globals = overview.globalMarkets.map {
                    MarketIndex(it.symbol, it.name, it.region, it.currentValue, it.change, it.percentChange, it.marketStatus, it.isAvailable, it.lastUpdated)
                }
                _uiState.value = MarketsUiState(isLoading = false, indianMarkets = indian, globalMarkets = globals)
            } catch (e: Exception) {
                _uiState.value = MarketsUiState(isLoading = false, error = "Failed to load market data: ${e.localizedMessage}")
            }
        }
    }
}
