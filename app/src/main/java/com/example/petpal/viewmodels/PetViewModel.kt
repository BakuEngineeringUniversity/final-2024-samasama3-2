package com.example.petpal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpal.api.ApiClient
import com.example.petpal.api.ApiResponse
import com.example.petpal.models.PetModel
import com.example.petpal.models.PetUpdateModel
import kotlinx.coroutines.launch
import retrofit2.Response

class PetViewModel : ViewModel() {
    private val _currentUserId = MutableLiveData<Long>()
    val currentUserId: LiveData<Long> get() = _currentUserId

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    private val _pets = MutableLiveData<List<PetModel>>()
    val pets: LiveData<List<PetModel>> get() = _pets

    private val _petDetails = MutableLiveData<PetModel?>()
    val petDetails: LiveData<PetModel?> get() = _petDetails

    private val _updateResponse = MutableLiveData<Boolean>()
    val updateResponse: LiveData<Boolean> get() = _updateResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Fetch pet details by ID
    fun fetchPetDetails(petId: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPetById(petId)

                // Log the response for debugging
                println("API Response: ${response.body()}")

                if (response.isSuccessful) {
                    val petModel = response.body()?.data
                    if (petModel != null) {
                        _petDetails.value = petModel
                    } else {
                        _error.value = "No pet data found"
                    }
                } else {
                    _error.value = "Failed to fetch pet details: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    fun fetchPetsByUserId(userId: Long) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPetsByUserId(userId)
                if (response.isSuccessful && response.body() != null) {
                    _pets.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Failed to fetch pets: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error fetching pets: ${e.message}"
            }
        }
    }

    fun deletePet(petId: Long) {
        viewModelScope.launch {
            try {
                val response: Response<ApiResponse<String>> = ApiClient.apiService.deletePet(petId)
                if (response.isSuccessful) {
                    _deleteResult.postValue(true)
                } else {
                    _deleteResult.postValue(false)
                    _error.postValue(response.errorBody()?.string() ?: "Unknown error")
                }
            } catch (e: Exception) {
                _error.postValue("Failed to delete pet: ${e.message}")
            }
        }
    }

    fun fetchCurrentUserId() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getCurrentUserId()
                if (response.isSuccessful) {
                    val userId = response.body()?.data
                    if (userId != null) {
                        _currentUserId.postValue(userId)
                    } else {
                        _error.postValue("Failed to fetch user ID: Data is null")
                    }
                } else {
                    _error.postValue("Failed to fetch user ID: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error fetching user ID: ${e.message}")
            }
        }
    }

    // Update pet details
    fun updatePetDetails(petId: Long, petUpdateModel: PetUpdateModel) {
        Log.d("PetViewModel", "Updating pet with ID: $petId and data: $petUpdateModel")

        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.updatePet(petId, petUpdateModel)
                Log.d("PetViewModel", "Update response: ${response.body()}")
                if (response.isSuccessful) {
                    _updateResponse.value = true
                } else {
                    _updateResponse.value = false
                    _error.value = "Failed to update pet: ${response.message()}"
                    Log.e("PetViewModel", "Error updating pet: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _updateResponse.value = false
                _error.value = "Error: ${e.message}"
                Log.e("PetViewModel", "Exception during update: ${e.message}")
            }
        }
    }



    // Clear error messages
    fun clearError() {
        _error.value = null
    }
}
