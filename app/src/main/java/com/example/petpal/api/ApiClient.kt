package com.example.petpal.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:9090/"

    // Logging interceptor with manual control
    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            // Manually set logging level (BODY for debugging, NONE for production)
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Error handling interceptor
    private val errorInterceptor: Interceptor by lazy {
        Interceptor { chain ->
            try {
                chain.proceed(chain.request())
            } catch (e: Exception) {
                throw IOException("Network error: ${e.message}")
            }
        }
    }

    // OkHttpClient configuration
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add logging interceptor
            .addInterceptor(errorInterceptor)  // Add error handling interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()
    }

    // Gson configuration for JSON parsing
    private val gson by lazy {
        GsonBuilder()
            .setLenient() // Lenient parsing for development
            .create()
    }

    // Retrofit instance
    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    // API service instance
    val apiService: ApiService by lazy {
        retrofitInstance.create(ApiService::class.java)
    }


}
