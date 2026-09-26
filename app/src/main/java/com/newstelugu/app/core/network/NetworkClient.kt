package com.newstelugu.app.core.network

import com.newstelugu.app.BuildConfig
import com.newstelugu.app.data.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {

    private var baseUrl: String = BuildConfig.DEFAULT_BASE_URL

    fun setBaseUrl(url: String) {
        if (url.isNotBlank()) {
            baseUrl = if (url.endsWith("/")) url else "$url/"
            apiServiceInstance = createApiService()
        }
    }

    fun getBaseUrl(): String = baseUrl

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private var apiServiceInstance: ApiService = createApiService()

    val apiService: ApiService
        get() = apiServiceInstance
}
