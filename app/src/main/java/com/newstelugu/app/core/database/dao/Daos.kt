package com.newstelugu.app.core.database.dao

import androidx.room.*
import com.newstelugu.app.core.database.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_cached ORDER BY publishedAt DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_cached WHERE category = :category ORDER BY publishedAt DESC")
    fun getNewsByCategory(category: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_cached WHERE isBreaking = 1 ORDER BY publishedAt DESC")
    fun getBreakingNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_cached WHERE isSaved = 1 ORDER BY publishedAt DESC")
    fun getSavedNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_cached WHERE id = :id LIMIT 1")
    suspend fun getNewsById(id: String): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(articles: List<NewsEntity>)

    @Query("UPDATE news_cached SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)
}

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist_local ORDER BY addedAt DESC")
    fun getWatchlist(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistEntity)

    @Query("DELETE FROM watchlist_local WHERE symbol = :symbol")
    suspend fun removeFromWatchlist(symbol: String)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_local WHERE symbol = :symbol)")
    suspend fun isWatchlisted(symbol: String): Boolean
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications_cached ORDER BY id DESC")
    fun getNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications_cached SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)
}

@Dao
interface UserPreferenceDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1 LIMIT 1")
    fun getUserPreferences(): Flow<UserPreferenceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(prefs: UserPreferenceEntity)
}
