package com.newstelugu.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_cached")
data class NewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val sourceName: String,
    val sourceUrl: String,
    val imageUrl: String?,
    val category: String,
    val publishedAt: String,
    val isBreaking: Boolean,
    val teluguTitle: String?,
    val teluguDescription: String?,
    val teluguSummary: String?,
    val sentiment: String?,
    val sentimentScore: Float,
    val marketRelevance: String?,
    val relatedCompany: String?,
    val relatedSymbol: String?,
    val isSaved: Boolean = false
)

@Entity(tableName = "watchlist_local")
data class WatchlistEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val currentPrice: Double,
    val change: Double,
    val percentChange: Double,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications_cached")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val title: String,
    val message: String,
    val articleId: String?,
    val stockSymbol: String?,
    val isRead: Boolean = false,
    val createdAt: String
)

@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey val id: Int = 1,
    val language: String = "telugu",
    val theme: String = "system",
    val favoriteCategories: String = "Top Stories,Markets,Economy",
    val favoriteCompanies: String = "RELIANCE,INFY,TCS",
    val favoriteSectors: String = "Banking,IT",
    val notifyPositive: Boolean = true,
    val notifyNegative: Boolean = true,
    val notifyNeutral: Boolean = true,
    val notifyBreaking: Boolean = true,
    val notifyWatchlist: Boolean = true,
    val notifyMarket: Boolean = true,
    val notifyTechnical: Boolean = true,
    val notifyPrice: Boolean = true
)
