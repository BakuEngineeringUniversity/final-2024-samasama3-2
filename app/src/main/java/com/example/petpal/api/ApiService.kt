package com.example.petpal.api

import com.example.petpal.dtos.LoginUserDto
import com.example.petpal.dtos.RegisterUserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/auth/register")
    suspend fun registerUser(
        @Body registerUserDto: RegisterUserDto
    ): Response<ApiResponse<String>>

    @POST("/api/auth/login")
    suspend fun loginUser(
        @Body loginUserDto: LoginUserDto
    ): Response<ApiResponse<Map<String, Any>>>

}
