package com.example.petpal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.api.ApiClient
import com.example.petpal.api.ApiResponse
import com.example.petpal.api.ApiService
import com.example.petpal.models.LoginUserModel
import com.example.petpal.models.RegisterUserModel
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.apiService

    private val _authResponse = MutableLiveData<ApiResponse<String>>()
    val authResponse: LiveData<ApiResponse<String>> get() = _authResponse

    private val _loginResponse = MutableLiveData<ApiResponse<Map<String, Any>>>()
    val loginResponse: LiveData<ApiResponse<Map<String, Any>>> get() = _loginResponse

    // Register a user
    fun registerUser(registerUserModel: RegisterUserModel) {
        viewModelScope.launch {
            try {
                val response = apiService.registerUser(registerUserModel)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _authResponse.postValue(responseBody)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    _authResponse.postValue(ApiResponse("error", errorMessage, null))
                }
            } catch (e: Exception) {
                _authResponse.postValue(ApiResponse("error", "An error occurred: ${e.message}", null))
            }
        }
    }


    // Login a user
    fun loginUser(loginUserDto: LoginUserModel) {
        viewModelScope.launch {
            try {
                val response = apiService.loginUser(loginUserDto)
                processResponse(response, _loginResponse)
            } catch (e: Exception) {
                _loginResponse.postValue(ApiResponse("error", "An error occurred: ${e.message}", null))
            }
        }
    }

    // Register an admin


    // Generic response processor
    private fun <T> processResponse(
        response: Response<ApiResponse<T>>,
        liveData: MutableLiveData<ApiResponse<T>>
    ) {
        if (response.isSuccessful) {
            val body = response.body()

            // Check that we have a valid body and it's a success response
            if (body != null && body.status == "success") {
                // If data is a Map<String, Any>, process it correctly
                if (body.data is Map<*, *>) {
                    val mapData = body.data as Map<String, Any>

                    // Log for debugging
                    println("Response Data: $mapData")

                    // If token exists, handle it
                    val token = mapData["token"]?.toString()
                    if (token != null) {
                        // Token can be saved here using TokenManager (if needed)
                        // TokenManager(context).saveToken(token)
                    }
                } else {
                    // Handle the case where `data` is not a Map
                    println("Unexpected data format: ${body.data}")
                    _authResponse.postValue(ApiResponse("error", "Unexpected data format", null))
                }
            }
            liveData.postValue(body)
        } else {
            liveData.postValue(ApiResponse("error", "Request failed: ${response.message()}", null))
        }
    }
}
