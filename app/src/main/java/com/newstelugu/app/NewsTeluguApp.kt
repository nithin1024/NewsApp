package com.newstelugu.app

import android.app.Application
import androidx.work.*
import com.newstelugu.app.core.worker.NewsSyncWorker
import java.util.concurrent.TimeUnit

class NewsTeluguApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Schedule periodic background sync for news updates
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<NewsSyncWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NewsTeluguPeriodicSync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}
