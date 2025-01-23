package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.petpal.R
import com.example.petpal.models.LoginUserModel
import com.example.petpal.utils.TokenManager
import com.example.petpal.api.ApiClient
import com.example.petpal.utils.decodeJwt
import com.example.petpal.viewmodels.AuthViewModel
import com.example.petpal.viewmodels.PetViewModel

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val petViewModel: PetViewModel by viewModels() // Added PetViewModel for fetching pet data
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenManager = TokenManager(this)

        // UI elements
        val usernameEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.loginPasswordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.loginRegisterLabel)

        // Navigate to UserRegisterActivity on Register TextView click
        registerTextView.setOnClickListener {
            val intent = Intent(this, UserRegisterActivity::class.java)
            startActivity(intent)
        }

        // Observe login response
        authViewModel.loginResponse.observe(this, Observer { response ->
            if (response.status == "success") {
                val token = response.data?.get("token") as? String

                if (token != null) {
                    Log.d("LoginActivity", "Login successful, token: $token")

                    // Decode userId from JWT token
                    val userId = decodeJwt(token)
                    if (userId != null) {
                        Log.d("LoginActivity", "Decoded User ID: $userId")
                        tokenManager.saveToken(token) // Save the token
                        ApiClient.setAuthToken(token) // Set the token in ApiClient

                        // Navigate to HomePageActivity
                        navigateToHomePage(userId)
                    } else {
                        Log.e("LoginActivity", "Failed to decode userId from token")
                        showToast("Login failed: Could not extract userId.")
                    }
                } else {
                    Log.e("LoginActivity", "Token not found in response")
                    showToast("Login failed: Token not found.")
                }
            } else {
                Log.e("LoginActivity", "Login failed: ${response.message}")
                showToast(response.message ?: "Login failed!")
            }
        })



        // Handle login button click
        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()) {
                val loginUserDto = LoginUserModel(email, password)
                Log.d("LoginActivity", "Logging in with email: $email")
                authViewModel.loginUser(loginUserDto)
            } else {
                Log.e("LoginActivity", "Email or password is empty")
                showToast("Please fill in all fields")
            }
        }
    }

    private fun fetchAndNavigateToHome(userId: Long) {
        Log.d("LoginActivity", "Fetching pets for userId: $userId")

        petViewModel.fetchPetsByUserId(userId) // Fetch pets using the userId

        // Observe fetched pets
        petViewModel.pets.observe(this, Observer { pets ->
            if (pets != null && pets.isNotEmpty()) {
                val firstPetId = pets[0].id // Use the first pet's ID
                Log.d("LoginActivity", "Fetched first Pet ID: $firstPetId")
                navigateToHomePage(firstPetId)
            } else {
                Log.e("LoginActivity", "No pets found for the user.")
                showToast("No pets found. Please create one to proceed.")
            }
        })

        // Observe errors during fetching
        petViewModel.error.observe(this, Observer { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Log.e("LoginActivity", "Error fetching pets: $errorMessage")
                showToast(errorMessage)
                petViewModel.clearError()
            }
        })
    }

    private fun navigateToHomePage(petId: Long) {
        Log.d("LoginActivity", "Navigating to HomePageActivity with Pet ID: $petId")
        val intent = Intent(this, HomePageActivity::class.java).apply {
            putExtra("PET_ID", petId)
        }
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
