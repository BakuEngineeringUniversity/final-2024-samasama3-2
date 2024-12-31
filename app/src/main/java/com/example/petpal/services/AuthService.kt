package com.example.petpal.services

import com.example.petpal.api.AuthApi
import com.example.petpal.api.RetrofitClient
import com.example.petpal.dtos.LoginUserDto
import com.example.petpal.dtos.RegisterUserDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private val authApi = RetrofitClient.instance.create(AuthApi::class.java)

    fun loginUser(loginUserDto: LoginUserDto, callback: (String?, String?) -> Unit) {
        authApi.loginUser(loginUserDto).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    callback(response.body()?.get("token"), null)
                } else {
                    callback(null, "Login failed")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    fun registerUser(registerUserDto: RegisterUserDto, callback: (String?, String?) -> Unit) {
        authApi.registerUser(registerUserDto).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Registration failed")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }
}
