package com.newstelugu.app.data.remote

import com.newstelugu.app.data.remote.dto.*
import retrofit2.http.*

interface ApiService {

    @GET("health")
    suspend fun checkHealth(): HealthCheckResponseDto

    @GET("api/v1/home")
    suspend fun getHomeFeed(): HomeFeedResponseDto

    @GET("api/v1/news")
    suspend fun getNews(
        @Query("category") category: String? = null,
        @Query("query") query: String? = null,
        @Query("limit") limit: Int = 20
    ): NewsListResponseDto

    @GET("api/v1/news/breaking")
    suspend fun getBreakingNews(): List<NewsArticleDto>

    @GET("api/v1/news/category/{category_name}")
    suspend fun getNewsByCategory(
        @Path("category_name") categoryName: String,
        @Query("limit") limit: Int = 20
    ): NewsListResponseDto

    @GET("api/v1/news/search")
    suspend fun searchNews(@Query("q") query: String): NewsListResponseDto

    @GET("api/v1/news/{article_id}")
    suspend fun getArticleDetails(@Path("article_id") articleId: String): NewsArticleDto

    @GET("api/v1/markets/india")
    suspend fun getIndianMarkets(): List<MarketIndexDto>

    @GET("api/v1/markets/global")
    suspend fun getGlobalMarkets(): List<MarketIndexDto>

    @GET("api/v1/markets/overview")
    suspend fun getMarketsOverview(): MarketsOverviewResponseDto

    @GET("api/v1/stocks/search")
    suspend fun searchStocks(@Query("q") query: String): List<StockQuoteDto>

    @GET("api/v1/stocks/{symbol}")
    suspend fun getStockQuote(@Path("symbol") symbol: String): StockQuoteDto

    @GET("api/v1/stocks/{symbol}/history")
    suspend fun getStockHistory(
        @Path("symbol") symbol: String,
        @Query("period") period: String = "1M"
    ): StockHistoryResponseDto

    @GET("api/v1/stocks/{symbol}/indicators")
    suspend fun getTechnicalIndicators(@Path("symbol") symbol: String): TechnicalIndicatorsResponseDto

    @GET("api/v1/watchlist")
    suspend fun getWatchlist(): WatchlistResponseDto

    @POST("api/v1/watchlist")
    suspend fun addToWatchlist(@Body request: WatchlistAddRequestDto): WatchlistResponseDto

    @DELETE("api/v1/watchlist/{symbol}")
    suspend fun removeFromWatchlist(@Path("symbol") symbol: String): WatchlistResponseDto

    @GET("api/v1/alerts")
    suspend fun getAlerts(): AlertsResponseDto

    @POST("api/v1/alerts/price")
    suspend fun createPriceAlert(@Body request: PriceAlertCreateDto): PriceAlertDto

    @POST("api/v1/alerts/technical")
    suspend fun createTechnicalAlert(@Body request: TechnicalAlertCreateDto): TechnicalAlertDto

    @DELETE("api/v1/alerts/{alert_id}")
    suspend fun deleteAlert(@Path("alert_id") alertId: Int)

    @POST("api/v1/notifications/device-token")
    suspend fun registerDeviceToken(@Body request: DeviceTokenRegisterDto)

    @GET("api/v1/notifications")
    suspend fun getNotifications(): NotificationListResponseDto
}
