package com.example.petpal.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Base URL for the backend API
    private const val BASE_URL = "http://10.0.2.2:9090/"

    // OkHttp client with logging for debugging
    private val httpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add logging for requests and responses
            .build()
    }

    // Retrofit instance
    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Define the base URL
            .addConverterFactory(GsonConverterFactory.create()) // Handle JSON serialization/deserialization
            .client(httpClient) // Use the custom OkHttp client
            .build()
    }

    // API service instance
    val apiService: ApiService by lazy {
        retrofitInstance.create(ApiService::class.java)
    }
}
