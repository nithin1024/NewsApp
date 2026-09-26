package com.newstelugu.app.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.domain.model.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val notifications: List<NotificationItem> = emptyList(),
    val filterType: String = "ALL",
    val notifyPositive: Boolean = true,
    val notifyNegative: Boolean = true,
    val notifyNeutral: Boolean = true,
    val notifyBreaking: Boolean = true,
    val notifyWatchlist: Boolean = true,
    val notifyMarket: Boolean = true,
    val notifyTechnical: Boolean = true,
    val notifyPrice: Boolean = true,
    val error: String? = null
)

class NotificationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val res = NetworkClient.apiService.getNotifications()
                val items = res.notifications.map {
                    NotificationItem(it.id, it.type, it.title, it.message, it.articleId, it.stockSymbol, it.isRead, it.createdAt)
                }
                _uiState.value = _uiState.value.copy(isLoading = false, notifications = items)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun setFilterType(type: String) {
        _uiState.value = _uiState.value.copy(filterType = type)
    }

    fun toggleSetting(settingName: String, enabled: Boolean) {
        when (settingName) {
            "POSITIVE" -> _uiState.value = _uiState.value.copy(notifyPositive = enabled)
            "NEGATIVE" -> _uiState.value = _uiState.value.copy(notifyNegative = enabled)
            "NEUTRAL" -> _uiState.value = _uiState.value.copy(notifyNeutral = enabled)
            "BREAKING" -> _uiState.value = _uiState.value.copy(notifyBreaking = enabled)
            "WATCHLIST" -> _uiState.value = _uiState.value.copy(notifyWatchlist = enabled)
            "MARKET" -> _uiState.value = _uiState.value.copy(notifyMarket = enabled)
            "TECHNICAL" -> _uiState.value = _uiState.value.copy(notifyTechnical = enabled)
            "PRICE" -> _uiState.value = _uiState.value.copy(notifyPrice = enabled)
        }
    }
}
