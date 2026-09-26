package com.newstelugu.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewsSentimentDto(
    @SerializedName("sentiment") val sentiment: String = "NEUTRAL",
    @SerializedName("score") val score: Float = 0f,
    @SerializedName("market_relevance") val marketRelevance: String = "MEDIUM",
    @SerializedName("related_company") val relatedCompany: String? = null,
    @SerializedName("related_symbol") val relatedSymbol: String? = null,
    @SerializedName("related_sector") val relatedSector: String? = null
)

data class NewsArticleDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("source_name") val sourceName: String = "NewsSource",
    @SerializedName("source_url") val sourceUrl: String,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("category") val category: String = "General",
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("is_breaking") val isBreaking: Boolean = false,
    @SerializedName("telugu_title") val teluguTitle: String? = null,
    @SerializedName("telugu_description") val teluguDescription: String? = null,
    @SerializedName("telugu_summary") val teluguSummary: String? = null,
    @SerializedName("sentiment") val sentiment: NewsSentimentDto? = null
)

data class NewsListResponseDto(
    @SerializedName("articles") val articles: List<NewsArticleDto>,
    @SerializedName("total") val total: Int,
    @SerializedName("category") val category: String? = null
)

data class MarketIndexDto(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String = "INDIA",
    @SerializedName("current_value") val currentValue: Double,
    @SerializedName("change") val change: Double,
    @SerializedName("percent_change") val percentChange: Double,
    @SerializedName("market_status") val marketStatus: String = "CLOSED",
    @SerializedName("is_available") val isAvailable: Boolean = true,
    @SerializedName("last_updated") val lastUpdated: String
)

data class MarketsOverviewResponseDto(
    @SerializedName("indian_markets") val indianMarkets: List<MarketIndexDto>,
    @SerializedName("global_markets") val globalMarkets: List<MarketIndexDto>,
    @SerializedName("last_updated") val lastUpdated: String
)

data class StockQuoteDto(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("exchange") val exchange: String = "NSE",
    @SerializedName("instrument_key") val instrumentKey: String? = null,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("change") val change: Double,
    @SerializedName("percent_change") val percentChange: Double,
    @SerializedName("open_price") val openPrice: Double,
    @SerializedName("high_price") val highPrice: Double,
    @SerializedName("low_price") val lowPrice: Double,
    @SerializedName("prev_close") val prevClose: Double,
    @SerializedName("volume") val volume: Long,
    @SerializedName("fifty_two_week_high") val fiftyTwoWeekHigh: Double,
    @SerializedName("fifty_two_week_low") val fiftyTwoWeekLow: Double,
    @SerializedName("sector") val sector: String? = null,
    @SerializedName("last_updated") val lastUpdated: String
)

data class CandlePointDto(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("open") val open: Double,
    @SerializedName("high") val high: Double,
    @SerializedName("low") val low: Double,
    @SerializedName("close") val close: Double,
    @SerializedName("volume") val volume: Long
)

data class StockHistoryResponseDto(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("period") val period: String,
    @SerializedName("candles") val candles: List<CandlePointDto>
)

data class IndicatorDetailDto(
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: Double,
    @SerializedName("status") val status: String,
    @SerializedName("description") val description: String
)

data class TechnicalIndicatorsResponseDto(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("sma_20") val sma20: IndicatorDetailDto,
    @SerializedName("sma_50") val sma50: IndicatorDetailDto,
    @SerializedName("sma_200") val sma200: IndicatorDetailDto,
    @SerializedName("ema_20") val ema20: IndicatorDetailDto,
    @SerializedName("ema_50") val ema50: IndicatorDetailDto,
    @SerializedName("rsi_14") val rsi14: IndicatorDetailDto,
    @SerializedName("macd") val macd: IndicatorDetailDto,
    @SerializedName("bollinger_bands") val bollingerBands: IndicatorDetailDto,
    @SerializedName("atr") val atr: IndicatorDetailDto,
    @SerializedName("obv") val obv: IndicatorDetailDto,
    @SerializedName("summary") val summary: String
)

data class WatchlistResponseDto(
    @SerializedName("items") val items: List<StockQuoteDto>
)

data class WatchlistAddRequestDto(
    @SerializedName("stock_symbol") val stockSymbol: String,
    @SerializedName("device_token") val deviceToken: String
)

data class PriceAlertCreateDto(
    @SerializedName("device_token") val deviceToken: String,
    @SerializedName("stock_symbol") val stockSymbol: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("target_price") val targetPrice: Double
)

data class TechnicalAlertCreateDto(
    @SerializedName("device_token") val deviceToken: String,
    @SerializedName("stock_symbol") val stockSymbol: String,
    @SerializedName("indicator") val indicator: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("value") val value: Double
)

data class PriceAlertDto(
    @SerializedName("id") val id: Int,
    @SerializedName("stock_symbol") val stockSymbol: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("target_price") val targetPrice: Double,
    @SerializedName("is_active") val isActive: Boolean
)

data class TechnicalAlertDto(
    @SerializedName("id") val id: Int,
    @SerializedName("stock_symbol") val stockSymbol: String,
    @SerializedName("indicator") val indicator: String,
    @SerializedName("condition") val condition: String,
    @SerializedName("value") val value: Double,
    @SerializedName("is_active") val isActive: Boolean
)

data class AlertsResponseDto(
    @SerializedName("price_alerts") val priceAlerts: List<PriceAlertDto>,
    @SerializedName("technical_alerts") val technicalAlerts: List<TechnicalAlertDto>
)

data class DeviceTokenRegisterDto(
    @SerializedName("token") val token: String,
    @SerializedName("device_id") val deviceId: String? = null,
    @SerializedName("platform") val platform: String = "android"
)

data class NotificationHistoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("article_id") val articleId: String? = null,
    @SerializedName("stock_symbol") val stockSymbol: String? = null,
    @SerializedName("is_read") val isRead: Boolean = false,
    @SerializedName("created_at") val createdAt: String
)

data class NotificationListResponseDto(
    @SerializedName("notifications") val notifications: List<NotificationHistoryDto>
)

data class HealthCheckResponseDto(
    @SerializedName("status") val status: String
)

data class HomeFeedResponseDto(
    @SerializedName("indian_markets") val indianMarkets: List<MarketIndexDto>,
    @SerializedName("global_markets") val globalMarkets: List<MarketIndexDto>,
    @SerializedName("breaking_news") val breakingNews: List<NewsArticleDto>,
    @SerializedName("latest_news") val latestNews: List<NewsArticleDto>,
    @SerializedName("category_news") val categoryNews: Map<String, List<NewsArticleDto>>
)
