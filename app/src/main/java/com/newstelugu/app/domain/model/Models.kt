package com.newstelugu.app.domain.model

data class NewsArticle(
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val sourceName: String,
    val sourceUrl: String,
    val imageUrl: String?,
    val category: String,
    val publishedAt: String,
    val isBreaking: Boolean = false,
    val teluguTitle: String?,
    val teluguDescription: String?,
    val teluguSummary: String?,
    val sentiment: NewsSentiment?
)

data class NewsSentiment(
    val sentiment: String, // POSITIVE, NEGATIVE, NEUTRAL
    val score: Float,
    val marketRelevance: String, // HIGH, MEDIUM, LOW
    val relatedCompany: String?,
    val relatedSymbol: String?,
    val relatedSector: String?
)

data class MarketIndex(
    val symbol: String,
    val name: String,
    val region: String, // INDIA or GLOBAL
    val currentValue: Double,
    val change: Double,
    val percentChange: Double,
    val marketStatus: String,
    val isAvailable: Boolean = true,
    val lastUpdated: String
)

data class StockQuote(
    val symbol: String,
    val name: String,
    val exchange: String,
    val instrumentKey: String?,
    val currentPrice: Double,
    val change: Double,
    val percentChange: Double,
    val openPrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val prevClose: Double,
    val volume: Long,
    val fiftyTwoWeekHigh: Double,
    val fiftyTwoWeekLow: Double,
    val sector: String?,
    val lastUpdated: String
)

data class CandlePoint(
    val timestamp: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

data class IndicatorDetail(
    val name: String,
    val value: Double,
    val status: String,
    val description: String
)

data class TechnicalIndicators(
    val symbol: String,
    val sma20: IndicatorDetail,
    val sma50: IndicatorDetail,
    val sma200: IndicatorDetail,
    val ema20: IndicatorDetail,
    val ema50: IndicatorDetail,
    val rsi14: IndicatorDetail,
    val macd: IndicatorDetail,
    val bollingerBands: IndicatorDetail,
    val atr: IndicatorDetail,
    val obv: IndicatorDetail,
    val summary: String
)

data class PriceAlert(
    val id: Int,
    val stockSymbol: String,
    val condition: String,
    val targetPrice: Double,
    val isActive: Boolean
)

data class TechnicalAlert(
    val id: Int,
    val stockSymbol: String,
    val indicator: String,
    val condition: String,
    val value: Double,
    val isActive: Boolean
)

data class NotificationItem(
    val id: Int,
    val type: String, // POSITIVE, NEGATIVE, NEUTRAL, BREAKING, PRICE, TECHNICAL, MARKET, WATCHLIST
    val title: String,
    val message: String,
    val articleId: String?,
    val stockSymbol: String?,
    val isRead: Boolean,
    val createdAt: String
)

data class UserPreferences(
    val language: String = "telugu", // telugu or english
    val theme: String = "system", // light, dark, system
    val favoriteCategories: List<String> = listOf("Top Stories", "Markets", "Economy"),
    val favoriteCompanies: List<String> = listOf("RELIANCE", "INFY", "TCS"),
    val favoriteSectors: List<String> = listOf("Banking", "IT"),
    val notifyPositive: Boolean = true,
    val notifyNegative: Boolean = true,
    val notifyNeutral: Boolean = true,
    val notifyBreaking: Boolean = true,
    val notifyWatchlist: Boolean = true,
    val notifyMarket: Boolean = true,
    val notifyTechnical: Boolean = true,
    val notifyPrice: Boolean = true
)
