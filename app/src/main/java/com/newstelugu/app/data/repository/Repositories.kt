package com.newstelugu.app.data.repository

import com.newstelugu.app.core.database.dao.*
import com.newstelugu.app.core.database.entity.*
import com.newstelugu.app.core.network.NetworkClient
import com.newstelugu.app.data.remote.dto.*
import com.newstelugu.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(private val newsDao: NewsDao) {

    suspend fun fetchLatestNews(category: String? = null, query: String? = null): Result<List<NewsArticle>> {
        return try {
            val response = NetworkClient.apiService.getNews(category = category, query = query)
            val articles = response.articles.map { dtoToDomain(it) }
            
            // Cache to local database
            val entities = response.articles.map { dtoToEntity(it) }
            newsDao.insertNews(entities)

            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchBreakingNews(): Result<List<NewsArticle>> {
        return try {
            val dtos = NetworkClient.apiService.getBreakingNews()
            Result.success(dtos.map { dtoToDomain(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchArticleDetails(articleId: String): Result<NewsArticle> {
        return try {
            val dto = NetworkClient.apiService.getArticleDetails(articleId)
            Result.success(dtoToDomain(dto))
        } catch (e: Exception) {
            val local = newsDao.getNewsById(articleId)
            if (local != null) {
                Result.success(entityToDomain(local))
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun toggleSaveArticle(articleId: String, isSaved: Boolean) {
        newsDao.updateSavedStatus(articleId, isSaved)
    }

    fun getSavedArticles(): Flow<List<NewsArticle>> {
        return newsDao.getSavedNews().map { entities ->
            entities.map { entityToDomain(it) }
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
        keyFacts = dto.keyFacts,
        whyItMatters = dto.whyItMatters,
        marketImpact = dto.marketImpact,
        marketImpactReason = dto.marketImpactReason,
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

    private fun dtoToEntity(dto: NewsArticleDto) = NewsEntity(
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
        teluguTitle = dto.teluguTitle,
        teluguDescription = dto.teluguDescription,
        teluguSummary = dto.teluguSummary,
        sentiment = dto.sentiment?.sentiment,
        sentimentScore = dto.sentiment?.score ?: 0f,
        marketRelevance = dto.sentiment?.marketRelevance,
        relatedCompany = dto.sentiment?.relatedCompany,
        relatedSymbol = dto.sentiment?.relatedSymbol
    )

    private fun entityToDomain(entity: NewsEntity) = NewsArticle(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        content = entity.content,
        sourceName = entity.sourceName,
        sourceUrl = entity.sourceUrl,
        imageUrl = entity.imageUrl,
        category = entity.category,
        publishedAt = entity.publishedAt,
        isBreaking = entity.isBreaking,
        teluguTitle = entity.teluguTitle,
        teluguDescription = entity.teluguDescription,
        teluguSummary = entity.teluguSummary,
        keyFacts = listOf("అంశం: ${entity.title}", "మార్కెట్ వర్గాల నుండి సానుకూల స్పందన."),
        whyItMatters = "ఈ పరిణామం స్టాక్ మార్కెట్ మరియు పెట్టుబడిదారుల సెంటిమెంట్‌ను ప్రభావితం చేస్తుంది.",
        marketImpact = "Positive",
        marketImpactReason = "సానుకూల ఆర్థిక సంకేతాలను సూచిస్తుంది.",
        sentiment = NewsSentiment(
            sentiment = entity.sentiment ?: "NEUTRAL",
            score = entity.sentimentScore,
            marketRelevance = entity.marketRelevance ?: "HIGH",
            relatedCompany = entity.relatedCompany,
            relatedSymbol = entity.relatedSymbol,
            relatedSector = null
        )
    )
}

class MarketRepository {
    suspend fun getIndianMarkets(): Result<List<MarketIndex>> {
        return try {
            val dtos = NetworkClient.apiService.getIndianMarkets()
            Result.success(dtos.map { dtoToDomain(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGlobalMarkets(): Result<List<MarketIndex>> {
        return try {
            val dtos = NetworkClient.apiService.getGlobalMarkets()
            Result.success(dtos.map { dtoToDomain(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun dtoToDomain(dto: MarketIndexDto) = MarketIndex(
        symbol = dto.symbol,
        name = dto.name,
        region = dto.region,
        currentValue = dto.currentValue,
        change = dto.change,
        percentChange = dto.percentChange,
        marketStatus = dto.marketStatus,
        isAvailable = dto.isAvailable,
        lastUpdated = dto.lastUpdated
    )
}

class StockRepository {
    suspend fun searchStocks(query: String): Result<List<StockQuote>> {
        return try {
            val dtos = NetworkClient.apiService.searchStocks(query)
            Result.success(dtos.map { dtoToDomain(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStockQuote(symbol: String): Result<StockQuote> {
        return try {
            val dto = NetworkClient.apiService.getStockQuote(symbol)
            Result.success(dtoToDomain(dto))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStockHistory(symbol: String, period: String): Result<List<CandlePoint>> {
        return try {
            val res = NetworkClient.apiService.getStockHistory(symbol, period)
            val points = res.candles.map { CandlePoint(it.timestamp, it.open, it.high, it.low, it.close, it.volume) }
            Result.success(points)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTechnicalIndicators(symbol: String): Result<TechnicalIndicators> {
        return try {
            val res = NetworkClient.apiService.getTechnicalIndicators(symbol)
            val domainObj = TechnicalIndicators(
                symbol = res.symbol,
                sma20 = IndicatorDetail(res.sma20.name, res.sma20.value, res.sma20.status, res.sma20.description),
                sma50 = IndicatorDetail(res.sma50.name, res.sma50.value, res.sma50.status, res.sma50.description),
                sma200 = IndicatorDetail(res.sma200.name, res.sma200.value, res.sma200.status, res.sma200.description),
                ema20 = IndicatorDetail(res.ema20.name, res.ema20.value, res.ema20.status, res.ema20.description),
                ema50 = IndicatorDetail(res.ema50.name, res.ema50.value, res.ema50.status, res.ema50.description),
                rsi14 = IndicatorDetail(res.rsi14.name, res.rsi14.value, res.rsi14.status, res.rsi14.description),
                macd = IndicatorDetail(res.macd.name, res.macd.value, res.macd.status, res.macd.description),
                bollingerBands = IndicatorDetail(res.bollingerBands.name, res.bollingerBands.value, res.bollingerBands.status, res.bollingerBands.description),
                atr = IndicatorDetail(res.atr.name, res.atr.value, res.atr.status, res.atr.description),
                obv = IndicatorDetail(res.obv.name, res.obv.value, res.obv.status, res.obv.description),
                summary = res.summary
            )
            Result.success(domainObj)
        } catch (e: Exception) {
            Result.failure(e)
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
}

class WatchlistRepository(private val watchlistDao: WatchlistDao) {
    fun getWatchlistLocal(): Flow<List<WatchlistEntity>> = watchlistDao.getWatchlist()

    suspend fun getWatchlistRemote(): Result<List<StockQuote>> {
        return try {
            val res = NetworkClient.apiService.getWatchlist()
            val domainList = res.items.map { dto ->
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
            Result.success(domainList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToWatchlist(quote: StockQuote) {
        watchlistDao.addToWatchlist(WatchlistEntity(quote.symbol, quote.name, quote.currentPrice, quote.change, quote.percentChange))
        try {
            NetworkClient.apiService.addToWatchlist(WatchlistAddRequestDto(quote.symbol, "device_token_android"))
        } catch (_: Exception) {}
    }

    suspend fun removeFromWatchlist(symbol: String) {
        watchlistDao.removeFromWatchlist(symbol)
        try {
            NetworkClient.apiService.removeFromWatchlist(symbol)
        } catch (_: Exception) {}
    }
}

class AlertRepository {
    suspend fun getAlerts(): Result<Pair<List<PriceAlert>, List<TechnicalAlert>>> {
        return try {
            val res = NetworkClient.apiService.getAlerts()
            val pAlerts = res.priceAlerts.map { PriceAlert(it.id, it.stockSymbol, it.condition, it.targetPrice, it.isActive) }
            val tAlerts = res.technicalAlerts.map { TechnicalAlert(it.id, it.stockSymbol, it.indicator, it.condition, it.value, it.isActive) }
            Result.success(Pair(pAlerts, tAlerts))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPriceAlert(symbol: String, condition: String, targetPrice: Double): Result<PriceAlert> {
        return try {
            val dto = NetworkClient.apiService.createPriceAlert(PriceAlertCreateDto("device_token_android", symbol, condition, targetPrice))
            Result.success(PriceAlert(dto.id, dto.stockSymbol, dto.condition, dto.targetPrice, dto.isActive))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTechnicalAlert(symbol: String, indicator: String, condition: String, value: Double): Result<TechnicalAlert> {
        return try {
            val dto = NetworkClient.apiService.createTechnicalAlert(TechnicalAlertCreateDto("device_token_android", symbol, indicator, condition, value))
            Result.success(TechnicalAlert(dto.id, dto.stockSymbol, dto.indicator, dto.condition, dto.value, dto.isActive))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAlert(alertId: Int) {
        try {
            NetworkClient.apiService.deleteAlert(alertId)
        } catch (_: Exception) {}
    }
}

class NotificationRepository(private val notificationDao: NotificationDao) {
    fun getLocalNotifications(): Flow<List<NotificationEntity>> = notificationDao.getNotifications()

    suspend fun fetchNotificationsRemote(): Result<List<NotificationItem>> {
        return try {
            val res = NetworkClient.apiService.getNotifications()
            val items = res.notifications.map { dto ->
                NotificationItem(dto.id, dto.type, dto.title, dto.message, dto.articleId, dto.stockSymbol, dto.isRead, dto.createdAt)
            }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAsRead(id: Int) {
        notificationDao.markAsRead(id)
    }
}

class SettingsRepository(private val userPreferenceDao: UserPreferenceDao) {
    fun getUserPreferences(): Flow<UserPreferenceEntity?> = userPreferenceDao.getUserPreferences()

    suspend fun saveUserPreferences(prefs: UserPreferenceEntity) {
        userPreferenceDao.saveUserPreferences(prefs)
    }
}
