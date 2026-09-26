package com.newstelugu.app.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.newstelugu.app.core.database.AppDatabase
import com.newstelugu.app.core.database.entity.NewsEntity
import com.newstelugu.app.core.network.NetworkClient

class NewsSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val response = NetworkClient.apiService.getNews(limit = 20)
            val db = AppDatabase.getDatabase(applicationContext)
            val entities = response.articles.map { dto ->
                NewsEntity(
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
            }
            db.newsDao().insertNews(entities)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
