package com.example.petpal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.api.ApiClient
import com.example.petpal.api.ApiResponse
import com.example.petpal.api.ApiService
import com.example.petpal.models.LoginUserDto
import com.example.petpal.models.RegisterAdminDto
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
                processResponse(response, _authResponse)
            } catch (e: Exception) {
                _authResponse.postValue(ApiResponse("error", "An error occurred: ${e.message}", null))
            }
        }
    }

    // Login a user
    fun loginUser(loginUserDto: LoginUserDto) {
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
    fun registerAdmin(registerAdminDto: RegisterAdminDto) {
        viewModelScope.launch {
            try {
                val response = apiService.registerAdmin(registerAdminDto)
                processResponse(response, _authResponse)
            } catch (e: Exception) {
                _authResponse.postValue(ApiResponse("error", "An error occurred: ${e.message}", null))
            }
        }
    }

    // Generic response processor
    private fun <T> processResponse(
        response: Response<ApiResponse<T>>,
        liveData: MutableLiveData<ApiResponse<T>>
    ) {
        if (response.isSuccessful) {
            liveData.postValue(response.body())
        } else {
            liveData.postValue(ApiResponse("error", "Request failed: ${response.message()}", null))
        }
    }
}
