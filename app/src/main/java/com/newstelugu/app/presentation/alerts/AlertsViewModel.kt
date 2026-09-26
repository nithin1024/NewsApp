package com.newstelugu.app.presentation.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.data.remote.dto.PriceAlertCreateDto
import com.newstelugu.app.data.remote.dto.TechnicalAlertCreateDto
import com.newstelugu.app.domain.model.PriceAlert
import com.newstelugu.app.domain.model.TechnicalAlert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlertsUiState(
    val isLoading: Boolean = true,
    val priceAlerts: List<PriceAlert> = emptyList(),
    val technicalAlerts: List<TechnicalAlert> = emptyList(),
    val error: String? = null
)

class AlertsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AlertsUiState())
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        loadAlerts()
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val res = NetworkClient.apiService.getAlerts()
                val pList = res.priceAlerts.map { PriceAlert(it.id, it.stockSymbol, it.condition, it.targetPrice, it.isActive) }
                val tList = res.technicalAlerts.map { TechnicalAlert(it.id, it.stockSymbol, it.indicator, it.condition, it.value, it.isActive) }
                _uiState.value = AlertsUiState(isLoading = false, priceAlerts = pList, technicalAlerts = tList)
            } catch (e: Exception) {
                _uiState.value = AlertsUiState(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun addPriceAlert(symbol: String, condition: String, price: Double) {
        viewModelScope.launch {
            try {
                NetworkClient.apiService.createPriceAlert(PriceAlertCreateDto("device_token_android", symbol, condition, price))
                loadAlerts()
            } catch (_: Exception) {}
        }
    }

    fun addTechnicalAlert(symbol: String, indicator: String, condition: String, value: Double) {
        viewModelScope.launch {
            try {
                NetworkClient.apiService.createTechnicalAlert(TechnicalAlertCreateDto("device_token_android", symbol, indicator, condition, value))
                loadAlerts()
            } catch (_: Exception) {}
        }
    }

    fun deleteAlert(id: Int) {
        viewModelScope.launch {
            try {
                NetworkClient.apiService.deleteAlert(id)
                loadAlerts()
            } catch (_: Exception) {}
        }
    }
}
