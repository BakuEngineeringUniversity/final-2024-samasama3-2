package com.example.petpal.network

import com.example.petpal.dtos.LoginUserDto
import com.example.petpal.dtos.RegisterUserDto
import com.example.petpal.entities.UserEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/register")
    fun registerUser(@Body user: RegisterUserDto): Call<String>

    @POST("api/auth/login")
    fun loginUser(@Body credentials: LoginUserDto): Call<Map<String, String>>

    @GET("api/users")
    fun <UserEntity> getAllUsers(): Call<List<UserEntity>>

    @GET("api/users/{id}")
    fun getUserById(@Path("id") id: Long): Call<UserEntity>
}
