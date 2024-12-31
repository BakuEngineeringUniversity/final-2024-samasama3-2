package com.example.petpal.api

import com.example.petpal.dtos.LoginUserDto
import com.example.petpal.dtos.RegisterUserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/register")
    fun registerUser(@Body registerUserDto: RegisterUserDto): Call<String>

    @POST("/api/auth/login")
    fun loginUser(@Body loginUserDto: LoginUserDto): Call<Map<String, String>>
}
