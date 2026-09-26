package com.newstelugu.app.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.domain.model.CandlePoint
import com.newstelugu.app.domain.model.IndicatorDetail
import com.newstelugu.app.domain.model.StockQuote
import com.newstelugu.app.domain.model.TechnicalIndicators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StockDetailsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val quote: StockQuote? = null,
    val period: String = "1M",
    val candles: List<CandlePoint> = emptyList(),
    val indicators: TechnicalIndicators? = null
)

class StockViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StockDetailsUiState())
    val uiState: StateFlow<StockDetailsUiState> = _uiState.asStateFlow()

    fun loadStockDetails(symbol: String, period: String = "1M") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, period = period)
            try {
                val qDto = NetworkClient.apiService.getStockQuote(symbol)
                val quote = StockQuote(
                    symbol = qDto.symbol,
                    name = qDto.name,
                    exchange = qDto.exchange,
                    instrumentKey = qDto.instrumentKey,
                    currentPrice = qDto.currentPrice,
                    change = qDto.change,
                    percentChange = qDto.percentChange,
                    openPrice = qDto.openPrice,
                    highPrice = qDto.highPrice,
                    lowPrice = qDto.lowPrice,
                    prevClose = qDto.prevClose,
                    volume = qDto.volume,
                    fiftyTwoWeekHigh = qDto.fiftyTwoWeekHigh,
                    fiftyTwoWeekLow = qDto.fiftyTwoWeekLow,
                    sector = qDto.sector,
                    lastUpdated = qDto.lastUpdated
                )

                val hRes = NetworkClient.apiService.getStockHistory(symbol, period)
                val candles = hRes.candles.map { CandlePoint(it.timestamp, it.open, it.high, it.low, it.close, it.volume) }

                val indRes = NetworkClient.apiService.getTechnicalIndicators(symbol)
                val indicators = TechnicalIndicators(
                    symbol = indRes.symbol,
                    sma20 = IndicatorDetail(indRes.sma20.name, indRes.sma20.value, indRes.sma20.status, indRes.sma20.description),
                    sma50 = IndicatorDetail(indRes.sma50.name, indRes.sma50.value, indRes.sma50.status, indRes.sma50.description),
                    sma200 = IndicatorDetail(indRes.sma200.name, indRes.sma200.value, indRes.sma200.status, indRes.sma200.description),
                    ema20 = IndicatorDetail(indRes.ema20.name, indRes.ema20.value, indRes.ema20.status, indRes.ema20.description),
                    ema50 = IndicatorDetail(indRes.ema50.name, indRes.ema50.value, indRes.ema50.status, indRes.ema50.description),
                    rsi14 = IndicatorDetail(indRes.rsi14.name, indRes.rsi14.value, indRes.rsi14.status, indRes.rsi14.description),
                    macd = IndicatorDetail(indRes.macd.name, indRes.macd.value, indRes.macd.status, indRes.macd.description),
                    bollingerBands = IndicatorDetail(indRes.bollingerBands.name, indRes.bollingerBands.value, indRes.bollingerBands.status, indRes.bollingerBands.description),
                    atr = IndicatorDetail(indRes.atr.name, indRes.atr.value, indRes.atr.status, indRes.atr.description),
                    obv = IndicatorDetail(indRes.obv.name, indRes.obv.value, indRes.obv.status, indRes.obv.description),
                    summary = indRes.summary
                )

                _uiState.value = StockDetailsUiState(
                    isLoading = false,
                    quote = quote,
                    period = period,
                    candles = candles,
                    indicators = indicators
                )
            } catch (e: Exception) {
                _uiState.value = StockDetailsUiState(isLoading = false, error = e.localizedMessage ?: "Network error")
            }
        }
    }
}
