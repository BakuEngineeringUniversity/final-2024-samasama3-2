package com.example.petpal.api

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null,
)
