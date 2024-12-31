package com.example.petpal.api

import com.example.petpal.entities.UserEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("/api/users")
    fun getAllUsers(): Call<List<UserEntity>>

    @GET("/api/users/{id}")
    fun getUserById(@Path("id") id: Long): Call<UserEntity>
}
